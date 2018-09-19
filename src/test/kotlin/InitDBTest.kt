import org.flywaydb.core.Flyway
import java.util.*

abstract class InitDBTest {

    init {
        Flyway().apply {
            configure(Properties().apply {
                load(javaClass.getResourceAsStream("/application.conf"))
            })
            migrate()
        }
    }
}
