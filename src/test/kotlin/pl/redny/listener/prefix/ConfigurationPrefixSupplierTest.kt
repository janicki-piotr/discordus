package pl.redny.listener.prefix

import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import pl.redny.config.mapping.DiscordConfigMapping
import javax.inject.Inject


@QuarkusTest
class ConfigurationPrefixSupplierTest {

    @Inject
    lateinit var configurationPrefixSupplier: ConfigurationPrefixSupplier

    @InjectMock
    lateinit var discordConfigMapping: DiscordConfigMapping

    @Test
    fun `should respond ! prefix`() {
        // given
        val prefix = "!"
        every { discordConfigMapping.prefix() } returns prefix

        // when
        val result = configurationPrefixSupplier.getPrefix()

        // then
        assertEquals(prefix, result)
    }
}
