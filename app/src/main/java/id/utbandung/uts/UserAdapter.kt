package id.utbandung.uts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class UserAdapter(private val context: Context, private var users: ArrayList<User>) : BaseAdapter() {

    override fun getCount(): Int {
        return users.size
    }

    override fun getItem(position: Int): User {
        return users[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val user = users[position]
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: inflater.inflate(R.layout.activity_main, parent, false)

        val imageView: ImageView = view.findViewById(R.id.listnameIMG)
        val nameTextView: TextView = view.findViewById(R.id.list_name)
        val itemTextView: TextView = view.findViewById(R.id.list_item)

        nameTextView.text = user.username
        itemTextView.text = user.bio

        if (user.profileImageUrl.isNotEmpty()) {
            Picasso.get().load(user.profileImageUrl).into(imageView)
        } else {
            imageView.setImageResource(R.drawable.illustration)
        }

        return view
    }

    fun updateUsers(newUsers: ArrayList<User>) {
        users = newUsers
        notifyDataSetChanged()
    }
}
