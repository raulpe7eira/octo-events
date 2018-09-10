package issueevent

import org.koin.dsl.module.module
import issueevent.repositories.IssueEventRepository
import issueevent.repositories.IssueEventRepositoryImpl
import issueevent.services.IssueEventService
import issueevent.services.IssueEventServiceImpl

val IssueEventAppModule = module {
    single<IssueEventService> { IssueEventServiceImpl(get()) }
    single<IssueEventRepository> { IssueEventRepositoryImpl() }
}
