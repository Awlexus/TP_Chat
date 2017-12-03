package tests

import protocol.Protocol

/**
 * Created by Awlex on 01.12.2017.
 */

fun main(args: Array<String>) {
    val auto = Protocol(userName = "Awlex")
    auto.hello()

    Thread.sleep(1000)
    auto.stop()
}