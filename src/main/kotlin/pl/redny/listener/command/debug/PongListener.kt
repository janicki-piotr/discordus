package pl.redny.listener.command.debug

import club.minnced.jda.reactor.on
import kotlinx.coroutines.reactor.mono
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import pl.redny.listener.EventListener
import pl.redny.listener.command.Command
import pl.redny.listener.prefix.PrefixSupplier


class PongListener(prefixSupplier: PrefixSupplier, private val jda: JDA) : EventListener, Command(prefixSupplier) {
    override fun register() {
        jda.on<MessageReceivedEvent>().filter { !it.author.isBot }
            .filter { it.message.contentRaw == (getPrefix() + getName()) }.flatMap(::handleCommand).subscribe()
    }

    private fun handleCommand(it: MessageReceivedEvent) = mono {
        val time = System.currentTimeMillis()
        it.channel.sendMessage("Pong!").queue { response: Message ->
            response.editMessageFormat(
                "Pong: %d ms", System.currentTimeMillis() - time
            ).queue()
        }
    }

    override fun getUsage(): String {
        return getPrefix() + getName()
    }

    override fun getDescription(): String {
        return "Returns answer time in ms."
    }

    override fun getName(): String {
        return "ping"
    }

    override fun getRequiredPermissions(): List<Permission> {
        return emptyList()
    }
}
