package id.utbandung.uts

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        val loginButton: Button = findViewById(R.id.buttonLogin)
        loginButton.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        val listView: ListView = findViewById(R.id.listdata)

        val beritaString: String = getString(R.string.Beritaedan)

        val beritaArray = beritaString.split("\n").toTypedArray()

        val adapter = adapter(this, android.R.layout.simple_list_item_1, beritaArray)

        listView.adapter = adapter
    }
}