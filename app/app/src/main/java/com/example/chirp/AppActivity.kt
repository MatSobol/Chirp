package com.example.chirp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.chirp.data.repositories.AccountRepository
import com.example.chirp.data.service.AuthService
import com.example.chirp.databinding.ActivityAppBinding
import com.example.chirp.databinding.ActivityMainBinding
import com.example.chirp.utility.initAfterLogin
import com.example.chirp.utility.unload
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppBinding
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            throwable.message?.let {
                failAlert(
                    this, findViewById<View>(android.R.id.content).rootView,
                    it
                )
            } ?: run {
                failAlert(
                    this, findViewById<View>(android.R.id.content).rootView,
                    "Unknown error"
                )
            }
            Log.e("AppError", "Uncaught exception in thread ${thread.name}", throwable)
        }

        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val drawerLayout: DrawerLayout = binding.appBarMain.drawerLayout
        val navView: NavigationView = binding.appBarMain.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_chatsPreview, R.id.nav_friendsFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val logOut: TextView = findViewById(R.id.logout)

        val authService: AuthService = getKoin().get()

        logOut.setOnClickListener {
            authService.logOut()
            unload()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            this.finish()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}