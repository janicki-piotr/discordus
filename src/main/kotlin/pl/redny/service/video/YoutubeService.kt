package pl.redny.service.video

import com.github.kiulian.downloader.model.videos.VideoInfo
import com.github.kiulian.downloader.model.videos.formats.Format
import com.github.kiulian.downloader.model.videos.formats.VideoWithAudioFormat
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.io.File
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class YoutubeService(
    @ConfigProperty(name = "app.youtube.download.quality", defaultValue = "360p") val quality: String,
    @ConfigProperty(name = "app.youtube.download.over", defaultValue = "true") val overwrite: Boolean,
    val youtubeClient: YoutubeClient
) : VideoDownloader {
    private final val youtubeUrlPattern =
        "^(?:https?:)?\\/\\/?(?:www|m)\\.?(?:youtube\\.com|youtu.be)\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?([\\w\\-]+)(\\S+)?\$"

    private final val youtubeUrlRegex = youtubeUrlPattern.toRegex()

    override fun download(source: String, destination: String): Result<File> {
        val videoId = youtubeUrlRegex.matchEntire(source)?.groupValues?.get(1)
        if (videoId.isNullOrBlank()) {
            return Result.failure(YoutubeException("Cannot parse videoId for url $source"))
        }

        val videoInfo: Result<VideoInfo> = youtubeClient.getVideoInfo(videoId)
        if (videoInfo.isFailure) {
            return Result.failure(YoutubeException(videoInfo.exceptionOrNull()!!))
        }

        return youtubeClient.downloadVideo(getFormat(videoInfo.getOrNull()), overwrite, destination)
    }

    private fun getFormat(videoInfo: VideoInfo?): Format? {
        return videoInfo?.formats()?.filterIsInstance<VideoWithAudioFormat>()?.sortedBy { it.qualityLabel() == quality }
            ?.last()
    }
}
