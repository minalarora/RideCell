package com.minal.ridecell.ui.home.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.minal.ridecell.R
import com.minal.ridecell.databinding.FragmentProfileBinding
import com.minal.ridecell.extensions.createToast
import com.minal.ridecell.extensions.getData
import com.minal.ridecell.extensions.putData
import com.minal.ridecell.ui.login.LoginActivity


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ProfileFragment.viewLifecycleOwner
            viewModel = ViewModelProvider(this@ProfileFragment).get(ProfileViewModel::class.java)
                .apply {

                    error.observe(this@ProfileFragment.viewLifecycleOwner, {
                        progressBar.visibility = View.GONE
                        requireContext().createToast(it)
                    })

                    user.observe(this@ProfileFragment.viewLifecycleOwner, {
                        progressBar.visibility = View.GONE
                        binding.displayName.text = it.display_name
                        binding.days.text = prettyPrint(it.created_at)
                    })

                    progressBar.visibility = View.VISIBLE
                    checkCurrentUser(requireContext().getData("token"))
                }

            logout.setOnClickListener {
                requireContext().putData("token",null)
                gotoLogin()
            }
        }

        return binding.root
    }

    private fun gotoLogin()
    {
        Intent(this.requireActivity(), LoginActivity::class.java)
            .also {
                startActivity(it)
                this.requireActivity().finish()
            }
    }
}