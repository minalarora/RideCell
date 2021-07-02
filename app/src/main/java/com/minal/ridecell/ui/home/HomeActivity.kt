package com.minal.ridecell.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.minal.ridecell.R
import com.minal.ridecell.databinding.ActivityHomeBinding
import com.minal.ridecell.ui.home.map.MapFragment
import com.minal.ridecell.ui.home.profile.ProfileFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)

        if (savedInstanceState == null) setCurrentFragment(MapFragment(),"map")

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.map -> setCurrentFragment(MapFragment(),"map")
                R.id.profile -> setCurrentFragment(ProfileFragment(),"user")
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment, tag: String)
    {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.fragmentContainer.id,fragment,tag)
            commit()
        }
    }

    override fun onStart() {
        super.onStart()
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)
    }
}