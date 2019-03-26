package codefest.client

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Context {

    val isLoggedIn = SimpleBooleanProperty(false)
    val isWaitingForResponse = SimpleBooleanProperty(false)

    // TODO: rename Server -> client
    val client = Server

    val root = StackPane()

    private val viewPool = ViewPool(this)

    private val allEventsConsumer = EventHandler<Event> { it.consume() }

    init {
        // dummy node
        root.children += Pane()
        root.children += viewPool.obtain("masker.fxml")

        showMaskerWhen(isWaitingForResponse)
    }

    fun showLogin() {
        setView("login.fxml")
    }

    fun showMain() {
        setView("main.fxml")
    }

    private fun showMaskerWhen(condition: BooleanProperty) {
        root.children[1].visibleProperty().bind(condition)
        condition.addListener { _, _, isActive ->
            if (isActive) {
                root.addEventFilter(EventType.ROOT, allEventsConsumer)
            } else {
                root.removeEventFilter(EventType.ROOT, allEventsConsumer)
            }
        }
    }

    private fun setView(viewName: String) {
        root.children[0] = viewPool.obtain(viewName)
    }
}