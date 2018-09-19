package module.issueevent.services

import InitDBTest
import module.issueevent.IssueEventModule
import module.issueevent.messages.Event
import module.issueevent.messages.Issue
import module.issueevent.messages.IssueEvent
import module.issueevent.messages.Statistics
import module.issueevent.repositories.IssueEventRepositoryImpl
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.get
import org.koin.standalone.getProperty
import org.koin.test.KoinTest
import org.koin.test.declareMock
import java.util.*

class IssueEventServiceTest : InitDBTest(), KoinTest {

    private lateinit var payloadDefault: IssueEvent

    @BeforeEach
    fun setUp() {
        startKoin(listOf(IssueEventModule), extraProperties = Properties().apply {
            load(javaClass.getResourceAsStream("/application.conf"))
        }.entries.associate {
            it.key.toString() to it.value.toString()
        })

        Database.connect(
                url = getProperty("exposed.database.url"),
                driver = getProperty("exposed.database.driver"),
                user = getProperty("exposed.database.username"),
                password = getProperty("exposed.database.password")
        )

        declareMock<IssueEventRepositoryImpl>()

        payloadDefault = IssueEvent(
                "opened", Issue(
                1234567890,
                "2018-09-18T00:00:00",
                "2018-09-18T00:00:00"
        ))
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Asserts load of payload successfully`() {
        val issueEventService = get<IssueEventService>()
        assertEquals(true, issueEventService.save(payloadDefault))
    }

    @Test
    fun `Asserts retrieval of events successfully`() {
        val issueEventService = get<IssueEventService>()
        issueEventService.save(payloadDefault)

        val expected = listOf(
                Event("opened", "2018-09-18T00:00:00.000-03:00", "2018-09-18T00:00:00.000-03:00")
        )
        val result = issueEventService.getEvents(1234567890)
        assertEquals(expected, result)
    }

    @Test
    fun `Asserts retrieval of statistics successfully`() {
        val issueEventService = get<IssueEventService>()
        issueEventService.save(payloadDefault)

        val expected = Statistics(1, 0)
        val result = issueEventService.getStatistics()
        assertEquals(expected, result)
    }
}
