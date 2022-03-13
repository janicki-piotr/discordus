package pl.redny.service.audio

import net.dv8tion.jda.api.audio.AudioReceiveHandler
import net.dv8tion.jda.api.audio.CombinedAudio
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import javax.enterprise.context.ApplicationScoped
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem


@ApplicationScoped
class InMemoryCombinedRecordingService : AudioReceiveHandler, Recorder {

    private val receivedBytes: MutableList<ByteArray> = ArrayList()

    private var isRecording: Boolean = false

    var OUTPUT_FORMAT = AudioFormat(48000.0f, 16, 2, true, true)

    override fun canReceiveCombined(): Boolean {
        return true
    }

    override fun handleCombinedAudio(combinedAudio: CombinedAudio) {
        if (!isRecording || combinedAudio.users.isEmpty()) return
        val data = combinedAudio.getAudioData(1.0) // volume at 100% = 1.0 (50% = 0.5 / 55% = 0.55)
        receivedBytes.add(data)
    }

    override fun start(): Result<Unit> {
        isRecording = true

        return Result.success(Unit)
    }

    override fun stop(): Result<Unit> {
        isRecording = false

        try {
            var size = 0
            for (bs in receivedBytes) {
                size += bs.size
            }
            val decodedData = ByteArray(size)
            var i = 0
            for (bs in receivedBytes) {
                for (j in bs.indices) {
                    decodedData[i++] = bs[j]
                }
            }
            getWavFile(File("/videos/Output.wav"), decodedData)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }

        receivedBytes.clear()
        return Result.success(Unit)
    }

    private fun getWavFile(outFile: File, decodedData: ByteArray) {
        AudioSystem.write(
            AudioInputStream(
                ByteArrayInputStream(
                    decodedData
                ), OUTPUT_FORMAT, decodedData.size.toLong()
            ), AudioFileFormat.Type.WAVE, outFile
        )
    }

}
