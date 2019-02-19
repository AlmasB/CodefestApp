package codefest.client

import codefest.common.Config.Companion.IP
import codefest.common.Config.Companion.PORT
import codefest.common.Config.Companion.PATH_PING
import codefest.common.Config.Companion.PATH_LOGIN
import codefest.common.data.Leaderboard
import java.lang.RuntimeException

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * An abstraction of the Codefest Server platform.
 * Allows making requests to the actual server.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Server {
    private val client by lazy { HttpClient.newHttpClient() }

    fun requestLogin(firstName: String, lastName: String, password: String, action: IOResponse<Long>.() -> Unit) {
        val response = IOResponse<Long>()
        action(response)

        val fn = { s: String ->
            val converted = (s.toLong())

            response.onSuccess?.invoke(converted) ?: Unit
        }

        request("$IP:$PORT$PATH_LOGIN?first=$firstName&last=$lastName&pass=$password", fn, response.onFailure)
    }

    fun requestPing(action: IOResponse<String>.() -> Unit) {
        val response = IOResponse<String>()
        action(response)

        val fn = { s: String ->
            val converted = (s)

            response.onSuccess?.invoke(converted) ?: Unit
        }

        request("$IP:$PORT$PATH_PING", fn, response.onFailure)
    }

    private fun request(reqURI: String, onSuccess: (String) -> Unit, onFail: (Throwable) -> Unit) {
        val request = HttpRequest.newBuilder()
                .uri(URI.create("http://$reqURI"))
                .build()

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply { it.body() }
                .whenCompleteAsync { result, error ->
                    if (result != null) {
                        onSuccess(result)
                    } else if (error != null) {
                        onFail(error)
                    } else {
                        onFail(RuntimeException("Both result and error were null!"))
                    }
                }
    }
}



