package codefest.client

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.util.Callback

class ViewPool(private val context: Context) {

    private val cache = HashMap<String, Parent>()

    fun obtain(viewName: String) : Parent {
        if (viewName !in cache) {
            val loader = FXMLLoader()
            loader.controllerFactory = Callback { type ->
                val controller = type.getDeclaredConstructor().newInstance()

                type.declaredFields.filter { it.getDeclaredAnnotation(FromContext::class.java) != null }
                        .forEach {
                            it.isAccessible = true
                            it.set(controller, context)
                        }

                controller
            }

            loader.location = javaClass.getResource(viewName)
            cache[viewName] = loader.load()
        }

        return cache[viewName]!!
    }
}