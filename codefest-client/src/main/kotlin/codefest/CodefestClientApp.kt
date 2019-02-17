package codefest

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import javafx.stage.Stage

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CodefestClientApp : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(StackPane(Text("hello!")))
        stage.show()
    }
}

fun main() {
    Application.launch(CodefestClientApp::class.java)
}