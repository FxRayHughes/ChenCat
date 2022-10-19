package ray.mintcat.chencat.module.shst

import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import ray.mintcat.chencat.PluginMain
import java.lang.Integer.parseInt

object FxShstAPI {


    fun getEncoded(): String {
        var head: Headers? = null
        val dataStr = try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .header("Content-Type", "text")
                .header(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36 Edg/106.0.1370.47"
                )
                .url("http://jwky.jltc.edu.cn:89/Logon.do?method=logon")
                .post(FormBody.Builder().apply {
                    add("flag", "sess")
                }.build())
                .build()
            val response = client.newCall(request).execute()
            head = response.headers
            response.body?.string() ?: "ERROR"
        } catch (e: Exception) {
            "请求异常: ${e.message} "
        }
        var scode = dataStr.split("#")[0]
        val sxh = dataStr.split("#")[1]
        val code = PluginMain.shst.account + "%%%" + PluginMain.shst.password
        var encoded = ""
        var i = 0
        while (i < code.length) {
            if (i < 20) {
                encoded = encoded + code.substring(i, i + 1) + scode.substring(0, parseInt(sxh.substring(i, i + 1)))
                scode = scode.substring(parseInt(sxh.substring(i, i + 1)), scode.length)
            } else {
                encoded += code.substring(i, code.length)
                i = code.length
            }
            i++
        }
        head!!.forEach {
            println(it.first + "->" + it.second)
        }
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .headers(head)
                .url("http://jwky.jltc.edu.cn:89/Logon.do?method=logon")
                .post(FormBody.Builder().apply {
                    add("userAccount", PluginMain.shst.account)
                    add("userPassword", "")
                    add("encoded", encoded)
                }.build()).build()
            val response = client.newCall(request).execute()
            println(response.body?.string() ?: "ERROR")
        } catch (e: Exception) {
            "请求异常: ${e.message} "
        }
        return encoded
    }

    fun getTable() {
    }

}