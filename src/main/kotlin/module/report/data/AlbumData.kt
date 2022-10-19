package module.report.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumData(
    @SerialName("albumName")
    var albumName: String,
    @SerialName("count")
    var count: Int,
    @SerialName("desc")
    var desc: String,
    @SerialName("h5Url")
    var h5Url: String,
    @SerialName("h5UrlMobile")
    var h5UrlMobile: String,
    @SerialName("iconUrl")
    var iconUrl: String,
    @SerialName("pics")
    var pics: List<Pic>,
    @SerialName("qunid")
    var qunid: Int,
    @SerialName("reportCompass")
    var reportCompass: ReportCompass,
    @SerialName("title")
    var title: String,
    @SerialName("total")
    var total: Int
)