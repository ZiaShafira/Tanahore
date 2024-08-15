package com.example.tanahore.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tanahore.R
import com.example.tanahore.data.Results
import com.example.tanahore.data.viewmodel.FactoryVM
import com.example.tanahore.data.viewmodel.RegisterVM
import com.example.tanahore.databinding.ActivityRegisterBinding
import com.example.tanahore.preference.UserManager

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var factory: FactoryVM
    private lateinit var preferences: UserManager
    private val viewModel: RegisterVM by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE
        factory = FactoryVM.getInstance(this)
        preferences = UserManager(this)

        setAction()
    }

    private fun setAction() {
        binding.txtSignup.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            register()
        }

        val edLoginPassword = binding.edRegisterPassword
        val icShowPass = binding.icShowPass
        binding.icShowPass.setOnClickListener{
            togglePasswordVisibility(edLoginPassword, icShowPass)
        }
    }

    private fun togglePasswordVisibility(edLoginPassword: EditText, icShowPass: ImageView) {
        val isPasswordVisible = edLoginPassword.inputType != (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

        if (isPasswordVisible) {
            edLoginPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            icShowPass.setImageResource(R.drawable.baseline_visibility_off_24)
        } else {
            edLoginPassword.inputType = InputType.TYPE_CLASS_TEXT
            icShowPass.setImageResource(R.drawable.baseline_visibility_24)
        }
        edLoginPassword.setSelection(edLoginPassword.text.length)
    }



    private fun register() {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        viewModel.register(name, email, password).observe(this) { response ->
            when (response) {
                is Results.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Results.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val data = response.data
                    preferences.setToken(data.data.accessToken)
                    preferences.setTokenCreated(System.currentTimeMillis())
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    intent.putExtra(MainActivity.EXTRA_DATA, data.data.accessToken)
                    startActivity(intent)
                    finish()
                }
                is Results.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
                else -> {}
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}