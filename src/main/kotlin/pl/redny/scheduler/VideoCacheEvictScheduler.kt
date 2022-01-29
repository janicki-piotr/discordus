package pl.redny.scheduler

import io.quarkus.scheduler.Scheduled
import io.quarkus.scheduler.ScheduledExecution
import org.eclipse.microprofile.config.inject.ConfigProperty
import pl.redny.common.date.DateTime
import java.io.File
import java.time.ZoneId
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class VideoCacheEvictScheduler(
    @ConfigProperty(name = "app.video.cache.path", defaultValue = "/video") val path: String,
    @ConfigProperty(name = "app.video.cache.ttl", defaultValue = "1800") val ttl: Long,
    private val dateTime: DateTime
) {

    @Scheduled(cron = "{app.video.cache.cron}")
    fun cronJob(execution: ScheduledExecution) {
        File(path).listFiles()?.filterNot { it.isDirectory }?.filter {
            (dateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - it.lastModified() >= ttl * 1000)
        }?.forEach { it.delete() }
    }
}
