package module.issueevent.services

import module.issueevent.messages.Event
import module.issueevent.messages.IssueEvent
import module.issueevent.messages.Statistics
import module.issueevent.repositories.IssueEventRepository

interface IssueEventService {

    fun save(issueEvent: IssueEvent): Boolean
    fun getEvents(issueId: Int): List<Event>
    fun getStatistics(): Statistics
}

class IssueEventServiceImpl(
        private val issueEventRepository: IssueEventRepository
) : IssueEventService {

    override fun save(issueEvent: IssueEvent) = issueEventRepository.save(issueEvent)

    override fun getEvents(issueId: Int) = issueEventRepository.getEvents(issueId)

    override fun getStatistics() = issueEventRepository.getStatistics()
}
