package pl.redny.service.video

import com.github.kiulian.downloader.YoutubeDownloader
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo
import com.github.kiulian.downloader.downloader.response.Response
import com.github.kiulian.downloader.downloader.response.ResponseStatus
import com.github.kiulian.downloader.model.videos.VideoInfo
import com.github.kiulian.downloader.model.videos.formats.Format
import java.io.File
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class YoutubeClient(val youtubeDownloader: YoutubeDownloader) {
    fun getVideoInfo(videoId: String): Result<VideoInfo> {
        val responseVideoInfo: Response<VideoInfo> = youtubeDownloader.getVideoInfo(RequestVideoInfo(videoId))

        return when (responseVideoInfo.status()) {
            ResponseStatus.error -> Result.failure(responseVideoInfo.error())
            ResponseStatus.completed -> Result.success(responseVideoInfo.data())
            ResponseStatus.downloading, ResponseStatus.canceled, null -> Result.failure(YoutubeException("Unsupported status, sync call"))
        }
    }

    fun downloadVideo(format: Format?, overwriteIfExists: Boolean, directoryPath: String): Result<File> {
        val responseDownload: Response<File> =
            youtubeDownloader.downloadVideoFile(getRequestVideoFileDownload(format, overwriteIfExists, directoryPath))

        return when (responseDownload.status()) {
            ResponseStatus.error -> Result.failure(responseDownload.error())
            ResponseStatus.completed -> Result.success(responseDownload.data())
            ResponseStatus.downloading, ResponseStatus.canceled, null -> Result.failure(YoutubeException("Unsupported status, sync call"))
        }
    }

    private fun getRequestVideoFileDownload(format: Format?, overwriteIfExists: Boolean, directoryPath: String) =
        RequestVideoFileDownload(format).overwriteIfExists(overwriteIfExists).saveTo(File(directoryPath))
}
