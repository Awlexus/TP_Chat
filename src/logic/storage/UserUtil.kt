package logic.storage

import java.io.File

/**
 * Created by Awlex on 20.12.2017.
 */
object UserUtil {

    var username: String
        get() {
            var ret: String? = null
            UserUtil::class.java.getResource("Username.txt").openStream().use {
                ret = it.bufferedReader().readLine()
            }
            return ret ?: "No name"
        }

        set(newName) {
            File(UserUtil::class.java.getResource("Username.txt").file).printWriter().use {
                it.write(newName)
            }
        }
}
