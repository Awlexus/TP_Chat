package tests

import logic.storage.UserUtil
import protocol.Protocol
import java.util.*

/**
 * Created by Awlex on 01.12.2017.
 */

fun main(args: Array<String>) {
    val auto = Protocol(UserUtil.username)

    auto.hello()

    Scanner(System.`in`).nextLine()
    auto.stop()
}