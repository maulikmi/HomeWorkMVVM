package com.imaginato.homeworkmvvm.ui.login

import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.imaginato.homeworkmvvm.R
import com.imaginato.homeworkmvvm.databinding.ActivityLoginBinding
import com.imaginato.homeworkmvvm.exts.hideKeyboard
import com.imaginato.homeworkmvvm.exts.isConnected
import com.imaginato.homeworkmvvm.exts.showSnackBar
import com.imaginato.homeworkmvvm.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel by viewModel<LoginActivityViewModel>()


    override fun inflateLayout(layoutInflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setUpListeners()
        startObservers()
    }

    private fun startObservers() {
        viewModel.loginResultData.observe(this) {
            binding.root.showSnackBar(it)
        }
        viewModel.isInProgress.observe(this) {
            binding.progressBar.isVisible = it
        }
        viewModel.loginValidation.observe(this) { validation ->
            when (validation) {
                LoginValidation.IS_USERNAME_BLANK -> {
                    binding.root.showSnackBar(getString(R.string.error_please_enter_username))
                }

                LoginValidation.IS_PASSWORD_BLANK -> {
                    binding.root.showSnackBar(getString(R.string.error_please_enter_password))
                }

                LoginValidation.VALID_INPUT -> {
                    if (this.isConnected()) {
                        viewModel.callLogin(
                            binding.etUserName.text.toString().trim(),
                            binding.etPassword.text.toString().trim()
                        )
                    } else {
                        binding.root.showSnackBar(getString(R.string.error_check_internet))
                    }
                }

                else -> {}
            }
        }
    }

    private fun setUpListeners() {
        binding.btnLogin.setOnClickListener {
            hideKeyboard()
            viewModel.checkLoginValidation(
                binding.etUserName.text.toString().trim(),
                binding.etPassword.text.toString().trim()
            )
        }
    }

}