package ray.mintcat.chencat.module.clazz.command

import ray.mintcat.chencat.command.BaseCommand
import net.mamoe.mirai.message.data.MessageChain
import ray.mintcat.chencat.command.Sender
import ray.mintcat.chencat.command.ShortCommand
import ray.mintcat.chencat.module.clazz.XLS
import java.util.*

object DayCommand : BaseCommand("今日课表"), ShortCommand {

    override val alias: MutableList<String>
        get() = mutableListOf("课表", "查课")

    override val subCommand: Map<String, String>
        get() = mapOf(
            "课节(数字)" to "获取目标课节内容"
        )

    override suspend fun run(sender: Sender, from: MessageChain) {
        val command = from.contentToString().split(" ")
        if (command.size <= 1) {
            this.getInfo(sender, from)
            return
        }
        sender.sendMessage("查询中 请等待...")
        val now = if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 == 0){
            7
        }else{
            Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
        }
        val title = command[1].toIntOrNull() ?: 0
        if (title > 7){
            sender.sendMessage("暂时还没这么多课")
            return
        }
        val data = XLS.getClassData(now, title)
        if (data == null || data.none) {
            sender.sendMessage(
                """
            本周${now} 第 ${title} 节课
            无课
        """.trimIndent(), from
            )
            return
        }
        sender.sendMessage(
            """
            本周 $now 第 $title 节课
            课程名称: ${data.name}
            授课教师: ${data.teacher}
            教学位置: ${data.location}
            
            首节时间: ${data.getTimeData().first}
            次节时间: ${data.getTimeData().last}
            
            腾讯会议号: ${data.getRoom().first}
            腾讯会议密码: ${data.getRoom().second}
        """.trimIndent(), from
        )
    }

    override val short: List<String>
        get() = listOf("今天什么课")

    override suspend fun evalShort(short: String, sender: Sender, from: MessageChain) {
        val now = if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 == 0){
            7
        }else{
            Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
        }
        val stringBuilder = StringBuilder()
        stringBuilder.append("今日课程: \n")
        XLS.data[now]?.list?.forEachIndexed { index, classData ->
            if (classData.none) {
                stringBuilder.append("第${classData.index +1}节: 无课 \n")
                return@forEachIndexed
            }
            stringBuilder.append("第${classData.index +1}节: ${classData.name} \n")
        }
        stringBuilder.append("详细内容输入: .查课 课节数字")
        sender.sendMessage(stringBuilder.toString(), from)
    }

}