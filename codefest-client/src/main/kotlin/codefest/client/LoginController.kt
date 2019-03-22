package codefest.client

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
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
    @FXML
    private lateinit var btnRegister: Button

    private val isServerAlive = SimpleBooleanProperty(false)

    private var isWaitingForResponse = SimpleBooleanProperty(false);

    fun initialize() {
        Server.requestPing {
            onSuccess = {
                isServerAlive.value = true
            }
        }

        labelStatus.textProperty().bind(
                Bindings.`when`(isServerAlive).then("Server status: Alive").otherwise("Server status: Down")
        )

        val shouldDisableButton = fieldFirstName.textProperty().isEmpty
                .or(fieldLastName.textProperty().isEmpty)
                .or(fieldPassword.textProperty().isEmpty)
                .or(isServerAlive.not())
                .or(isWaitingForResponse)

        btnLogin.disableProperty().bind(shouldDisableButton)
        btnRegister.disableProperty().bind(shouldDisableButton)
    }

    fun onLogin() {
        isWaitingForResponse.value = true

        val firstName = fieldFirstName.text
        val lastName = fieldLastName.text
        val pass = fieldPassword.text

        Server.requestLogin(firstName, lastName, pass) {
            onSuccess = {
                if(it < 0){
                    println("Unable to log in. Incorrect name or password.")
                }
                else{
                    Views.showMain()
                }

                isWaitingForResponse.value = false
                println("Got id: $it")
            }

            onFailure = {
                isWaitingForResponse.value = false
            }
        }
    }

    fun onRegister() {
        isWaitingForResponse.value = true

        val firstName = fieldFirstName.text
        val lastName = fieldLastName.text
        val pass = fieldPassword.text

        Server.requestRegister(firstName, lastName, pass) {
            onSuccess = {

                if(it < 0){
                    println("Failed to register user: $it. User already exists.")
                }
                else{
                    println("Registered user: $it")
                }

                isWaitingForResponse.value = false
                println("Got id: $it")
            }

            onFailure = {
                isWaitingForResponse.value = false
            }
        }
    }
}