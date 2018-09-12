import io.javalin.Javalin
import module.issueevent.IssueEventEndpoint
import module.issueevent.IssueEventModule
import org.jetbrains.exposed.sql.Database
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.getProperty
import org.koin.standalone.inject
import java.util.*

class Application : KoinComponent {

    private val issueEventEndpoint by inject<IssueEventEndpoint>()

    fun start() {
        Database.connect(
                url = getProperty("exposed.database.url"),
                driver = getProperty("exposed.database.driver"),
                user = getProperty("exposed.database.username"),
                password = getProperty("exposed.database.password")
        )

        val app = Javalin.create().apply {
            error(404) { ctx -> ctx.json("not found") }
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            port(getProperty("javalin.application.port"))
        }.start()

        app.routes {
            issueEventEndpoint.addEndpoints()
        }
    }
}

object Boot {

    private fun getExtraProperties() = when (System.getenv("ENV")) {
        "hmg", "prd" -> mapOf(
                "exposed.database.url" to System.getenv("EXPOSED_DATABASE_URL"),
                "exposed.database.driver" to System.getenv("EXPOSED_DATABASE_DRIVER"),
                "exposed.database.username" to System.getenv("EXPOSED_DATABASE_USERNAME"),
                "exposed.database.password" to System.getenv("EXPOSED_DATABASE_PASSWORD"),
                "javalin.application.port" to System.getenv("JAVALIN_APPLICATION_PORT")
        )
        else -> Properties().apply {
            load(Application::class.java.getResource("application.conf").openStream())
        }.entries.associate {
            it.key.toString() to it.value.toString()
        }
    }

    @JvmStatic
    fun main(args : Array<String>) {
        startKoin(list = listOf(IssueEventModule), extraProperties = getExtraProperties())
        Application().start()
    }
}
