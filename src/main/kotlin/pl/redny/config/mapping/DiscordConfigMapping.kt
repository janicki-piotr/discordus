package pl.redny.config.mapping

import io.smallrye.config.ConfigMapping


@ConfigMapping(prefix = "app.discord", namingStrategy = ConfigMapping.NamingStrategy.KEBAB_CASE)
interface DiscordConfigMapping {
    fun token(): String
    fun prefix(): String
    fun maxAttachmentSize(): Long
}
