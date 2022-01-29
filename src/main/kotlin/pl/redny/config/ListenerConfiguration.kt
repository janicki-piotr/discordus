package pl.redny.config

import net.dv8tion.jda.api.JDA
import pl.redny.listener.command.Command
import pl.redny.listener.command.debug.PongListener
import pl.redny.listener.command.util.HelpListener
import pl.redny.listener.command.voice.JoinVoiceListener
import pl.redny.listener.command.voice.LeaveVoiceListener
import pl.redny.listener.prefix.PrefixSupplier
import javax.enterprise.context.Dependent
import javax.enterprise.inject.Instance
import javax.enterprise.inject.Produces


@Dependent
class ListenerConfiguration {
    @Produces
    fun leaveVoiceListener(jda: JDA, prefixSupplier: PrefixSupplier): LeaveVoiceListener =
        LeaveVoiceListener(prefixSupplier, jda)

    @Produces
    fun joinVoiceListener(jda: JDA, prefixSupplier: PrefixSupplier): JoinVoiceListener =
        JoinVoiceListener(prefixSupplier, jda)

    @Produces
    fun pongListener(jda: JDA, prefixSupplier: PrefixSupplier): PongListener = PongListener(prefixSupplier, jda)

    @Produces
    fun helpListener(jda: JDA, prefixSupplier: PrefixSupplier, commands: Instance<Command>): HelpListener =
        HelpListener(prefixSupplier, jda, commands)
}
