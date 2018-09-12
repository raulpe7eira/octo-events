package module.issueevent

import module.issueevent.handlers.IssueEventHandler
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.apibuilder.EndpointGroup

class IssueEventEndpoint : EndpointGroup {

    override fun addEndpoints() {
        post("/payload", IssueEventHandler::payload)

        path("/issues") {
            get("/:id/events", IssueEventHandler::getEvents)
            get("/statistics", IssueEventHandler::getStatistics)
        }
    }
}
