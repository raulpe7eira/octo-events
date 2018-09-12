package module.issueevent.repositories

import module.issueevent.messages.Event
import module.issueevent.messages.IssueEvent
import module.issueevent.messages.Statistics
import module.issueevent.models.IssueEntity
import module.issueevent.models.IssueEventEntity
import module.issueevent.models.IssueEventTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

interface IssueEventRepository {
    fun save(issueEvent: IssueEvent): Boolean
    fun getEvents(issueId: Int): List<Event>
    fun getStatistics(): Statistics
}

class IssueEventRepositoryImpl : IssueEventRepository {

    override fun save(issueEvent: IssueEvent) = transaction {
        val issue = IssueEntity.findById(issueEvent.issue.id)?.let {
            it.apply {
                createdAt = DateTime(issueEvent.issue.createdAt)
                updatedAt = DateTime(issueEvent.issue.updatedAt)
                flush()
            }
        } ?: IssueEntity.new(issueEvent.issue.id) {
            this.createdAt = DateTime(issueEvent.issue.createdAt)
            this.updatedAt = DateTime(issueEvent.issue.updatedAt)
        }

        IssueEventEntity.new {
            this.action = issueEvent.action
            this.issue = issue
            this.createdAt = DateTime.now()
        }.flush()
    }

    override fun getEvents(issueId: Int) = transaction {
        IssueEventEntity.find { IssueEventTable.issueId eq issueId }.map {
            Event(it.action, it.issue.createdAt.toString(), it.issue.updatedAt.toString())
        }
    }

    override fun getStatistics() = transaction {
        val actions = exec("""
            |SELECT ie.action
            |FROM issues_event ie
            |GROUP BY ie.action, ie.issue_id
            |HAVING MAX(ie.created_at) = (
            |    SELECT MAX(mxie.created_at)
            |    FROM issues_event mxie
            |    WHERE mxie.issue_id = ie.issue_id
            |)
        """.trimMargin().trim()) { resultSet ->
            mutableListOf<String>().apply {
                while (resultSet.next()) {
                    this += resultSet.getString("action")
                }
            }
        }.orEmpty()

        actions.fold(Statistics(0, 0)) { statistics, action ->
            when(action) {
                "opened", "reopened" -> statistics.apply { open++ }
                "closed" -> statistics.apply { closed++ }
                else -> statistics
            }
        }
    }
}
