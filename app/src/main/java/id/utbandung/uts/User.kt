package id.utbandung.uts

data class User(
    var id: String = "",
    var email: String = "",
    var username: String = "",
    var namadpn: String = "",
    var namablkg: String = "",
    var nohp: String = "",
    var bio: String = "",
    var pass: String = "",
    var profileImageUrl: String = ""
)

fun main() {
    val user = User(
        id = "ID",
        email = "Email",
        username = "Username",
        namadpn = "Nama Depan",
        namablkg = "Nama Belakang",
        nohp = "Nomor Telepon",
        bio = "Bio",
        pass = "Password",
        profileImageUrl = ""
    )
}
