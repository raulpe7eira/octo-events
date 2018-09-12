package module.issueevent

import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.checkModules

class IssueEventModuleTest : KoinTest {

    @Test
    fun `check issue event module hierarchy`() {
        checkModules(listOf(IssueEventModule))
    }
}
