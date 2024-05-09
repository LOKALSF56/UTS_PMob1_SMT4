package id.utbandung.uts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val forgotPasswordButton: TextView = findViewById(R.id.register)
        forgotPasswordButton.setOnClickListener {
            Intent(this,Register::class.java).also{
                startActivity(it)
                finish()
            }
        }
        val loginButton: Button = findViewById(R.id.button)
        loginButton.setOnClickListener {
            Toast.makeText(this@Login, "Login Berhasil", Toast.LENGTH_SHORT).show()
        }
    }
}