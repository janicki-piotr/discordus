package pl.redny.config.mapping

import io.smallrye.config.ConfigMapping


@ConfigMapping(prefix = "app.youtube")
interface YoutubeConfigMapping {
    fun download(): Download

    interface Download {
        fun quality(): String
        fun overwrite(): Boolean
    }
}
