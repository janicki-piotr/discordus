package pl.redny.listener.command.voice

import club.minnced.jda.reactor.on
import kotlinx.coroutines.reactor.mono
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import pl.redny.listener.EventListener
import pl.redny.listener.command.Command
import pl.redny.listener.prefix.PrefixSupplier

class JoinVoiceListener(private val jda: JDA, prefixSupplier: PrefixSupplier) : EventListener, Command(prefixSupplier) {
    override fun register() {
        jda.on<MessageReceivedEvent>().filter { !it.author.isBot }
            .filter { it.message.contentRaw == (getPrefix() + getName()) }.flatMap(::handleCommand).subscribe()
    }

    private fun handleCommand(it: MessageReceivedEvent) = mono {
        val connectedVoice = it.member?.voiceState?.channel
        if (connectedVoice == null) {
            it.channel.sendMessage("You are not on the voice channel!").queue()
        } else if (!it.guild.selfMember.hasPermission(connectedVoice, Permission.VOICE_CONNECT)) {
            it.channel.sendMessage("I do not have permissions to join a voice channel!").queue()
        } else {
            val audioManager = it.guild.audioManager
            if (audioManager.isConnected) {
                it.channel.sendMessage("The bot is already connected!").queue()
            } else {
                audioManager.openAudioConnection(connectedVoice)
                it.channel.sendMessage("Connected to the voice channel!").queue()
            }
        }
    }

    override fun getUsage(): String {
        return getPrefix() + getName()
    }

    override fun getDescription(): String {
        return "Joins bot to your voice channel."
    }

    override fun getName(): String {
        return "join"
    }

    override fun getRequiredPermissions(): List<Permission> {
        return emptyList()
    }
}
