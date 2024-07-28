package id.utbandung.uts

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val firstNameEditText: TextInputEditText = findViewById(R.id.nametxt)
        val lastNameEditText: TextInputEditText = findViewById(R.id.nameblkgtxt)
        val usernameEditText: TextInputEditText = findViewById(R.id.usernametxt)
        val phoneEditText: TextInputEditText = findViewById(R.id.nohptxt)
        val bioEditText: TextInputEditText = findViewById(R.id.biotxt)
        val emailEditText: TextInputEditText = findViewById(R.id.emailtxt)
        val passwordEditText: TextInputEditText = findViewById(R.id.passtxt)
        val registerButton: Button = findViewById(R.id.button)

        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val bio = bioEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val userId = user?.uid

                            val userInfo = hashMapOf(
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "username" to username,
                                "phone" to phone,
                                "bio" to bio,
                                "email" to email
                            )

                            if (userId != null) {
                                db.collection("users").document(userId).set(userInfo)
                                    .addOnSuccessListener {
                                        Toast.makeText(this@Register, "Register Berhasil", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this@Register, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this@Register, "Register Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}