package pl.redny.listener.command.voice

import club.minnced.jda.reactor.on
import kotlinx.coroutines.reactor.mono
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import pl.redny.listener.EventListener
import pl.redny.listener.command.Command
import pl.redny.listener.prefix.PrefixSupplier


class LeaveVoiceListener(prefixSupplier: PrefixSupplier, private val jda: JDA) : EventListener,
    Command(prefixSupplier) {
    override fun register() {
        jda.on<MessageReceivedEvent>().filter { !it.author.isBot }
            .filter { it.message.contentRaw == (getPrefix() + getName()) }.flatMap(::handleCommand).subscribe()
    }

    private fun handleCommand(it: MessageReceivedEvent) = mono {
        val connectedVoice = it.guild.selfMember.voiceState?.channel
        if (connectedVoice == null) {
            it.channel.sendMessage("I am not connected to a voice channel!").queue()
        } else {
            it.guild.audioManager.closeAudioConnection()
            it.channel.sendMessage("Disconnected from the voice channel!").queue()
        }
    }

    override fun getUsage(): String {
        return getPrefix() + getName()
    }

    override fun getDescription(): String {
        return "Kicks bot from voice channel."
    }

    override fun getName(): String {
        return "join"
    }

    override fun getRequiredPermissions(): List<Permission> {
        return emptyList()
    }
}
