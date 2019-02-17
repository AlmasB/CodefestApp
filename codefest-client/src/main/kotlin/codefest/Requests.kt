package codefest

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */

private val client by lazy { HttpClient.newHttpClient() }

fun login(firstName: String, lastName: String, password: String, consumer: (Long) -> Unit) {
    val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:55555/login?first=$firstName&last=$lastName&pass=$password"))
            .build()

    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply { it.body() }
            //.thenApply { jacksonObjectMapper().readValue(it, Leaderboard::class.java) }
            //.thenAccept { updateListView(it) }
            .thenAccept { consumer(it.toLong()) }
}