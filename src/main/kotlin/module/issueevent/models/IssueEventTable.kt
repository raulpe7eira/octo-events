package module.issueevent.models

import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.dao.UUIDTable

object IssueEventTable : UUIDTable("github.issues_event") {
    val action = varchar("action", 50)
    val issueId = reference("issue_id", IssueTable)
    val createdAt = datetime("created_at")
}

object IssueTable : IdTable<Int>("github.issues") {
    override val id = integer("id").primaryKey().entityId()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
