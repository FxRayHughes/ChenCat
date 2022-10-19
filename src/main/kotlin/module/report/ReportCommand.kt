package ray.mintcat.chencat.module.report

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.event.selectMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import ray.mintcat.chencat.PluginMain
import ray.mintcat.chencat.command.BaseCommand
import ray.mintcat.chencat.command.Sender
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.imageio.ImageIO


object ReportCommand : BaseCommand("汇报") {

    override val alias: MutableList<String>
        get() = mutableListOf()

    override val subCommand: Map<String, String>
        get() = mapOf(
            "创建 汇报代号 汇报类型" to "创建一个汇报",
            "提交 汇报代号" to "提交汇报",
            "列表 汇报代号" to "查看汇报情况",
            "导出 汇报代号" to "导出内容"
        )

    override suspend fun run(sender: Sender, from: MessageChain) {
        val command = from.contentToString().split(" ")
        when (command.getOrElse(1) { "null" }) {
            "创建" -> {
                if (command.size <= 3) {
                    this.getInfo(sender, from)
                    return
                }
                val id = command[2]
                val type = command[3]
                if (!PluginMain.group!!.contains(sender.member?.id ?: 0L)) {
                    sender.sendMessage("权限不足", from)
                    return
                }
                if (type !in arrayOf("文字", "图片")) {
                    sender.sendMessage("类型只能是 文字/图片")
                    return
                }
                if (ReportConfig.taskList.keys.contains(id)) {
                    sender.sendMessage("代号重复了 重新定义一个")
                    return
                }
                sender.group = PluginMain.group
                sender.sendMessage(
                    """
                    成功创建任务: ${id} 类型: ${type}
                    1. 请大家添加本机器人QQ好友
                    2. 私聊发送:
                    汇报 提交 ${id}
                    3. 发送要提交的内容
                    4. 详细使用方式[图文]：
                    https://xv5zac7cto.feishu.cn/docx/E6kOdWHQ0oNhn6xKpEtcbhX2nYg
                """.trimIndent()
                )
                ReportConfig.taskList[id] = type
            }

            "提交" -> {
                if (command.size <= 2) {
                    this.getInfo(sender, from)
                    return
                }
                val id = command[2]
                val type = ReportConfig.taskList[id] ?: return kotlin.run {
                    sender.sendMessage("请重新确认汇报代号")
                }
                when (type) {
                    "文字" -> {
                        sender.sendMessage("请发送要汇报的文字")
                        val text: String = sender.event!!.selectMessages {
                            has<PlainText> { it.content }
                            defaultReply { "请发送文字" }
                            timeout(30_000) {
                                sender.sendMessage("超时 请重写")
                                null
                            }
                        } ?: return
                        sender.sendMessage("汇报成功")
                        ReportConfig.taskData.getOrPut(id) { mutableMapOf() }[sender.get().id.toString()] = text
                        Sender(PluginMain.group, null, null).sendMessage(
                            """
                            汇报播报:
                            汇报代号: $id
                            汇报内容:
                            ${text}
                        """.trimIndent()
                        )
                    }

                    "图片" -> {
                        sender.sendMessage("请发送要汇报的图片")
                        val text: URL = sender.event!!.selectMessages {
                            has<Image> {
                                withContext(Dispatchers.IO) {
                                    URL(it.queryUrl())
                                }
                            }
                            defaultReply { "请发送图片" }
                            timeout(30_000) {
                                sender.sendMessage("超时 请重写")
                                null
                            }
                        } ?: return
                        withContext(Dispatchers.IO) {
                            val name =
                                PluginMain.publicGroup?.members?.firstOrNull { it.id == sender.get().id }?.nameCard
                                    ?: sender.get().id
                            val file = "${PluginMain.dataFolderPath}\\图片汇报\\${id}\\${name}\\${name}.png"
                            writeImageToDisk(text.readBytes(), file)
                        }
                        sender.sendMessage("汇报成功")
                        Sender(PluginMain.group, null, null).sendMessage(
                            """
                            汇报播报:
                            汇报代号: ${id}
                            汇报内容:
                        """.trimIndent()
                        )
                        Sender(PluginMain.group, null, null).sendImage(text.toExternalForm(), from)
                    }
                }
            }

            "列表" -> {
                if (command.size <= 2) {
                    this.getInfo(sender, from)
                    return
                }
                val id = command[2]
                if (!PluginMain.group!!.contains(sender.member?.id ?: 0L)) {
                    sender.sendMessage("权限不足", from)
                    return
                }
                val type = ReportConfig.taskList[id] ?: return kotlin.run {
                    sender.sendMessage("请重新确认汇报代号")
                }
                when (type) {
                    "文字" -> {
                        val list = ReportConfig.taskData.getOrPut(id) { mutableMapOf() }.keys
                        val groups = PluginMain.publicGroup ?: return
                        val strings = mutableListOf<String>()
                        groups.members.forEachIndexed { _, member ->
                            if (!member.nameCard.contains("220102301")) {
                                return@forEachIndexed
                            }
                            val mid = member.nameCard.split("220102301")[1]
                            if (list.contains(member.id.toString())) {
                                strings.add("${mid}:${member.nameCard} 已完成 √")
                                return@forEachIndexed
                            }
                            strings.add("${mid}:${member.nameCard} 未完成 ×")
                        }

                        sender.sendMessage(strings.sortedBy { it }.joinToString { "${it}\n" }.replace(", ", ""), from)
                    }

                    "图片" -> {
                        val strings = mutableListOf<String>()
                        PluginMain.publicGroup!!.members.forEach { member ->
                            if (!member.nameCard.contains("220102301")) {
                                return@forEach
                            }
                            val mid = member.nameCard.split("220102301")[1]
                            val file =
                                File("${PluginMain.dataFolderPath}\\图片汇报\\${id}\\${member.nameCard}\\${member.nameCard}.png")
                            if (file.exists()) {
                                strings.add("${mid}:${member.nameCard} 已完成 √")
                                return@forEach
                            }
                            strings.add("${mid}:${member.nameCard} 未完成 ×")
                        }
                        sender.sendMessage(
                            strings.sortedBy { it }.joinToString { "${it}\n" }.replace(", ", ""),
                            from
                        )
                    }

                }
            }

            "导出" -> {
                if (command.size <= 2) {
                    this.getInfo(sender, from)
                    return
                }
                if (!PluginMain.group!!.contains(sender.member?.id ?: 0L)) {
                    sender.sendMessage("权限不足", from)
                    return
                }
                val id = command[2]
                val type = ReportConfig.taskList[id] ?: return kotlin.run {
                    sender.sendMessage("请重新确认汇报代号")
                }
                when (type) {
                    "文字" -> {
                        sender.sendMessage("http://cc.csor.cn:1150/d/chencat/${id}.xls")
                        Report.put(id)
                    }

                    "图片" -> {
                        sender.sendMessage("http://cc.csor.cn:1150/d/chencat/${id}.zip")
                        val fos1 = withContext(Dispatchers.IO) {
                            FileOutputStream(File("${PluginMain.dataFolderPath}\\${id}.zip"))
                        }
                        Report.toZip("${PluginMain.dataFolderPath}\\图片汇报\\${id}", fos1, true)
                    }

                }
            }

            else -> {
                this.getInfo(sender, from)
            }
        }

    }

    fun writeImageToDisk(data: ByteArray, fileName: String) {
        try {
            val file = File(fileName) // 本地目录
            val fileParent: File = file.parentFile
            if (!fileParent.exists()) {
                fileParent.mkdirs()
                file.createNewFile()
            }
            val fops = FileOutputStream(file)
            fops.write(data)
            fops.flush()
            fops.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun convert(source: String, formatName: String, result: String) {
        try {
            val f = File(source)
            f.canRead()
            val src = ImageIO.read(f)
            ImageIO.write(src, formatName, File(result))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}