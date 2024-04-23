package com.josephabirizk.currencyexchange

import androidx.appcompat.app.AppCompatActivity
import com.josephabirizk.currencyexchange.MainActivity
import android.os.Bundle
import com.josephabirizk.currencyexchange.api.ExchangeService
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.josephabirizk.currencyexchange.api.Authentication
import com.josephabirizk.currencyexchange.api.model.Token
import com.josephabirizk.currencyexchange.api.model.User
import android.view.View
import android.widget.Button
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent


class LoginActivity : AppCompatActivity() {
    private var usernameEditText: TextInputLayout? = null
    private var passwordEditText: TextInputLayout? = null
    private var loginButton: Button? = null
    private var BackButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.txtInptUsername)
        passwordEditText = findViewById(R.id.txtInptPassword)
        loginButton = findViewById(R.id.btnLogin)
        BackButton = findViewById(R.id.btnBack)

        BackButton?.setOnClickListener { view ->
            navigateToMainPage()
        }

        loginButton?.setOnClickListener { view ->
            performLogin()
        }
    }

    private fun performLogin() {
        val user = User()
        user.username = usernameEditText?.editText?.text.toString()
        user.password = passwordEditText?.editText?.text.toString()

        ExchangeService.exchangeApi().authenticate(user).enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>, t: Throwable) {
                Snackbar.make(loginButton as View, "Login failed.", Snackbar.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.isSuccessful) {
                    response.body()?.token?.let { token ->
                        Authentication.saveToken(token)
                        navigateToMainPage()
                    } ?: Snackbar.make(loginButton as View, "Login failed.", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(loginButton as View, "Invalid credentials.", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun navigateToMainPage() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}