package tests

import protocol.Protocol
import java.util.*

/**
 * Created by Awlex on 01.12.2017.
 */

fun main(args: Array<String>) {
    val auto = Protocol(userName = "Tom")

    auto.hello()

    Scanner(System.`in`).nextLine()
    auto.stop()
}