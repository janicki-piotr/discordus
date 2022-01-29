package pl.redny.listener.moderator.video

import club.minnced.jda.reactor.on
import kotlinx.coroutines.reactor.mono
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.eclipse.microprofile.config.inject.ConfigProperty
import pl.redny.listener.DiscordException
import pl.redny.listener.EventListener
import pl.redny.service.video.VideoEncoder
import pl.redny.service.video.YoutubeService
import java.io.File
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class YoutubeListener(
    @ConfigProperty(name = "app.video.cache.path", defaultValue = "/videos") val destination: String,
    @ConfigProperty(name = "max-attachment-size", defaultValue = "8388608") val maxAttachmentSize: Long,
    val jda: JDA,
    val youtubeService: YoutubeService,
    val videoEncoder: VideoEncoder
) : EventListener, Moderator {
    override fun register() {
        jda.on<MessageReceivedEvent>().filter { !it.author.isBot }
            .filter { it.message.contentRaw.contains("www.youtube.com") }.flatMap(::handleCommand).subscribe()
    }

    private fun handleCommand(it: MessageReceivedEvent) = mono {
        val channel = it.channel
        handleDownloading(it.message.contentRaw).onSuccess {
            handleEncoding(it).onSuccess { uploadFile("Reupload", it, channel) }
        }.onFailure { channel.sendMessage("Error processing file").queue() }
    }

    private fun uploadFile(message: String, file: File, channel: MessageChannel): Result<Unit> {
        if (message.isEmpty()) {
            return Result.failure(DiscordException("Message cannot be empty."))
        }
        if (file.length() > maxAttachmentSize) {
            return Result.failure(DiscordException("Attachment exceeded max size "))
        }
        return Result.runCatching { channel.sendMessage(message).addFile(file).queue() }
    }

    private fun handleDownloading(url: String): Result<File> = youtubeService.download(url, destination)

    private fun handleEncoding(file: File): Result<File> = videoEncoder.encode(file)

    override fun getExceptions(): List<Permission> {
        return emptyList()
    }
}