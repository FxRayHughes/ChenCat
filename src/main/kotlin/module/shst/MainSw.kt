package module.shst

import module.shst.Http.httpRequest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

/**
 * @author Czy
 * @time Jul 6, 2019
 * @detail *
 */
class MainSw {
    /**
     * 强智教务系统
     */
    ////////////////////////////////////////////////////////
    val account = "22010230122"
    val password = "Yuchang123!"
    val url = "http://jwky.jltc.edu.cn:89/app.do"
    ////////////////////////////////////////////////////////
    /**
     * 注意：由于处理Json需要引入Json包，所以暂时不处理Json数据，假设当前学期是2018-2019-2，当前周次是18周
     * 引入Json包后可以直接调用getCurrentTime()方法得到字符串再转化为Json获取数据
     * 其实学期与当前周次是可以自行计算的，还可以减少对强智服务器的请求
     */
    ////////////////////////////////////////////////////////
    val curWeek = "8"
    val curTerm = "2022-2023-1"

    ////////////////////////////////////////////////////////
    val params: MutableMap<String, String> = HashMap()
    val headers: MutableMap<String, String> = HashMap()

    val cookie: String

    init {
        params["method"] = "authUser"
        params["xh"] = account
        params["pwd"] = password
        val reqResult = httpRequest(url, params, "GET", headers)
        println(reqResult)
        val reqResultArr = reqResult!!.split(",")
        if (reqResultArr[0][9] == '0') {
            println("登录失败")
            exitProcess(0)
        } else {
            headers["token"] = "reqResultArr[1].substring(9, reqResultArr[2].length -1)"
            cookie =" reqResultArr[1].substring(9, reqResultArr[2].length -1)"
        }
    }

    val studentInfo: MainSw
        get() {
            params["method"] = "getUserInfo"
            params["xh"] = account
            return this
        }
    val currentTime: MainSw
        get() {
            val df = SimpleDateFormat("yyyy-MM-dd")
            params["method"] = "getCurrentTime"
            params["currDate"] = df.format(Date())
            return this
        }
    val table: MainSw
        get() {
            params["method"] = "getKbcxAzc"
            params["xh"] = account
            params["xnxqid"] = curTerm
            params["zc"] = curWeek
            return this
        }

    fun setWeek(week: String): MainSw {
        params["zc"] = week
        return this
    }

    val grade: MainSw
        get() {
            params["method"] = "getCjcx"
            params["xh"] = account
            params["xnxqid"] = ""
            return this
        }

    fun setTerm(term: String): MainSw {
        params["xnxqid"] = term
        return this
    }

    fun getClassroom(idleTime: String): MainSw {
        val df = SimpleDateFormat("yyyy-MM-dd")
        params["method"] = "getKxJscx"
        params["time"] = df.format(Date())
        params["idleTime"] = idleTime
        return this
    }

    val examInfo: MainSw
        get() {
            params["method"] = "getKscx"
            params["xh"] = account
            return this
        }

    fun exec(): String? {
        val result = httpRequest(url, params, "GET", headers)
        params.clear()
        println(result)
        return result
    }
}