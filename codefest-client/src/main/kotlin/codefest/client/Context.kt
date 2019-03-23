package codefest.client

import javafx.beans.property.SimpleBooleanProperty

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Context {

    val isLoggedIn = SimpleBooleanProperty(false)
    val isWaitingForResponse = SimpleBooleanProperty(false)

}