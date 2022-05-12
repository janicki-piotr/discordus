package pl.redny.listener.moderator.video

import club.minnced.jda.reactor.on
import kotlinx.coroutines.reactor.mono
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import pl.redny.config.mapping.DiscordConfigMapping
import pl.redny.config.mapping.VideoConfigMapping
import pl.redny.listener.DiscordException
import pl.redny.listener.EventListener
import pl.redny.service.video.VideoEncoder
import pl.redny.service.video.youtube.YoutubeService
import java.io.File
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class YoutubeListener(
    private val discordConfigMapping: DiscordConfigMapping,
    private val videoConfigMapping: VideoConfigMapping,
    private val jda: JDA,
    private val youtubeService: YoutubeService,
    private val videoEncoder: VideoEncoder
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
        return if (message.isEmpty()) {
            Result.failure(DiscordException("Message cannot be empty."))
        } else if (file.length() > discordConfigMapping.maxAttachmentSize()) {
            Result.failure(DiscordException("Attachment exceeded max size "))
        } else Result.runCatching { channel.sendMessage(message).addFile(file).queue() }
    }

    private fun handleDownloading(url: String): Result<File> =
        youtubeService.download(url, videoConfigMapping.cache().path())

    private fun handleEncoding(file: File): Result<File> = videoEncoder.encode(file)

    override fun getExceptions(): List<Permission> {
        return emptyList()
    }
}
