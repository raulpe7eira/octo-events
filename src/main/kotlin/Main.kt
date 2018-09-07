import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post

// payload data

@JsonIgnoreProperties(ignoreUnknown = true)
data class IssueEvent(
        @JsonProperty("action") val action: String,
        @JsonProperty("issue") val issue: Issue
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Issue(
        @JsonProperty("id") val id: Int,
        @JsonProperty("created_at") val createdAt: String,
        @JsonProperty("updated_at") val updatedAt: String
)

// response data

data class Event(
        @JsonProperty("action") val action: String,
        @JsonProperty("created_at") val createdAt: String,
        @JsonProperty("updated_at") val updatedAt: String
)

data class Statistics(
        @JsonProperty("open") var open: Int,
        @JsonProperty("closed") var closed: Int
)

// Application

fun main(args: Array<String>) {

    // data model

    val dataModel = mutableListOf<IssueEvent>()

    // Javalin (!)

    val app = Javalin.create().apply {
        error(404) { ctx -> ctx.json("not found") }
        exception(Exception::class.java) { e, _ -> e.printStackTrace() }
        port(4567)
    }.start()

    // routes w/ controller + service

    app.routes {
        post("/payload") { ctx ->
            val issueEvent= ctx.bodyAsClass(IssueEvent::class.java)
            dataModel.add(issueEvent)

            ctx.json("payload loaded")
        }

        get("/issues/:id/events") {  ctx ->
            val issueId = ctx.pathParam("id").toInt()
            val events = dataModel.filter { it.issue.id == issueId }.map {
                Event(it.action, it.issue.createdAt, it.issue.updatedAt)
            }

            ctx.json(events)
        }

        get("/issues/statistics") { ctx ->
            val statistics = dataModel.groupBy { it.issue.id }.values.map { issuesEvent ->
                issuesEvent.maxBy { it.issue.updatedAt }
            }.fold(Statistics(0,0)) { statistics, issueEvent ->
                when(issueEvent?.action) {
                    "opened", "reopened" -> statistics.apply { open++ }
                    "closed" -> statistics.apply { closed++ }
                    else -> statistics
                }
            }

            ctx.json(statistics)
        }
    }
}
