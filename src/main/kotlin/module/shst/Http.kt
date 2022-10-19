package module.shst

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * @author Czy
 * @time Jul 6, 2019
 * @detail Http请求类
 */
object Http {

    @JvmStatic
    fun httpRequest(url: String, param: Map<String, String>, method: String, headers: Map<String, String>): String? {
        val paramDipose = pramHandle(param)
        return if (method.equals("GET", ignoreCase = true)) {
            doGet(url + paramDipose, headers)
        } else {
            doPost(url, paramDipose, headers)
        }
    }

    private fun pramHandle(params: Map<String, String>): String {
        val urlParam = StringBuilder("?")
        for ((key, value) in params) {
            urlParam.append("$key=$value&")
        }
        return urlParam.toString()
    }

    private fun doGet(httpurl: String, headers: Map<String, String>): String? {
        var connection: HttpURLConnection? = null
        var result: String? = null // 返回结果字符串
        try {
            // 创建远程url连接对象
            val url = URL(httpurl)
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = url.openConnection() as HttpURLConnection
            // 设置连接方式：get
            connection.requestMethod = "GET"
            // 设置连接主机服务器的超时时间：15000毫秒
            connection!!.connectTimeout = 15000
            // 设置读取远程返回的数据时间：60000毫秒
            connection.readTimeout = 60000
            for ((key, value) in headers) {
                connection.setRequestProperty(key, value)
            }
            // 发送请求
            connection.connect()
            // 通过connection连接，获取输入流
            if (connection.responseCode == 200) result = getResult(connection)
        } catch (e: Exception) {
            println(e.toString())
        }
        return result
    }

    private fun doPost(httpUrl: String, param: String, headers: Map<String, String>): String? {
        var connection: HttpURLConnection? = null
        var os: OutputStream? = null
        var result: String? = null
        try {
            val url = URL(httpUrl)
            connection = url.openConnection() as HttpURLConnection
            connection!!.requestMethod = "POST"
            connection.connectTimeout = 15000
            connection.readTimeout = 60000
            connection.doOutput = true
            connection.doInput = true
            for ((key, value) in headers) {
                connection.setRequestProperty(key, value)
            }
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            os = connection.outputStream
            os.write(param.toByteArray())
            if (connection.responseCode == 200) result = getResult(connection)
        } catch (e: Exception) {
            println(e.toString())
        } finally {
            if (null != os) {
                try {
                    os.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return result
    }

    private fun getResult(connection: HttpURLConnection?): String {
        var `is`: InputStream? = null
        var br: BufferedReader? = null
        val sb = StringBuilder()
        try {
            `is` = connection!!.inputStream
            br = BufferedReader(InputStreamReader(`is`, StandardCharsets.UTF_8))
            var temp: String? = ""
            while (br.readLine().also { temp = it } != null) {
                sb.append(temp)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeConn(br, `is`, connection)
        }
        return sb.toString()
    }

    private fun closeConn(br: BufferedReader?, `is`: InputStream?, connection: HttpURLConnection?) {
        if (null != br) {
            try {
                br.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (null != `is`) {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        connection?.disconnect()
    }
}