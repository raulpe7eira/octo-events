package issueevent

import issueevent.controllers.IssueEventController
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.apibuilder.EndpointGroup

class IssueEventEndpoint : EndpointGroup {

    override fun addEndpoints() {
        post("/payload", IssueEventController::payload)

        path("/issues") {
            get("/:id/events", IssueEventController::getEvents)
            get("/statistics", IssueEventController::getStatistics)
        }
    }
}
