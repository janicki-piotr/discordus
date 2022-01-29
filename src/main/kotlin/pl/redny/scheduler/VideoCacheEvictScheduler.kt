package pl.redny.scheduler

import io.quarkus.scheduler.Scheduled
import io.quarkus.scheduler.ScheduledExecution
import pl.redny.common.date.DateTime
import pl.redny.config.mapping.VideoConfigMapping
import java.io.File
import java.time.ZoneId
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class VideoCacheEvictScheduler(private val videoConfigMapping: VideoConfigMapping, private val dateTime: DateTime) {
    @Scheduled(cron = "{app.video.cache.cron}")
    fun cronJob(execution: ScheduledExecution) {
        File(videoConfigMapping.cache().path()).listFiles()?.filterNot { it.isDirectory }?.filter {
            (dateTime.now().atZone(ZoneId.systemDefault()).toInstant()
                .toEpochMilli() - it.lastModified() >= videoConfigMapping.cache().ttl() * 1000)
        }?.forEach { it.delete() }
    }
}
