package module.report.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    @SerialName("ctime")
    var ctime: Int,
    @SerialName("token")
    var token: String
)