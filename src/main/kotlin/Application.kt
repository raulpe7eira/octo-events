import io.javalin.Javalin
import issueevent.IssueEventAppModule
import issueevent.IssueEventEndpoint
import org.koin.standalone.StandAloneContext.startKoin

fun main(args: Array<String>) {

    val app = Javalin.create().apply {
        error(404) { ctx -> ctx.json("not found") }
        exception(Exception::class.java) { e, _ -> e.printStackTrace() }
        port(4567)
    }.start()

    app.routes {
        IssueEventEndpoint()
    }

    startKoin(listOf(
            IssueEventAppModule
    ))
}
