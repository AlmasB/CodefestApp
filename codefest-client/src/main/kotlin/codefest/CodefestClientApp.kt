package codefest

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * Codefest client application entry point.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CodefestClientApp : Application() {

    override fun start(stage: Stage) {
        stage.minWidth = 800.0
        stage.minHeight = 600.0

        stage.title = "Codefest Client"

        stage.scene = Scene(FXMLLoader.load(javaClass.getResource("main.fxml")))
        stage.show()
    }
}

fun main() {
    Application.launch(CodefestClientApp::class.java)
}