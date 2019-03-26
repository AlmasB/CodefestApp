package codefest.client

import javafx.beans.property.*
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


    val isWaitingForResponse = SimpleBooleanProperty(false)

    /**
     * Current runtimeID of this client.
     * Set to -1 if not connected to server.
     */
    val clientID = SimpleLongProperty(-1)

    private val isLoggedInWrapper = ReadOnlyBooleanWrapper().apply { bind(clientID.greaterThan(-1)) }

    val isLoggedIn: ReadOnlyBooleanProperty
        get() = isLoggedInWrapper.readOnlyProperty

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