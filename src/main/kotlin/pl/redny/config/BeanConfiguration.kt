package pl.redny.config

import pl.redny.common.date.DateTime
import pl.redny.common.date.LocalDateTimeService
import javax.enterprise.context.Dependent
import javax.enterprise.inject.Produces


@Dependent
class BeanConfiguration {
    @Produces
    fun dateTimeService(): DateTime = LocalDateTimeService()
}
