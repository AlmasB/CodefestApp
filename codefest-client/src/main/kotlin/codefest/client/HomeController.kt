package codefest.client

import javafx.fxml.FXML
import javafx.scene.control.Button

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class HomeController {

    @FromContext
    private lateinit var context: Context

    fun onLogOut() {
        Server.requestLogout {
            onSuccess = {
                context.isLoggedIn.value = false

                context.showLogin()
            }
        }
    }
}