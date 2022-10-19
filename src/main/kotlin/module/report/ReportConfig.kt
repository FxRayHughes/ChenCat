package ray.mintcat.chencat.module.report

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object ReportConfig : AutoSavePluginData("MyData") {
    var taskList: MutableMap<String, String> by value()
    var taskData: MutableMap<String, MutableMap<String, String>> by value()
}