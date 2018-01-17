package logic.storage

import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Awlex on 11.01.2018.
 */
abstract class SerializableList<T : IndexableData> : ArrayList<T>() {

    val path = "${Paths.get("").toAbsolutePath()}${File.separatorChar}savefiles${File.separatorChar}${javaClass.simpleName}.ser"
    val indexer = AtomicInteger(1)

    @Synchronized
    fun save() {
        DataIOs.print(path, ArrayList(this))
    }

    @Synchronized
    fun read() {
        clear()

        val file = File(path)

        try {
            val savedIndexes = (DataIOs.read(path) as ArrayList<T>)
            savedIndexes.forEach {
                add(it)
            }
        } catch (e: FileNotFoundException) {
            println("Fresh start. Creating \"$path\"")
            file.createNewFile()
        }
    }

    override fun add(element: T): Boolean {
        if (element.id == -1)
            element.id = indexer.getAndIncrement()

        return super.add(element)
    }

    fun getById(id: Int) = find { it.id == id }

}