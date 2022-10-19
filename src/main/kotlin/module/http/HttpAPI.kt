package ray.mintcat.chencat.module.http

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.nio.charset.Charset

object HttpAPI {

    fun httpGet(url: String): String? {
        return try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()
            val response = client.newCall(request).execute()
            String(response.body?.bytes()!!, Charset.forName("GB2312"))
        } catch (e: Exception) {
            "请求异常: ${e.message} "
        }
    }

    fun httpPost(url: String, body: RequestBody): String? {
        return try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
            val response = client.newCall(request).execute()
            response.body?.string()
        } catch (e: Exception) {
            "请求异常: ${e.message} "
        }
    }

}