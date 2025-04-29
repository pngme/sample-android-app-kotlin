package com.example.samplekotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.samplekotlin.home.MainActivity
import com.example.samplekotlin.model.User
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class SignUpActivity : AppCompatActivity() {

    private lateinit var externalIdInput: EditText
    private lateinit var firstNameInput: EditText
    private lateinit var lastNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = getString(R.string.bank_name)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        externalIdInput = findViewById(R.id.external_id_text)
        firstNameInput  = findViewById(R.id.first_name_text)
        lastNameInput   = findViewById(R.id.last_name_text)
        emailInput      = findViewById(R.id.email_text)
        phoneInput      = findViewById(R.id.phone_text)

        val createAccountButton = findViewById<Button>(R.id.create_account_button)
        createAccountButton.setOnClickListener {
            saveUserInputs()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveUserInputs() {
        val userInfo = User(
            externalId = externalIdInput.text.toString(),
            firstName  = firstNameInput.text.toString(),
            lastName   = lastNameInput.text.toString(),
            email      = emailInput.text.toString(),
            phoneNumber= phoneInput.text.toString()
        )

        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<User> = moshi.adapter(User::class.java)
        val json = jsonAdapter.toJson(userInfo)

        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE)
        sharedPreferences.edit {
            putString("userInfo", json)
            apply()
        }
    }

}