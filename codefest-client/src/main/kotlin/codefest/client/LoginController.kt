package codefest.client

import com.almasb.sslogger.Logger
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class LoginController {

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

    private val isServerAlive = SimpleBooleanProperty(false)

    fun initialize() {
        Server.requestPing {
            onSuccess = {
                isServerAlive.value = true
            }
        }

        labelStatus.textProperty().bind(
                Bindings.`when`(isServerAlive).then("Server status: Alive").otherwise("Server status: Down")
        )

        btnLogin.disableProperty().bind(
                fieldFirstName.textProperty().isEmpty
                        .or(fieldLastName.textProperty().isEmpty)
                        .or(fieldPassword.textProperty().isEmpty)
                        .or(isServerAlive.not())
        )
    }

    fun onLogin() {
        val firstName = fieldFirstName.text
        val lastName = fieldLastName.text
        val pass = fieldPassword.text

        Server.requestLogin(firstName, lastName, pass) {
            onSuccess = {
                val scene = btnLogin.scene
                scene.root = FXMLLoader.load(javaClass.getResource("main.fxml"))

                println("Got id: $it")
            }
        }
    }
}