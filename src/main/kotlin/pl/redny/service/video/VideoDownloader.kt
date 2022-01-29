package pl.redny.service.video

import java.io.File


interface VideoDownloader {
    fun download(source: String, destination: String) : Result<File>
}
