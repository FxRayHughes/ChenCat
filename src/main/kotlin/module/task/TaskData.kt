package ray.mintcat.chencat.module.task

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object TaskData : AutoSavePluginData("TaskData") {
    var task: MutableMap<String, String> by value()
}