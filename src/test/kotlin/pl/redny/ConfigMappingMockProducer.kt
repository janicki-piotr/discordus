package pl.redny

import io.quarkus.test.Mock
import io.smallrye.config.SmallRyeConfig
import org.eclipse.microprofile.config.Config
import pl.redny.config.mapping.DiscordConfigMapping
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Produces
import javax.inject.Inject

class ConfigMappingMockProducer {
    @Inject
    lateinit var config: Config

    @Produces
    @ApplicationScoped
    @Mock
    fun discordConfigMapping(): DiscordConfigMapping {
        return config.unwrap(SmallRyeConfig::class.java).getConfigMapping(DiscordConfigMapping::class.java)
    }
}
