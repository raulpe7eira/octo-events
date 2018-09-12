package integration

import Application
import io.javalin.Javalin
import junit.framework.TestCase

class TestIntegration : TestCase() {

    private lateinit var app: Javalin

    private val url = "http://localhost:4567"

    override fun setUp() {
        app = Application().start()
    }

    override fun tearDown() {
        app.stop()
    }

    fun `test endpoint not found`() {
        val response = khttp.get(url = url + "/endpoint/not/found")
        assertEquals(404, response.statusCode)
    }

    fun `test payload endpoint exist`() {
        val response = khttp.post(url = url + "/payload")
        assertEquals(200, response.statusCode)
    }

    fun `test issues_events endpoint exist`() {
        val response = khttp.get(url = url + "/issues/0/events")
        assertEquals(200, response.statusCode)
    }

    fun `test issues_statistics endpoint exist`() {
        val response = khttp.get(url = url + "/issues/statistics")
        assertEquals(200, response.statusCode)
    }
}
