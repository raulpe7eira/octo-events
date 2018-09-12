package integration

import Application
import io.javalin.Javalin
import junit.framework.TestCase
import org.junit.Test

class IntegrationTest : TestCase() {

    private lateinit var app: Javalin

    private val url = "http://localhost:4567"

    override fun setUp() {
        app = Application().start()
    }

    override fun tearDown() {
        app.stop()
    }

    @Test
    fun `assert endpoint not found`() {
        val response = khttp.get(url = url + "/endpoint/not/found")
        assertEquals(404, response.statusCode)
    }

    @Test
    fun `assert payload endpoint found`() {
        val response = khttp.post(url = url + "/payload")
        assertEquals(200, response.statusCode)
    }

    @Test
    fun `assert issues_events endpoint found`() {
        val response = khttp.get(url = url + "/issues/0/events")
        assertEquals(200, response.statusCode)
    }

    @Test
    fun `assert issues_statistics endpoint found`() {
        val response = khttp.get(url = url + "/issues/statistics")
        assertEquals(200, response.statusCode)
    }
}
