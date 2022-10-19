package ray.mintcat.chencat.module.report

import jxl.Workbook
import jxl.write.Label
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import ray.mintcat.chencat.PluginMain
import ray.mintcat.chencat.module.groupphoto.GroupPhotoEvent
import ray.mintcat.chencat.module.task.TaskData
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object Report {

    suspend fun put(id: String) {
        coroutineScope {
            withContext(Dispatchers.IO) {
                val wb = Workbook.createWorkbook(File(PluginMain.dataFolder.absolutePath, "${id}.xls"))
                val sheet = wb.createSheet("data", 0)
                val groups = PluginMain.publicGroup ?: return@withContext
                sheet.addCell(Label(0, 0, "序号"))
                sheet.addCell(Label(1, 0, "名称"))
                sheet.addCell(Label(2, 0, "学号"))
                sheet.addCell(Label(3, 0, "内容"))
                var i = 0
                groups.members.forEachIndexed { index, member ->
                    if (!member.nameCard.contains("220102301")) {
                        return@forEachIndexed
                    }
                    val mid = member.nameCard.split("220102301")[1]
                    sheet.addCell(Label(0, i + 1, index.toString()))
                    sheet.addCell(Label(1, i + 1, member.nameCard.split("220102301")[0]))
                    sheet.addCell(Label(2, i + 1, "220102301$mid"))
                    sheet.addCell(
                        Label(
                            3,
                            i + 1,
                            ReportConfig.taskData.getOrPut(id) { mutableMapOf() }[member.id.toString()] ?: "无"
                        )
                    )
                    i++
                }
                wb.write();
                wb.close();
            }
        }
    }


    fun toZip(srcDir: String, out: OutputStream, KeepDirStructure: Boolean) {
        val start = System.currentTimeMillis()
        var zos: ZipOutputStream? = null
        try {
            zos = ZipOutputStream(out)
            val sourceFile = File(srcDir)
            compress(sourceFile, zos, sourceFile.name, KeepDirStructure)
            val end = System.currentTimeMillis()
            println("压缩完成，耗时：" + (end - start) + " ms")
        } catch (e: java.lang.Exception) {
            throw RuntimeException("zip error from ZipUtils", e)
        } finally {
            if (zos != null) {
                try {
                    zos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private const val BUFFER_SIZE = 2 * 1024

    @Throws(java.lang.Exception::class)
    private fun compress(
        sourceFile: File, zos: ZipOutputStream, name: String,
        KeepDirStructure: Boolean
    ) {
        val buf = ByteArray(BUFFER_SIZE)
        if (sourceFile.isFile) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(ZipEntry(name))
            // copy文件到zip输出流中
            var len: Int
            val `in` = FileInputStream(sourceFile)
            while (`in`.read(buf).also { len = it } != -1) {
                zos.write(buf, 0, len)
            }
            // Complete the entry
            zos.closeEntry()
            `in`.close()
        } else {
            val listFiles = sourceFile.listFiles()
            if (listFiles == null || listFiles.size == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(ZipEntry("$name/"))
                    // 没有文件，不需要文件的copy
                    zos.closeEntry()
                }
            } else {
                for (file in listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.name, KeepDirStructure)
                    } else {
                        compress(file, zos, file.name, KeepDirStructure)
                    }
                }
            }
        }
    }

    fun test() {
        PluginMain.eventChannel.subscribeAlways<GroupPhotoEvent> {
            val name = this.appData.meta.albumData.albumName
            if (ReportConfig.taskList.getOrDefault(name,"ERROR") != "群相册") {
                return@subscribeAlways
            }
            val data = ReportConfig.taskData.getOrPut(name) { mutableMapOf() }
            data[this.event.sender.id.toString()] = "完成"
            PluginMain.group!!.sendMessage(
                """
                汇报提交: $name
                提交者: ${this.event.sender.nameCard}
            """.trimIndent()
            )
        }
    }


}