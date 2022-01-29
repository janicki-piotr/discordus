package pl.redny.listener.moderator.video

import net.dv8tion.jda.api.Permission

interface Moderator {
    fun getExceptions(): List<Permission>
}
