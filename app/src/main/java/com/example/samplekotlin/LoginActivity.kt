package com.example.samplekotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = getString(R.string.bank_name)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val signUpButton = findViewById<Button>(R.id.go_to_signup)
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}