package pl.redny.listener.prefix

import pl.redny.config.mapping.DiscordConfigMapping
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class ConfigurationPrefixSupplier(private val discordConfigMapping: DiscordConfigMapping) : PrefixSupplier {
    override fun getPrefix(): String {
        return discordConfigMapping.prefix()
    }
}
