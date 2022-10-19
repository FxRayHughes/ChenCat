package module.report.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Extra(
    @SerialName("app_type")
    var appType: Int,
    @SerialName("appid")
    var appid: Int,
    @SerialName("uin")
    var uin: Long
)