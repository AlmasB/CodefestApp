package codefest.client

import codefest.common.Config.Companion.IP
import codefest.common.Config.Companion.PATH_CHALLENGES
import codefest.common.Config.Companion.PATH_LEADERBOARD
import codefest.common.Config.Companion.PORT
import codefest.common.Config.Companion.PATH_PING
import codefest.common.Config.Companion.PATH_LOGIN
import codefest.common.Config.Companion.PATH_LOGOUT
import codefest.common.Config.Companion.PATH_SUBMIT
import codefest.common.data.Challenge
import codefest.common.data.Codefest
import codefest.common.data.Leaderboard
import com.almasb.sslogger.Logger
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javafx.application.Platform
import java.lang.RuntimeException

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * An abstraction of the Codefest Server platform.
 * Allows making requests to the actual server.
 * All request callbacks are executed on the JavaFX thread,
 * while the requests are executed on background threads.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Server {
    private val log = Logger.get("ServerProxy")

    private val client by lazy { HttpClient.newHttpClient() }

    private var token: Long = -1

    fun requestLogin(firstName: String, lastName: String, password: String, action: IOResponse<Long>.() -> Unit) {
        val response = IOResponse<Long>()
        action(response)

        val fn = { s: String ->
            val converted = (s.toLong())

            token = converted

            response.onSuccess?.invoke(converted) ?: Unit
        }

        request("$IP:$PORT$PATH_LOGIN?first=$firstName&last=$lastName&pass=$password", fn, response.onFailure)
    }

    fun requestLogout(action: IOResponse<String>.() -> Unit) {
        val response = IOResponse<String>()
        action(response)

        val fn = { s: String ->
            val converted = (s)

            response.onSuccess?.invoke(converted) ?: Unit
        }

        request("$IP:$PORT$PATH_LOGOUT?id=$token", fn, response.onFailure)
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

    fun requestChallenges(action: IOResponse<Codefest>.() -> Unit) {
        val response = IOResponse<Codefest>()
        action(response)

        val fn = { s: String ->
            val converted = jacksonObjectMapper().readValue(s, Codefest::class.java)

            response.onSuccess?.invoke(converted) ?: Unit
        }

        request("$IP:$PORT$PATH_CHALLENGES", fn, response.onFailure)
    }

    fun requestLeaderboard(action: IOResponse<Leaderboard>.() -> Unit) {
        val response = IOResponse<Leaderboard>()
        action(response)

        val fn = { s: String ->
            val converted = jacksonObjectMapper().readValue(s, Leaderboard::class.java)

            response.onSuccess?.invoke(converted) ?: Unit
        }

        request("$IP:$PORT$PATH_LEADERBOARD", fn, response.onFailure)
    }

    fun requestSubmit(challengeID: Int) {
        val request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create("http://$IP:$PORT$PATH_SUBMIT?id=$token&challengeID=$challengeID"))
                .build()

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply { it.body() }
                .whenCompleteAsync { result, error ->
                    if (result != null) {
                        println(result)
//                        Platform.runLater {
//                            onSuccess(result)
//                        }
                    } else if (error != null) {
//                        Platform.runLater {
//                            onFail(error)
//                        }
                    } else {
//                        Platform.runLater {
//                            onFail(RuntimeException("Both result and error were null!"))
//                        }
                    }
                }
    }

    private fun request(reqURI: String, onSuccess: (String) -> Unit, onFail: (Throwable) -> Unit) {
        val uri = URI.create("http://$reqURI")

        log.debug("Building GET uri: $uri")

        val request = HttpRequest.newBuilder()
                .uri(uri)
                .build()

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply { it.body() }
                .whenCompleteAsync { result, error ->
                    if (result != null) {
                        Platform.runLater {
                            onSuccess(result)
                        }
                    } else if (error != null) {
                        Platform.runLater {
                            onFail(error)
                        }
                    } else {
                        Platform.runLater {
                            onFail(RuntimeException("Both result and error were null!"))
                        }
                    }
                }
    }
}



