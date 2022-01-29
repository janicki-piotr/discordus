package pl.redny.service.video

import java.io.File

interface VideoEncoder {
    fun encode(file: File): Result<File>
}
