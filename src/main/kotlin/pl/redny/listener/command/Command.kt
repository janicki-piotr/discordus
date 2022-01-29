package pl.redny.listener.command

import net.dv8tion.jda.api.Permission
import pl.redny.listener.prefix.PrefixSupplier


abstract class Command(private val prefixSupplier: PrefixSupplier) {
    fun getPrefix(): String {
        return prefixSupplier.getPrefix()
    }
    abstract fun getUsage(): String
    abstract fun getDescription(): String
    abstract fun getName(): String
    abstract fun getRequiredPermissions(): List<Permission>
}
