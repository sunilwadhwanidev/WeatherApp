package com.snapwork.weatherapp.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.snapwork.weatherapp.databinding.ActivityRegisterBinding
import com.snapwork.weatherapp.ui.HomeActivity
import com.snapwork.weatherapp.util.UserManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userManager = UserManager(this)

        // If already signed in, skip register
        if (userManager.isSignedIn()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        binding.registerBtn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString()
            val confirm = binding.confirmPasswordEt.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = userManager.register(email, password)
            if (success) {
                Toast.makeText(this, "Registered successfully — please sign in", Toast.LENGTH_SHORT).show()
                // send user to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "An account already exists on this device", Toast.LENGTH_LONG).show()
            }
        }

        binding.goToLoginTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
