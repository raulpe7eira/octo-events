package issueevent.repositories

import issueevent.entities.Event
import issueevent.entities.IssueEvent
import issueevent.entities.Statistics

interface IssueEventRepository {

    fun save(issueEvent: IssueEvent): Boolean
    fun getEvents(issueId: Int): List<Event>
    fun getStatistics(): Statistics
}

class IssueEventRepositoryImpl : IssueEventRepository {
    val dataModel = mutableListOf<IssueEvent>()

    override fun save(issueEvent: IssueEvent) = dataModel.add(issueEvent)

    override fun getEvents(issueId: Int) = dataModel.filter { it.issue.id == issueId }.map {
        Event(it.action, it.issue.createdAt, it.issue.updatedAt)
    }

    override fun getStatistics() = dataModel.groupBy { it.issue.id }.values.map { issuesEvent ->
        issuesEvent.maxBy { it.issue.updatedAt }
    }.fold(Statistics(0, 0)) { statistics, issueEvent ->
        when(issueEvent?.action) {
            "opened", "reopened" -> statistics.apply { open++ }
            "closed" -> statistics.apply { closed++ }
            else -> statistics
        }
    }
}
