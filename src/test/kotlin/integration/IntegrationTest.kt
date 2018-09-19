package integration

import Application
import InitDBTest
import io.javalin.Javalin
import org.junit.FixMethodOrder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class IntegrationTest : InitDBTest() {

    private lateinit var app: Javalin
    private val baseUrl = "http://localhost:4567"

    @BeforeEach
    fun setUp() {
        app = Application().start()
    }

    @AfterEach
    fun tearDown() {
        app.stop()
    }

    @Test
    fun `Asserts endpoint not found`() {
        val response = khttp.get(
                url = "$baseUrl/endpoint/not/found"
        )
        assertEquals(404, response.statusCode)
    }

    @Test
    fun `Asserts 'payload' endpoint found with success`() {
        val payload = javaClass.getResourceAsStream("/payloads/issues_events--valid.json")!!
        val response = khttp.post(
                url = "$baseUrl/payload",
                data = payload
        )
        assertEquals(201, response.statusCode)
        assertEquals("payload loaded", response.jsonObject["message"])
    }

    @Test
    fun `Asserts 'payload' endpoint found with error`() {
        val payload = javaClass.getResourceAsStream("/payloads/issues_events--invalid.json")!!
        val response = khttp.post(
                url = "$baseUrl/payload",
                data = payload
        )
        assertEquals(406, response.statusCode)
        assertEquals("payload not loaded", response.jsonObject["message"])
    }

    @Test
    fun `Asserts 'issues_id_events' endpoint found with success`() {
        val response = khttp.get(
                url = "$baseUrl/issues/1234567890/events"
        )
        assertEquals(200, response.statusCode)
        assertEquals(1, response.jsonArray.length())
    }

    @Test
    fun `Asserts 'issues_id_events' endpoint found with error`() {
        val response = khttp.get(
                url = "$baseUrl/issues/0/events"
        )
        assertEquals(200, response.statusCode)
        assertEquals(0, response.jsonArray.length())
    }

    @Test
    fun `Asserts 'issues_statistics' endpoint found`() {
        val response = khttp.get(
                url = "$baseUrl/issues/statistics"
        )
        assertEquals(200, response.statusCode)
    }
}
