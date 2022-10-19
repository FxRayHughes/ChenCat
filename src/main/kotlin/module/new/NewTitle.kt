package ray.mintcat.chencat.module.new

import kotlinx.coroutines.*
import ray.mintcat.chencat.PluginMain
import ray.mintcat.chencat.command.Sender
import ray.mintcat.chencat.module.task.TaskData
import java.text.SimpleDateFormat
import java.util.*

object NewTitle {

    suspend fun run() {
        coroutineScope {
            withContext(Dispatchers.IO) {
                while (true) {
                    val calendar = Calendar.getInstance()
                    if (TaskData.task["60s"] != now() && calendar.get(Calendar.HOUR_OF_DAY) >= 8) {

                        PluginMain.bot.groups.forEach {
                            Sender(it).sendImage("https://api.03c3.cn/zb/")
                        }

                        TaskData.task["60s"] = now()
                    }
                    delay(1000 * 60)
                }
            }
        }
    }

    fun now(): String {
        val formatter = SimpleDateFormat("yyyy:MM:dd")
        return formatter.format(Date(System.currentTimeMillis()))
    }

}