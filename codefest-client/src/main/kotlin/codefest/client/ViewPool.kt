package codefest.client

import javafx.fxml.FXMLLoader
import javafx.scene.Parent

class ViewPool {

    private val cache = HashMap<String, Parent>()

    fun obtain(viewName: String) : Parent {
        if (viewName !in cache) {
            cache[viewName] = FXMLLoader.load(javaClass.getResource(viewName))
        }

        return cache[viewName]!!
    }
}