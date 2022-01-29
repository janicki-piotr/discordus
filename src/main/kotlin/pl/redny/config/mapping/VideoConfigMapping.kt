package pl.redny.config.mapping

import io.smallrye.config.ConfigMapping


@ConfigMapping(prefix = "app.video")
interface VideoConfigMapping {
    fun cache(): Cache

    interface Cache {
        fun path(): String
        fun ttl(): Long
        fun cron(): String
    }
}
