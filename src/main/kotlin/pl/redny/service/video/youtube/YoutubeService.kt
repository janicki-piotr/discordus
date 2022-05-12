package pl.redny.service.video.youtube

import com.github.kiulian.downloader.model.videos.VideoInfo
import com.github.kiulian.downloader.model.videos.formats.Format
import com.github.kiulian.downloader.model.videos.formats.VideoWithAudioFormat
import pl.redny.config.mapping.YoutubeConfigMapping
import pl.redny.service.video.VideoDownloader
import pl.redny.service.video.VideoUploader
import java.io.File
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class YoutubeService(val youtubeConfigMapping: YoutubeConfigMapping, val youtubeClient: YoutubeClient) :
    VideoDownloader, VideoUploader {
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

        return youtubeClient.downloadVideo(
            getFormat(videoInfo.getOrNull()), youtubeConfigMapping.download().overwrite(), destination
        )
    }

    private fun getFormat(videoInfo: VideoInfo?): Format? {
        return videoInfo?.formats()?.filterIsInstance<VideoWithAudioFormat>()
            ?.sortedBy { it.qualityLabel() == youtubeConfigMapping.download().quality() }?.last()
    }

    override fun upload(file: File, parameters: Map<String, String>): Result<File> {
        TODO("Not yet implemented")
    }
}
