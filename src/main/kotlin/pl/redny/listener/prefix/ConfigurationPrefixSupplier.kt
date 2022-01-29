package pl.redny.listener.prefix

import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ConfigurationPrefixSupplier(
    @ConfigProperty(
        name = "app.discord.prefix", defaultValue = "!"
    ) val configPrefix: String
) : PrefixSupplier {
    override fun getPrefix(): String {
        return configPrefix
    }
}
