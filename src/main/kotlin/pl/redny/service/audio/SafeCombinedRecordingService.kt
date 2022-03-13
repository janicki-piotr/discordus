package pl.redny.service.audio

import net.dv8tion.jda.api.audio.AudioReceiveHandler
import net.dv8tion.jda.api.audio.CombinedAudio
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import javax.enterprise.context.ApplicationScoped
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem


//@ApplicationScoped
class SafeCombinedRecordingService : AudioReceiveHandler, Recorder {

    private var isRecording: Boolean = false

    private val fileStreams : MutableMap<String, FileOutputStream> = ConcurrentHashMap()

    var OUTPUT_FORMAT = AudioFormat(48000.0f, 16, 2, true, true)

    override fun canReceiveCombined(): Boolean {
        return true
    }

    override fun handleCombinedAudio(combinedAudio: CombinedAudio) {
        if (!isRecording || combinedAudio.users.isEmpty()) return
        val data = combinedAudio.getAudioData(1.0) // volume at 100% = 1.0 (50% = 0.5 / 55% = 0.55)
        fileStreams[""]?.write(data)


        try {
            getWavFile(File("/videos/Output.wav"), data)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }

    }

    override fun start(): Result<Unit> {
        val string = "/videos/Output.wav"
        val file: File =  File(string)
        if(!fileStreams.containsKey(string)) {
            fileStreams[string] = FileOutputStream(file)
        }
        isRecording = true
        return Result.success(Unit)
    }

    override fun stop(): Result<Unit> {
        isRecording = false

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
