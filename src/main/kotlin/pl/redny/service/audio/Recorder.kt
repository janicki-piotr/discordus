package pl.redny.service.audio

interface Recorder {

    fun start() : Result<Unit>

    fun stop() : Result<Unit>
}
