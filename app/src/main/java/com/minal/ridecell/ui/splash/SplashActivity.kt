package com.minal.ridecell.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.minal.ridecell.R
import com.minal.ridecell.databinding.ActivitySplashBinding
import com.minal.ridecell.ui.login.LoginActivity
import com.minal.ridecell.ui.home.HomeActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this@SplashActivity,R.layout.activity_splash)
            .apply {

                viewModel = ViewModelProvider(this@SplashActivity).get(SplashViewModel::class.java).apply {

                    checkCurrentUser(this@SplashActivity){
                        it?.let { goToHome() }
                            ?: goToLogin()
                    }
                }
            }

    }

    override fun onStart() {
        super.onStart()
        window.statusBarColor = ContextCompat.getColor(this,R.color.skin)
    }

    private fun goToLogin()
    {
        Intent(this@SplashActivity, LoginActivity::class.java).
        also {
            startActivity(it)
            this@SplashActivity.finish()
        }
    }

    private fun goToHome()
    {
        Intent(this@SplashActivity,HomeActivity::class.java).
        also {
            startActivity(it)
            this@SplashActivity.finish()
        }
    }
}