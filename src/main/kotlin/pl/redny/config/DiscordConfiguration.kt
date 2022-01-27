package pl.redny.config

import club.minnced.jda.reactor.createManager
import io.quarkus.runtime.Startup
import io.quarkus.runtime.StartupEvent
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import org.eclipse.microprofile.config.inject.ConfigProperty
import pl.redny.listener.EventListener
import javax.enterprise.context.Dependent
import javax.enterprise.event.Observes
import javax.enterprise.inject.Instance
import javax.enterprise.inject.Produces
import javax.inject.Inject
import javax.inject.Singleton


@Dependent
class DiscordConfiguration {
    @ConfigProperty(name = "app.discord.token")
    lateinit var token: String

    @Startup
    @Produces
    @Singleton
    fun jdaConfiguration(): JDA =
        JDABuilder.createDefault(token)
            .setActivity(Activity.playing("with your mum"))
            .setEventManager(createManager())
            .build()

    @Inject
    lateinit var eventHandlers: Instance<EventListener>

    fun onStart(@Observes ev: StartupEvent?) {
        eventHandlers.forEach { it.register() }
    }
}
