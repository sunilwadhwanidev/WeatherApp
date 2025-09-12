package com.snapwork.weatherapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.snapwork.weatherapp.databinding.ActivityLoginBinding
import com.snapwork.weatherapp.ui.HomeActivity
import com.snapwork.weatherapp.util.UserManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userManager = UserManager(this)

        // If already signed in -> skip to home
        if (userManager.isSignedIn()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        binding.signinBtn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ok = userManager.login(email, password)
            if (ok) {
                Toast.makeText(this, "Signed in", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials or no account registered", Toast.LENGTH_LONG).show()
            }
        }

        binding.goToRegisterTv.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}
