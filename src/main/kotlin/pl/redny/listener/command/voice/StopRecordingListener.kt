package pl.redny.listener.command.voice

import club.minnced.jda.reactor.on
import kotlinx.coroutines.reactor.mono
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import pl.redny.listener.EventListener
import pl.redny.listener.command.Command
import pl.redny.listener.prefix.PrefixSupplier
import pl.redny.service.audio.Recorder

class StopRecordingListener (prefixSupplier: PrefixSupplier, private val jda: JDA, private val recorder: Recorder) : EventListener, Command(prefixSupplier) {
    override fun register() {
        jda.on<MessageReceivedEvent>().filter { !it.author.isBot }
            .filter { it.message.contentRaw == (getPrefix() + getName()) }.flatMap(::handleCommand).subscribe()
    }

    private fun handleCommand(it: MessageReceivedEvent) = mono {
        recorder.stop()
    }

    override fun getUsage(): String {
        TODO("Not yet implemented")
    }

    override fun getDescription(): String {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        return "stop"
    }

    override fun getRequiredPermissions(): List<Permission> {
        TODO("Not yet implemented")
    }
}
