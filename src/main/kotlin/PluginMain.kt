package ray.mintcat.chencat

import command.Command
import command.Sender
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.utils.info


object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "ray.mintcat.chencat",
        name = "辰猫",
        version = "0.1.0"
    )
) {
    val eventChannel = GlobalEventChannel.parentScope(this)

    override fun onEnable() {
        logger.info { "Plugin loaded" }
        //配置文件目录 "${dataFolder.absolutePath}/"
        XLS.load()
        Command.loadAll()
        globalEventChannel().subscribeAlways<FriendMessageEvent> {
            Command.runCommand(Sender(null, null, friend), this.message)
        }
        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            Command.runCommand(Sender(this.group, this.sender, null), this.message)
        }
        eventChannel.subscribeAlways<FriendMessageEvent> {
            //好友信息
            sender.sendMessage("hi")
        }
        eventChannel.subscribeAlways<NewFriendRequestEvent> {
            //自动同意好友申请
            accept()
        }
        eventChannel.subscribeAlways<BotInvitedJoinGroupRequestEvent> {
            //自动同意加群申请
            accept()
        }
    }
}
