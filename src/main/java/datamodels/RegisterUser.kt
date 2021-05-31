package datamodels

data class RegisterUser(
        val role: Int,
        val name: String?,
        val email: String?,
        val username: String?,
        val sex: String?
)