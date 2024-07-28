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
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val emailEditText: TextInputEditText = findViewById(R.id.emailtxt)
        val passwordEditText: TextInputEditText = findViewById(R.id.passtxt)
        val registerTextView: TextView = findViewById(R.id.register)
        val loginButton: Button = findViewById(R.id.button)

        registerTextView.setOnClickListener {
            Intent(this, Register::class.java).also {
                startActivity(it)
            }
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, navigate to HomePage
                            Toast.makeText(this@Login, "Login Berhasil", Toast.LENGTH_SHORT).show()
                            Intent(this, HomePage::class.java).also {
                                startActivity(it)
                                finish()
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this@Login, "Login Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}