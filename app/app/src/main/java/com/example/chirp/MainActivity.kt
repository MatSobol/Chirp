package com.example.chirp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.chirp.data.repositories.AccountRepository
import com.example.chirp.data.service.AccountService
import com.example.chirp.data.service.Client
import com.example.chirp.data.service.AuthService
import com.example.chirp.data.service.FriendsService
import com.example.chirp.databinding.ActivityMainBinding
import com.example.chirp.utility.initAfterLogin
import com.example.chirp.utility.unload
import com.example.chirp.viewModel.auth.LoginViewModel
import com.example.chirp.viewModel.auth.RegisterViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.scope.activityScope
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


class MainActivity : AppCompatActivity() {

    private val appModule = module {
        single { AuthService(get()) }
        single { getPreferences(Context.MODE_PRIVATE) }
        viewModel { LoginViewModel(get()) }
        viewModel { RegisterViewModel(get()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            failAlert(this, findViewById<View>(android.R.id.content).rootView, "Global error")
            Log.e("GlobalError", "Uncaught exception in thread ${thread.name}", throwable)
        }

        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }

        val authService: AuthService = getKoin().get()

        if (!authService.isLoggedIn()) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        } else {
            initAfterLogin()
            lifecycleScope.launch {
                val accountRepository: AccountRepository = getKoin().get()
                accountRepository.initAccount()
            }
            val intent = Intent(this, AppActivity::class.java)
            startActivity(intent)
        }

    }

}