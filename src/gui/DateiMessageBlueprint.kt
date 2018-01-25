package gui

import sun.audio.AudioData
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

/**
 * @author Matteo Cosi
 * @since 24.01.2018
 */
data class DateiMessageBlueprint(val type: Chat.chatMessageType, val name: String, val dateiname: String?, val bytes: ByteArray?, val date: String?, val color: Color) {

    val fileEnding: String?
        get() {
            val index = dateiname?.lastIndexOf('.') ?: -1
            return if (index != -1)
                dateiname?.substring(index)!!.toLowerCase()//TODO ending safer & . ??
            else null
        }

    val image: BufferedImage?
        get() {
            return if (fileEnding == "png" || fileEnding == "jpg" || fileEnding == "jpeg" || fileEnding == "gif")
                ImageIO.read(ByteArrayInputStream(bytes))
            else
                null
        }

    val audio: AudioData?
        get() {
            return if (fileEnding == "mp3" || fileEnding == "wav"|| fileEnding == "wav") {
                AudioData(bytes)
            } else
                null
            // Play the sound
            //AudioDataStream audioStream = new AudioDataStream(audioData);
            //AudioPlayer.player.start(audioStream);
        }

}
