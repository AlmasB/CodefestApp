package codefest.client

import com.almasb.sslogger.ConsoleOutput
import com.almasb.sslogger.Logger
import com.almasb.sslogger.LoggerConfig
import com.almasb.sslogger.LoggerLevel
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage

/**
 * Codefest client application entry point.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CodefestClientApp : Application() {

    private var exitRequested = false

    private val context = Context()

    override fun start(stage: Stage) {
        stage.minWidth = 800.0
        stage.minHeight = 600.0

        stage.width = 800.0
        stage.height = 600.0

        stage.title = "Codefest Client"
        stage.onCloseRequest = EventHandler {
            it.consume()

            onExit()
        }

        stage.scene = Scene(context.root)

        context.showLogin()

        stage.show()
    }

    override fun stop() {
        Logger.close()
    }

    private fun onExit() {
        if (exitRequested)
            return

        if (!context.isLoggedIn.value) {
            Platform.exit()
            return
        }

        Server.requestLogout {
            onSuccess = {
                Platform.exit()
            }

            onFailure = {
                Logger.get("ClientApp").warning("Error during log out", it)
                Platform.exit()
            }
        }
    }
}

fun main() {
    Logger.configure(LoggerConfig())
    Logger.addOutput(ConsoleOutput(), LoggerLevel.DEBUG)

    Application.launch(CodefestClientApp::class.java)
}