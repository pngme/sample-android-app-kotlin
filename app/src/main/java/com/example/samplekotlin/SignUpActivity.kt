package com.example.samplekotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = getString(R.string.bank_name)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }
}