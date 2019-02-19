package codefest.client

import javafx.fxml.FXML
import javafx.scene.control.Label

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class SubmitController {

    @FXML
    private lateinit var labelChallenge: Label

    fun initialize() {
        Server.requestChallenges {
            onSuccess = {
                labelChallenge.text = it.text
            }
        }
    }
}