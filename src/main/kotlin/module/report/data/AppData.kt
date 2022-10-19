package module.report.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppData(
    @SerialName("app")
    var app: String,
    @SerialName("config")
    var config: Config,
    @SerialName("desc")
    var desc: String,
    @SerialName("extra")
    var extra: Extra,
    @SerialName("meta")
    var meta: Meta,
    @SerialName("prompt")
    var prompt: String,
    @SerialName("ver")
    var ver: String,
    @SerialName("view")
    var view: String
)