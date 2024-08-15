package com.example.tanahore.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tanahore.R
import com.example.tanahore.data.Results
import com.example.tanahore.data.viewmodel.FactoryVM
import com.example.tanahore.data.viewmodel.LoginVM
import com.example.tanahore.databinding.ActivityLoginBinding
import com.example.tanahore.preference.UserManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var factory: FactoryVM
    private val viewModel: LoginVM by viewModels {
        factory
    }
    private lateinit var preferences: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE
        factory = FactoryVM.getInstance(this)
        preferences = UserManager(this)

        setAction()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setAction() {
        binding.txtSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            login()
        }
        val edLoginPassword = binding.edLoginPassword
        val icShowPass = binding.icShowPass
        binding.icShowPass.setOnClickListener{
            togglePasswordVisibility(edLoginPassword, icShowPass)
        }
    }
    private fun togglePasswordVisibility(edLoginPassword: EditText, icShowPass: ImageView) {
        val inputType = if (edLoginPassword.inputType == (InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT)) {
            InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        }
        edLoginPassword.inputType = inputType
        icShowPass.setImageResource(if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24)
        edLoginPassword.setSelection(edLoginPassword.text.length)
    }

    private fun login() {
        val email = binding.edLoginEmail.text.toString()
        Log.d( "login", "login: $email")
        val password = binding.edLoginPassword.text.toString()

        viewModel.login(email, password).observe(this) { response ->
            if (response != null) {
                when (response) {
                    is Results.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Results.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val data = response.data
                        preferences.setToken(data.data.accessToken)
                        preferences.setTokenCreated(System.currentTimeMillis())
                        preferences.setEmail(email)
                        preferences.setPassword(password)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra(MainActivity.EXTRA_DATA, data.data.accessToken)
                        startActivity(intent)
                        finish()
                    }
                    is Results.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Email or Password is Incorrect", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}