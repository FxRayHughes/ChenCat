package module.report.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportCompass(
    @SerialName("actiontype")
    var actiontype: String,
    @SerialName("reserves")
    var reserves: Reserves,
    @SerialName("subactiontype")
    var subactiontype: String,
    @SerialName("table")
    var table: String
)