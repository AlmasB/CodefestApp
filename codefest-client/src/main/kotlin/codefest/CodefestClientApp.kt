package codefest

import codefest.data.Student
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import javafx.stage.Stage

import java.net.http.HttpResponse.BodyHandlers
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpClient.newHttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class CodefestClientApp : Application() {

//    override fun init() {
//        val client = HttpClient.newHttpClient()
//        val request = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:55555/top"))
//                .build()
//
//        client.sendAsync(request, BodyHandlers.ofString())
//                .thenApply { it.body() }
//                .thenApply { jacksonObjectMapper().readValue(it, Student::class.java) }
//                .thenAccept { println(it.firstName) }
//                .join()
//    }

    override fun start(stage: Stage) {
        stage.minWidth = 800.0
        stage.minHeight = 600.0

        stage.title = "Codefest Client"

        stage.scene = Scene(FXMLLoader.load(javaClass.getResource("main.fxml")))
        stage.show()
    }
}

fun main() {
    Application.launch(CodefestClientApp::class.java)
}