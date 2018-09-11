import io.javalin.Javalin
import issueevent.IssueEventAppModule
import issueevent.IssueEventEndpoint
import org.jetbrains.exposed.sql.Database
import org.koin.standalone.StandAloneContext.startKoin

fun main(args: Array<String>) {

    Database.connect(
            url ="jdbc:postgresql://localhost:5432/octoevents",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "postgres"
    )

    val app = Javalin.create().apply {
        error(404) { ctx -> ctx.json("not found") }
        exception(Exception::class.java) { e, _ -> e.printStackTrace() }
        port(4567)
    }.start()

    app.routes {
        IssueEventEndpoint().addEndpoints()
    }

    startKoin(listOf(
            IssueEventAppModule
    ))
}
