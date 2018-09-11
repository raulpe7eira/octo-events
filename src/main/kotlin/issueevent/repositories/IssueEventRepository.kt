package issueevent.repositories

import issueevent.entities.Event
import issueevent.entities.IssueEvent
import issueevent.entities.Statistics
import issueevent.models.IssueDAO
import issueevent.models.IssueEventDAO
import issueevent.models.IssueEventTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

interface IssueEventRepository {

    fun save(issueEvent: IssueEvent): Boolean
    fun getEvents(issueId: Int): List<Event>
    fun getStatistics(): Statistics
}

class IssueEventRepositoryImpl : IssueEventRepository {

    override fun save(issueEvent: IssueEvent) = transaction {
        val issue = IssueDAO.findById(issueEvent.issue.id)?.let {
            it.apply {
                createdAt = DateTime(issueEvent.issue.createdAt)
                updatedAt = DateTime(issueEvent.issue.updatedAt)
                flush()
            }
        } ?: IssueDAO.new(issueEvent.issue.id) {
            this.createdAt = DateTime(issueEvent.issue.createdAt)
            this.updatedAt = DateTime(issueEvent.issue.updatedAt)
        }

        IssueEventDAO.new {
            this.action = issueEvent.action
            this.issue = issue
            this.createdAt = DateTime.now()
        }.flush()
    }

    override fun getEvents(issueId: Int) = transaction {
        IssueEventDAO.find { IssueEventTable.issueId eq issueId }.map {
            Event(it.action, it.issue.createdAt.toString(), it.issue.updatedAt.toString())
        }
    }

    override fun getStatistics() = transaction {
        IssueEventDAO.all().groupBy { it.issue.id }.values.map { issuesEvent ->
            issuesEvent.maxBy {
                it.createdAt
            }
        }.fold(Statistics(0, 0)) { statistics, issueEvent ->
            when(issueEvent?.action) {
                "opened", "reopened" -> statistics.apply { open++ }
                "closed" -> statistics.apply { closed++ }
                else -> statistics
            }
        }
    }
}
