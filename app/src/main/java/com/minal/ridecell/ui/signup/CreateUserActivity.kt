package com.minal.ridecell.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.minal.ridecell.R
import com.minal.ridecell.databinding.ActivityCreateuserBinding
import com.minal.ridecell.extensions.createToast
import com.minal.ridecell.extensions.hideKeyBoard
import com.minal.ridecell.extensions.putData
import com.minal.ridecell.ui.home.HomeActivity
import com.minal.ridecell.ui.login.LoginActivity

class CreateUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateuserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityCreateuserBinding>(this, R.layout.activity_createuser)
            .apply {
                viewModel = ViewModelProvider(this@CreateUserActivity).get(SignUpViewModel::class.java)
                    .apply {
                        error.observe(this@CreateUserActivity) {
                            progressBar.visibility = View.GONE
                            this@CreateUserActivity.createToast(it)
                        }

                        token.observe(this@CreateUserActivity) {
                            progressBar.visibility = View.GONE
                            this@CreateUserActivity.putData("token", it?.authentication_token)
                            goToHome()
                        }
                    }

                create.setOnClickListener {
                    it.requestFocus()
                    this@CreateUserActivity.hideKeyBoard()
                    progressBar.visibility = View.VISIBLE
                    this.viewModel?.createUser(email.text.toString(),
                        password.text.toString(),
                        confirmPassword.text.toString(),
                        name.text.toString())
                }

                login.setOnClickListener {
                    gotoLogin()
                }
            }
    }

    private fun gotoLogin()
    {
        Intent(this,LoginActivity::class.java)
            .also {
                startActivity(it)
                this.finish()
            }
    }

    private fun goToHome()
    {
        Intent(this, HomeActivity::class.java).
        also {
            startActivity(it)
            this.finish()
        }
    }
}