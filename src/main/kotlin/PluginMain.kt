package ray.mintcat.chencat

import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import module.report.data.AppData
import module.shst.MainSw
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.broadcast
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.LightApp
import net.mamoe.mirai.utils.info
import ray.mintcat.chencat.command.Command
import ray.mintcat.chencat.command.Sender
import ray.mintcat.chencat.module.clazz.XLS
import ray.mintcat.chencat.module.groupphoto.GroupPhotoEvent
import ray.mintcat.chencat.module.new.NewTitle
import ray.mintcat.chencat.module.report.Report
import ray.mintcat.chencat.module.report.ReportConfig
import ray.mintcat.chencat.module.task.TaskData


object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "ray.mintcat.chencat",
        name = "辰猫",
        version = "0.1.0"
    )
) {

    val bot by lazy {
        Bot.getInstance(137458045L)
    }
    val group by lazy {
        bot.getGroup(531627318L)
    }
    val publicGroup by lazy {
        bot.getGroup(346036074L)
    }

    val eventChannel = GlobalEventChannel.parentScope(this)

    val json = Json {
        coerceInputValues = true
    }

    override fun onEnable() {
        logger.info { "Plugin loaded" }
        //配置文件目录 "${dataFolder.absolutePath}/"
        XLS.load()
        Command.loadAll()
        TaskData.reload()
        ReportConfig.reload()
        eventChannel.subscribeAlways<BotOnlineEvent> {
            launch {
                NewTitle.run()
            }
        }
        eventChannel.subscribeAlways<GroupMessageEvent> {
            val messagex = message[1]
            try {
                if (messagex is LightApp) {
                    val appdata = json.decodeFromString(AppData.serializer(), messagex.contentToString())
                    if (appdata.app == "com.tencent.groupphoto"){
                        GroupPhotoEvent(this,appdata).broadcast()
                    }
                }
            } catch (_: Exception) {

            }
        }
        globalEventChannel().subscribeAlways<FriendMessageEvent> {
            Command.runCommand(Sender(null, null, friend, this), this.message)
        }
        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            Command.runCommand(Sender(this.group, this.sender, null, this), this.message)
        }
        eventChannel.subscribeAlways<FriendMessageEvent> {
        }
        eventChannel.subscribeAlways<NewFriendRequestEvent> {
            //自动同意好友申请
            accept()
        }
        eventChannel.subscribeAlways<BotInvitedJoinGroupRequestEvent> {
            //自动同意加群申请
            accept()
        }

        Report.test()
    }
}
