package codefest.client

import javafx.beans.property.BooleanProperty
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane

object Views {

    private val root = StackPane()

    val scene = Scene(root)

    private val viewPool = ViewPool()

    private val allEventsConsumer = EventHandler<Event> { it.consume() }

    init {
        // dummy node
        root.children += Pane()
        root.children += viewPool.obtain("masker.fxml")
    }

    fun showLogin() {
        setView("login.fxml")
    }

    fun showMain() {
        setView("main.fxml")
    }

    fun showMaskerWhen(condition: BooleanProperty) {
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
