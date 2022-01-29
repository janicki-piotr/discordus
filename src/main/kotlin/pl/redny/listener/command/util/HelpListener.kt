package pl.redny.listener.command.util

import club.minnced.jda.reactor.on
import kotlinx.coroutines.reactor.mono
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import pl.redny.listener.EventListener
import pl.redny.listener.command.Command
import pl.redny.listener.prefix.PrefixSupplier
import javax.enterprise.inject.Instance

class HelpListener(private val jda: JDA, private val commands: Instance<Command>, prefixSupplier: PrefixSupplier) :
    EventListener, Command(prefixSupplier) {

    private val helpPattern = "!help (.+)"

    private val helpPatternRegex = helpPattern.toRegex()

    override fun register() {
        jda.on<MessageReceivedEvent>().filter { !it.author.isBot }
            .filter { it.message.contentRaw.startsWith((getPrefix() + getName())) }.flatMap(::handleCommand).subscribe()
    }

    private fun handleCommand(it: MessageReceivedEvent) = mono {
        val group = helpPatternRegex.matchEntire(it.message.contentRaw)?.groupValues?.get(0)

        val message: String = if (group.isNullOrBlank()) prepareMessages() else prepareMessage(group)

        it.channel.sendMessage(message).queue()
    }

    private fun prepareMessage(name: String): String {
        val command = commands.firstOrNull { it.getName() == name } ?: return "Command $name not found"

        return command.getUsage() + " - " + command.getDescription()
    }

    private fun prepareMessages(): String {
        return commands.joinToString("\n") { it.getUsage() + " - " + it.getDescription() }
    }

    override fun getUsage(): String {
        return getPrefix() + getName() + " [optional command_name]"
    }

    override fun getDescription(): String {
        return "Prints info about commands, or one specific command if command name present."
    }

    override fun getName(): String {
        return "help"
    }

    override fun getRequiredPermissions(): List<Permission> {
        return emptyList()
    }
}
