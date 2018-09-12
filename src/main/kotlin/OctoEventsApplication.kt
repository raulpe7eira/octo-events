import io.javalin.Javalin
import module.issueevent.IssueEventEndpoint
import module.issueevent.IssueEventModule
import org.jetbrains.exposed.sql.Database
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.getProperty
import org.koin.standalone.inject
import java.util.*

class OctoEventsApplication : KoinComponent {

    private val issueEventEndpoint by inject<IssueEventEndpoint>()

    fun start() {
        Database.connect(
                url = getProperty("DATABASE_URL"),
                driver = getProperty("DATABASE_DRIVER"),
                user = getProperty("DATABASE_USERNAME"),
                password = getProperty("DATABASE_PASSWORD")
        )

        val app = Javalin.create().apply {
            error(404) { ctx -> ctx.json("not found") }
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            port(getProperty("APPLICATION_PORT"))
        }.start()

        app.routes {
            issueEventEndpoint.addEndpoints()
        }
    }
}

private fun getExtraProperties() = when (System.getenv("ENV")) {
    "hmg", "prd" -> mapOf(
            "DATABASE_URL" to System.getenv("DATABASE_URL"),
            "DATABASE_DRIVER" to System.getenv("DATABASE_DRIVER"),
            "DATABASE_USERNAME" to System.getenv("DATABASE_USERNAME"),
            "DATABASE_PASSWORD" to System.getenv("DATABASE_PASSWORD"),
            "APPLICATION_PORT" to System.getenv("APPLICATION_PORT")
    )
    else -> Properties().apply {
        load(OctoEventsApplication::class.java.getResource("/config/application.properties").openStream())
    }.entries.associate {
        it.key.toString() to it.value.toString()
    }
}

fun main(args: Array<String>) {

    startKoin(list = listOf(IssueEventModule), extraProperties = getExtraProperties())
    OctoEventsApplication().start()
}
