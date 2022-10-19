package ray.mintcat.chencat

import module.report.data.AppData
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import ray.mintcat.chencat.module.http.HttpAPI
import ray.mintcat.chencat.module.new.NewTitle
import ray.mintcat.chencat.module.shst.FxShstAPI
import java.io.File
import java.util.*

@OptIn(ConsoleExperimentalApi::class)
suspend fun main() {
    MiraiConsoleTerminalLoader.startAsDaemon()

    PluginMain.load()
    PluginMain.enable()

    PluginMain.json.decodeFromString(AppData.serializer(),"")


}