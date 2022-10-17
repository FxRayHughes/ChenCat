package ray.mintcat.chencat.data

import ray.mintcat.chencat.XLS

data class ClassData(
    val name: String = "",
    val teacher: String = "",
    val time: String = "",
    val location: String = "",
    val index: Int = 0,
    val none: Boolean = false
) {

    fun getTimeData(): TimeData {
        val time = XLS.timeData.firstOrNull { this.index == (it.index - 1) }
        return time ?: TimeData(-1, "错误", "错误", "获取错误联系卜帅辰")
    }

    fun getRoom(): Pair<String, String> {
        return XLS.roomData[teacher] ?: ("无记录" to "无记录")
    }

}