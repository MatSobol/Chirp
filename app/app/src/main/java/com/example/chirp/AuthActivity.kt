package com.example.chirp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.chirp.data.repositories.AccountRepository
import com.example.chirp.data.service.AccountService
import com.example.chirp.data.service.Client
import com.example.chirp.data.service.FriendsService
import com.example.chirp.databinding.ActivityAuthBinding
import com.example.chirp.viewModel.auth.LoginViewModel
import com.example.chirp.viewModel.auth.RegisterViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val loginViewModel: LoginViewModel by viewModel()
    private val registerViewModel: RegisterViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel.loginResultAuth.observe(this) { loginResult ->
            loginResult.success?.let {
                loginResult ?: return@let
                lifecycleScope.launch {
                    delay(1500)
                    val intent = Intent(this@AuthActivity, AppActivity::class.java)
                    startActivity(intent)
                    this@AuthActivity.finish()
                }
            }
        }
        registerViewModel.registerResultAuth.observe(this) { registerResult ->
            registerResult.success?.let {
                registerResult ?: return@let
                lifecycleScope.launch {
                    delay(1500)
                    val intent = Intent(this@AuthActivity, AppActivity::class.java)
                    startActivity(intent)
                    this@AuthActivity.finish()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onDestroy() {
        super.onDestroy()
    }
}