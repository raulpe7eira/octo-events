package module.issueevent

import org.koin.dsl.module.module
import module.issueevent.repositories.IssueEventRepository
import module.issueevent.repositories.IssueEventRepositoryImpl
import module.issueevent.services.IssueEventService
import module.issueevent.services.IssueEventServiceImpl

val IssueEventModule = module("module.issue-event") {
    single { IssueEventEndpoint() }
    single<IssueEventService> { IssueEventServiceImpl(get()) }
    single<IssueEventRepository> { IssueEventRepositoryImpl() }
}
