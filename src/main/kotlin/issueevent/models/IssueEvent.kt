package issueevent.models

import org.jetbrains.exposed.dao.*
import java.util.*

// Tables

object IssueEventTable : UUIDTable("issues_event") {
    val action = varchar("action", 50)
    val issueId = reference("issue_id", IssueTable)
    val createdAt = datetime("created_at")
}

object IssueTable : IdTable<Int>("issues") {
    override val id = integer("id").primaryKey().entityId()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}

// DAOs

class IssueEventDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<IssueEventDAO>(IssueEventTable)

    var action by IssueEventTable.action
    var issue by IssueDAO referencedOn IssueEventTable.issueId
    var createdAt by IssueEventTable.createdAt
}

class IssueDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<IssueDAO>(IssueTable)

    var createdAt by IssueTable.createdAt
    var updatedAt by IssueTable.updatedAt
}
