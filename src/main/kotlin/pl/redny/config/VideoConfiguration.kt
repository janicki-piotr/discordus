package pl.redny.config

import com.github.kiulian.downloader.Config.Builder
import com.github.kiulian.downloader.YoutubeDownloader
import ws.schild.jave.Encoder
import javax.enterprise.context.Dependent
import javax.enterprise.inject.Produces


@Dependent
class VideoConfiguration {
    @Produces
    fun youtubeDownloader(): YoutubeDownloader = YoutubeDownloader(
        Builder().build()
    )

    @Produces
    fun videoEncoder(): Encoder = Encoder()
}