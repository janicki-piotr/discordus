package pl.redny.service.audio

import net.dv8tion.jda.api.audio.AudioReceiveHandler
import net.dv8tion.jda.api.audio.CombinedAudio
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap
import javax.enterprise.context.ApplicationScoped
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem


//@ApplicationScoped
class SafeCombinedRecordingService : AudioReceiveHandler, Recorder {


    private val receivedBytes: MutableList<ByteArray> = ArrayList()

    private var isRecording: Boolean = false

    private var outputFormat = AudioFormat(48000.0f, 16, 2, true, true)

    override fun canReceiveCombined(): Boolean {
        return true
    }

    override fun handleCombinedAudio(combinedAudio: CombinedAudio) {
        if (!isRecording || combinedAudio.users.isEmpty()) return
        val data = combinedAudio.getAudioData(1.0) // volume at 100% = 1.0 (50% = 0.5 / 55% = 0.55)

        FileOutputStream("/videos/Output.wav.tmp", true).use { output -> output.write(data); }
    }

    override fun start(): Result<Unit> {
        isRecording = true

        return Result.success(Unit)
    }

    override fun stop(): Result<Unit> {
        isRecording = false

        try {
            val decodedData = Files.readAllBytes(Paths.get("/videos/Output.wav.tmp"))
            getWavFile(File("/videos/Output.wav"), decodedData)
            Files.delete(Paths.get("/videos/Output.wav.tmp"))
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
                ), outputFormat, decodedData.size.toLong()
            ), AudioFileFormat.Type.WAVE, outFile
        )
    }

}
