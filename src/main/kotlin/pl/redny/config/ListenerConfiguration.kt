package pl.redny.config

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.audio.AudioReceiveHandler
import net.dv8tion.jda.api.audio.AudioSendHandler
import pl.redny.listener.command.Command
import pl.redny.listener.command.debug.PongListener
import pl.redny.listener.command.util.HelpListener
import pl.redny.listener.command.voice.JoinVoiceListener
import pl.redny.listener.command.voice.LeaveVoiceListener
import pl.redny.listener.command.voice.StartRecordingListener
import pl.redny.listener.command.voice.StopRecordingListener
import pl.redny.listener.prefix.PrefixSupplier
import pl.redny.service.audio.Recorder
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
    fun startRecordingListener(
        jda: JDA, prefixSupplier: PrefixSupplier, recorder: Recorder, audioReceiveHandler: AudioReceiveHandler
    ): StartRecordingListener = StartRecordingListener(prefixSupplier, jda, recorder, audioReceiveHandler)

    @Produces
    fun stopRecordingListener(jda: JDA, prefixSupplier: PrefixSupplier, recorder: Recorder): StopRecordingListener =
        StopRecordingListener(prefixSupplier, jda, recorder)

    @Produces
    fun pongListener(jda: JDA, prefixSupplier: PrefixSupplier): PongListener = PongListener(prefixSupplier, jda)

    @Produces
    fun helpListener(jda: JDA, prefixSupplier: PrefixSupplier, commands: Instance<Command>): HelpListener =
        HelpListener(prefixSupplier, jda, commands)
}
