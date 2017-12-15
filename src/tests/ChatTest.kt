package tests

import protocol.Protocol

/**
 * Created by Awlex on 15.12.2017.
 */
fun main(args: Array<String>) {
    val prot = Protocol("Matteo")
    prot.hello()
    do {
        val line = readLine()
        if (line == "exit")
            continue
        if (line != null)
            prot.message(line, prot.broadcastAddress)
    } while (line != "exit")

    prot.stop()
}