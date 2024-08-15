package com.example.tanahore.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tanahore.databinding.ActivityWelcomeBinding
import com.example.tanahore.preference.UserManager

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private var isCheckboxChecked: Boolean = false
    private lateinit var preference: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preference = UserManager(this)
        setupAction()
        setupView()
        if (preference.getWelcome()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        setupCheckbox()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction(){
        binding.button.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    private fun setupCheckbox() {
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            isCheckboxChecked = isChecked
            preference.setWelcome(isCheckboxChecked)
        }
    }

}