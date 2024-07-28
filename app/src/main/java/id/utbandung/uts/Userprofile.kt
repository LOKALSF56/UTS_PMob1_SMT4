package id.utbandung.uts

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import android.widget.ImageView
import android.net.Uri
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import java.io.ByteArrayOutputStream
import com.squareup.picasso.Picasso

class Userprofile : AppCompatActivity() {
    private lateinit var profileImageView: ImageView
    private lateinit var nametxt: TextInputEditText
    private lateinit var nameblkgtxt: TextInputEditText
    private lateinit var usernametxt: TextInputEditText
    private lateinit var nohptxt: TextInputEditText
    private lateinit var biotxt: TextInputEditText
    private lateinit var emailtxt: TextInputEditText
    private lateinit var passtxt: TextInputEditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_userprofile)
        profileImageView = findViewById(R.id.profileImageView)
        nametxt = findViewById(R.id.nametxt)
        nameblkgtxt = findViewById(R.id.nameblkgtxt)
        usernametxt = findViewById(R.id.usernametxt)
        nohptxt = findViewById(R.id.nohptxt)
        biotxt = findViewById(R.id.biotxt)
        emailtxt = findViewById(R.id.emailtxt)
        updateButton = findViewById(R.id.button)
        deleteButton = findViewById(R.id.buttonapus)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        profileImageView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        updateButton.setOnClickListener {
            updateProfile()
        }

        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        loadUserProfile()
    }

    private fun loadUserProfile() {
        val userId = intent.getStringExtra("userId")
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        nametxt.setText(document.getString("namadpn"))
                        nameblkgtxt.setText(document.getString("namablkg"))
                        usernametxt.setText(document.getString("username"))
                        nohptxt.setText(document.getString("nohp"))
                        biotxt.setText(document.getString("bio"))
                        emailtxt.setText(document.getString("email"))

                        val profileImageUrl = document.getString("profileImageUrl")
                        if (!profileImageUrl.isNullOrEmpty()) {
                            Picasso.get().load(profileImageUrl).into(profileImageView)
                        }
                    } else {
                        Toast.makeText(this, "Document does not exist.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error getting documents: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateProfile() {
        val userId = intent.getStringExtra("userId") ?: return
        val firstName = nametxt.text.toString()
        val lastName = nameblkgtxt.text.toString()
        val username = usernametxt.text.toString()
        val phone = nohptxt.text.toString()
        val bio = biotxt.text.toString()
        val email = emailtxt.text.toString()

        val userUpdates = mapOf(
            "namadpn" to firstName,
            "namablkg" to lastName,
            "username" to username,
            "nohp" to phone,
            "bio" to bio,
            "email" to email
        )

        firestore.collection("users").document(userId).set(userUpdates)
            .addOnSuccessListener {
                if (imageUri != null) {
                    uploadImageToFirebaseStorage(userId)
                } else {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    redirectToHomePage()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error updating profile: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage(userId: String) {
        val storageRef = storage.reference.child("profileImages/$userId")
        val bitmap = (profileImageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        storageRef.putBytes(data)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    firestore.collection("users").document(userId)
                        .update("profileImageUrl", uri.toString())
                        .addOnSuccessListener {
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            redirectToHomePage()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error uploading image: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            profileImageView.setImageURI(imageUri)
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Apa anda yakin mau menghapus akun ini?")
            .setPositiveButton("Yes") { dialog, id ->
                deleteAccount()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun deleteAccount() {
        val userId = intent.getStringExtra("userId") ?: return
        firestore.collection("users").document(userId).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error deleting account: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun redirectToHomePage() {
        val intent = Intent(this, HomePage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
