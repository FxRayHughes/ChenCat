package module.report.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Reserves(
    @SerialName("click")
    var click: String,
    @SerialName("expose")
    var expose: String
)