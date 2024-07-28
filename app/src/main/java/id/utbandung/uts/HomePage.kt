package id.utbandung.uts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class HomePage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userAdapter: UserAdapter
    private lateinit var usersListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        usersListView = findViewById(R.id.listdata)
        userAdapter = UserAdapter(this, ArrayList())
        usersListView.adapter = userAdapter

        usersListView.setOnItemClickListener { parent, view, position, id ->
            val selectedUser = userAdapter.getItem(position) as User
            val intent = Intent(this, Userprofile::class.java)
            intent.putExtra("userId", selectedUser.id) // Pass user ID
            startActivity(intent)
        }

        val loginButton: Button = findViewById(R.id.buttonLogin)
        loginButton.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        loadUsers()
    }

    private fun loadUsers() {
        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                val usersList = ArrayList<User>()
                for (document in documents) {
                    val user = document.toObject<User>().copy(id = document.id)
                    usersList.add(user)
                }
                userAdapter.updateUsers(usersList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting documents: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
