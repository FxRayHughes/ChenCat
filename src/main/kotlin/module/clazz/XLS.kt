package ray.mintcat.chencat.module.clazz

import jxl.Workbook
import ray.mintcat.chencat.PluginMain
import ray.mintcat.chencat.module.clazz.data.ClassData
import ray.mintcat.chencat.module.clazz.data.DayData
import ray.mintcat.chencat.module.clazz.data.TimeData
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object XLS {

    //星期：每日内容
    val data = ConcurrentHashMap<Int, DayData>()

    val xls by lazy {
        File(PluginMain.dataFolder.absolutePath, "data.xls")
    }

    fun load() {
        loadData()
        loadTime()
        loadRoom()
    }

    fun loadData() {
        val map = mutableMapOf<Int, MutableList<String?>>()
        Workbook.getWorkbook(xls).sheets.forEach { sheet ->
            (3..8).forEach { rowID ->
                val row = sheet.getRow(rowID)
                (1..7).forEach {
                    val data = row.getOrNull(it)?.contents
                    val list = map.getOrPut(it) { mutableListOf() }
                    list.add(data)
                }
            }
        }
        map.forEach { t, u ->
            val dayData = data.getOrPut(t) { DayData() }
            u.forEachIndexed { index, s ->
                if (s.isNullOrBlank()) {
                    dayData.list.add(ClassData(index = index, none = true))
                    return@forEachIndexed
                }
                val list = s.split("\n").filter { a -> a.isNotBlank() }
                dayData.list.add(
                    ClassData(
                        list.getOrElse(0) { "获取失败" },
                        list.getOrElse(1) { "获取失败" },
                        list.getOrElse(2) { "获取失败" },
                        list.getOrElse(3) { "获取失败" },
                        index
                    )
                )
            }
        }
    }

    fun getClassData(week: Int, classIndex: Int): ClassData? {
        return data[week]?.list?.get(classIndex - 1)
    }

    val time by lazy {
        File(PluginMain.dataFolder.absolutePath, "time.xls")
    }

    val timeData = ArrayList<TimeData>()

    fun loadTime() {
        Workbook.getWorkbook(time).sheets.forEach { sheet ->
            (1..5).forEach { rowID ->
                val row = sheet.getRow(rowID)
                timeData.add(TimeData(row[0].contents.toInt(), row[1].contents, row[2].contents, row[3].contents))
            }
        }
    }

    val room by lazy {
        File(PluginMain.dataFolder.absolutePath, "room.xls")
    }

    val roomData = ConcurrentHashMap<String, Pair<String, String>>()

    fun loadRoom() {
        Workbook.getWorkbook(room).sheets.forEach { sheet ->
            (1 until sheet.rows).forEach { rowID ->
                val row = sheet.getRow(rowID)
                roomData[row[0].contents] = row[1].contents to row[2].contents
            }
        }
    }


}