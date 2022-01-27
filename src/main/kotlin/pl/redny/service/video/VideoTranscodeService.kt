package pl.redny.service.video

import ws.schild.jave.Encoder
import ws.schild.jave.MultimediaObject
import ws.schild.jave.encode.AudioAttributes
import ws.schild.jave.encode.EncodingAttributes
import ws.schild.jave.encode.VideoAttributes
import java.io.File
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class VideoTranscodeService(private val encoder: Encoder) : VideoEncoder {
    override fun encode(file: File): Result<File> {
        return Result.runCatching { encodeDiscordVideo(file) }
    }

    private fun encodeDiscordVideo(source: File): File {
        val output = File(source.parent, source.nameWithoutExtension + "_dc" + ".webm")

//        val audio = AudioAttributes()
//            .setCodec("libvorbis")
//            .setBitRate(64000)
//            .setSamplingRate(44100)
//            .setChannels(2)

        val video = VideoAttributes()
            .setCodec("libvpx")
            .setFrameRate(20)
//            .setBitRate(16000)
//            .setFrameRate(24)

        val attrs = EncodingAttributes()
//            .setAudioAttributes(audio)
            .setVideoAttributes(video)
            .setOutputFormat("webm")

        encoder.encode(MultimediaObject(source), output, attrs, null)

        return output
    }
}