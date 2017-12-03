package tests

import protocol.Protocol

/**
 * Created by Awlex on 03.12.2017.
 */
fun main(args: Array<String>) {
    val prot = Protocol(userName = "Awlex")
    prot.createGroup()

    Thread.sleep(1000)
}