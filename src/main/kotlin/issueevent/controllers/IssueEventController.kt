package issueevent.controllers

import issueevent.entities.IssueEvent
import io.javalin.Context
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import issueevent.services.IssueEventService

object IssueEventController : KoinComponent {

    private val issueEventService by inject<IssueEventService>()

    fun payload(ctx: Context) {
        val issueEvent = ctx.bodyAsClass(IssueEvent::class.java)
        when (issueEventService.save(issueEvent)) {
            true -> ctx.json("payload loaded")
            else -> ctx.json("payload not loaded")
        }
    }

    fun getEvents(ctx: Context) {
        val issueId = ctx.pathParam("id").toInt()
        val events = issueEventService.getEvents(issueId)
        ctx.json(events)
    }

    fun getStatistics(ctx: Context) {
        val statistics = issueEventService.getStatistics()
        ctx.json(statistics)
    }
}
