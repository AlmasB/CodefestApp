package codefest.client

import com.almasb.sslogger.Logger
import javafx.animation.FadeTransition
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.util.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class LoginController {

    private val log = Logger.get(javaClass)

    @FromContext
    private lateinit var context: Context

    @FXML
    private lateinit var boxServerMessages: VBox
    @FXML
    private lateinit var labelStatus: Label
    @FXML
    private lateinit var fieldFirstName: TextField
    @FXML
    private lateinit var fieldLastName: TextField
    @FXML
    private lateinit var fieldPassword: PasswordField
    @FXML
    private lateinit var btnLogin: Button
    @FXML
    private lateinit var btnRegister: Button

    private val isServerAlive = SimpleBooleanProperty(false)

    private val serverPingScheduler = Executors.newSingleThreadScheduledExecutor {
        Thread(it, "Server Ping Thread").apply { isDaemon = true }
    }

    fun initialize() {
        boxServerMessages.children.clear()

        serverPingScheduler.scheduleAtFixedRate(this::sendPing, 0L, 5L, TimeUnit.SECONDS)

        labelStatus.textProperty().bind(
                Bindings.`when`(isServerAlive).then("Server status: Alive").otherwise("Server status: Down")
        )

        val shouldDisableButton = fieldFirstName.textProperty().isEmpty
                .or(fieldLastName.textProperty().isEmpty)
                .or(fieldPassword.textProperty().isEmpty)
                .or(isServerAlive.not())

        btnLogin.disableProperty().bind(shouldDisableButton)
        btnRegister.disableProperty().bind(shouldDisableButton)
    }

    private fun sendPing() {
        if (isServerAlive.value)
            return

        log.debug("Pinging server")

        Server.requestPing {
            onSuccess = {
                log.debug("Got pong from server")
                isServerAlive.value = true
            }
        }
    }

    fun onLogin() {
        val firstName = fieldFirstName.text
        val lastName = fieldLastName.text
        val pass = fieldPassword.text

        Server.requestLogin(firstName, lastName, pass) {
            onSuccess = {
                if (it < 0){
                    pushMessage("Unable to log in. Incorrect name or password.")
                } else{
                    context.showMain()
                }

                println("Got id: $it")
            }

            onFailure = {
            }
        }
    }

    fun onRegister() {
        val firstName = fieldFirstName.text
        val lastName = fieldLastName.text
        val pass = fieldPassword.text

        Server.requestRegister(firstName, lastName, pass) {
            onSuccess = {

                if (it < 0){
                    pushMessage("Failed to register. User already exists.")
                } else {
                    pushMessage("Registration successful!")
                }

                println("Got id: $it")
            }
        }
    }

    private fun pushMessage(message: String) {
        if (boxServerMessages.children.size == 4) {
            boxServerMessages.children.removeAt(0)
        }

        val text = Text(message)
        text.opacity = 0.0
        text.font = Font.font(14.0)

        val ft = FadeTransition(Duration.seconds(1.0), text)
        ft.toValue = 1.0
        ft.play()

        val bg = Rectangle(text.layoutBounds.width + 20, text.layoutBounds.height + 20, null)
        bg.arcWidth = 15.0
        bg.arcHeight = 15.0
        bg.stroke = Color.DARKBLUE

        val pane = StackPane(bg, text)

        boxServerMessages.children += pane
    }
}