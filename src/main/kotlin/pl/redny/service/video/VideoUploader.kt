package pl.redny.service.video

import java.io.File

interface VideoUploader {
    fun upload(file: File, parameters: Map<String, String>): Result<File>
}
