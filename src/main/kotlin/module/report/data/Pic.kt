package module.report.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pic(
    @SerialName("height")
    var height: Int,
    @SerialName("isVideo")
    var isVideo: Int,
    @SerialName("url")
    var url: String,
    @SerialName("width")
    var width: Int
)