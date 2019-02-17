package codefest

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class LoginController {

    @FXML
    private lateinit var fieldFirstName: TextField
    @FXML
    private lateinit var fieldLastName: TextField
    @FXML
    private lateinit var fieldPassword: PasswordField
    @FXML
    private lateinit var btnLogin: Button

    fun initialize() {
        btnLogin.disableProperty().bind(
                fieldFirstName.textProperty().isEmpty
                        .or(fieldLastName.textProperty().isEmpty)
                        .or(fieldPassword.textProperty().isEmpty)
        )
    }

    fun onLogin() {
        val firstName = fieldFirstName.text
        val lastName = fieldLastName.text
        val pass = fieldPassword.text

        login(firstName, lastName, pass) {
            val scene = btnLogin.scene
            scene.root = FXMLLoader.load(javaClass.getResource("main.fxml"))

            println("Got id: $it")
        }
    }
}