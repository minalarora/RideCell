package com.minal.ridecell.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.minal.ridecell.R
import com.minal.ridecell.databinding.ActivityLoginBinding
import com.minal.ridecell.extensions.createToast
import com.minal.ridecell.extensions.hideKeyBoard
import com.minal.ridecell.extensions.putData
import com.minal.ridecell.ui.home.HomeActivity
import com.minal.ridecell.ui.signup.CreateUserActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
            .apply {
                viewModel = ViewModelProvider(this@LoginActivity).get(LoginViewModel::class.java)
                    .apply {

                        error.observe(this@LoginActivity) {
                            progressBar.visibility = View.GONE
                            this@LoginActivity.createToast(it)
                        }

                        token.observe(this@LoginActivity) {
                            progressBar.visibility = View.GONE
                            this@LoginActivity.putData("token", it?.authentication_token)
                            goToHome()
                        }
                    }

                login.setOnClickListener {
                    this@LoginActivity.hideKeyBoard()
                    progressBar.visibility = View.VISIBLE
                    this.viewModel?.loginUser(email.text.toString(), password.text.toString())
                }

                createAccount.setOnClickListener {
                    gotoCreateUser()
                }

            }
    }

    private fun gotoCreateUser()
    {
        Intent(this@LoginActivity, CreateUserActivity::class.java)
            .also {
                startActivity(it)
                this@LoginActivity.finish()
            }
    }

    private fun goToHome()
    {
        Intent(this@LoginActivity, HomeActivity::class.java).
        also {
            startActivity(it)
            this@LoginActivity.finish()
        }
    }
}