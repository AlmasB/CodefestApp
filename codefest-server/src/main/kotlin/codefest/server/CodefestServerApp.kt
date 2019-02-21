package codefest.server

import codefest.common.Config
import codefest.common.Config.Companion.PATH_CHALLENGES
import codefest.common.Config.Companion.PATH_LEADERBOARD
import codefest.common.Config.Companion.PATH_LOGIN
import codefest.common.Config.Companion.PATH_LOGOUT
import codefest.common.Config.Companion.PATH_PING
import codefest.common.Config.Companion.PATH_SUBMIT
import codefest.common.data.*
import com.almasb.sslogger.ConsoleOutput
import com.almasb.sslogger.Logger
import com.almasb.sslogger.LoggerConfig
import com.almasb.sslogger.LoggerLevel
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Route
import spark.Spark.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

// TODO: what to return instead of OK and NOT_OK

private val log = Logger.get("Codefest Server")

private val dbUsers = CopyOnWriteArrayList<User>()

private val activeUsers = CopyOnWriteArrayList<User>()

// runtimeID starts at 1
private val nextActive = AtomicLong(1)

fun main() {
    Logger.configure(LoggerConfig())
    Logger.addOutput(ConsoleOutput(), LoggerLevel.DEBUG)

    log.info("Starting server on port: ${Config.PORT}")

    try {
        port(Config.PORT)

        setUpRoutes()

        // TODO: debug user
        dbUsers += User(Student("Almas", "Baim"), "test", 1, 0)
    } catch (e: Exception) {
        log.fatal("Error during startup", e)
    }
}

private fun setUpRoutes() {
    get(PATH_LEADERBOARD, onLeaderboard)
    get(PATH_LOGIN, onLogin)
    get(PATH_LOGOUT, onLogout)
    get(PATH_PING, onPing)
    get(PATH_CHALLENGES, onChallenges)

    put(PATH_SUBMIT, onSubmit)
}

private val onPing = Route { _, _ ->
    "OK"
}

private val onLogin = Route { req, _ ->
    val firstName = req.queryParams("first")
    val lastName = req.queryParams("last")
    val password = req.queryParams("pass")

    log.debug("Received login request from: $firstName $lastName")

    // in case user already logged in
    val removed = activeUsers.removeIf { it.student.firstName == firstName && it.student.lastName == lastName && it.password == password }
    if (removed) {
        log.debug("Removed already logged in user $firstName $lastName")
    }

    dbUsers.find { it.student.firstName == firstName && it.student.lastName == lastName && it.password == password }
            ?.let {
                val id = nextActive.getAndIncrement()

                it.runtimeID = id

                activeUsers += it

                log.info("Num active users: ${activeUsers.size}")

                return@Route id
            }

    log.debug("Login failed")

    return@Route -1L
}

// TODO: nicer way of sanity checking the params?

private val onLogout = Route { req, _ ->

    val id = req.queryParams("id")?.toLong() ?: return@Route "NOT OK"

    log.debug("Logout request from id: $id")

    val removed = activeUsers.removeIf { it.runtimeID == id }

    if (removed) {
        log.info("Num active users: ${activeUsers.size}")
    } else {
        log.debug("$id not found in active users")
    }

    "OK"
}

private val onLeaderboard = Route { _, _ ->
    val students = dbUsers.map { it.student }
            .sortedByDescending { it.numSolvedChallenges }
            .take(5)

    jacksonObjectMapper().writeValueAsString(Leaderboard(students))
}

private val onChallenges = Route { _, _ ->
    val challenges = listOf(
            Challenge(1, "public int challenge(String a, int b)",
                    listOf(
                            ChallengeParams(5, listOf("Hello", 0)),
                            ChallengeParams(7, listOf("Hello w", 0)),
                            ChallengeParams(5, listOf("Hello World", 6)),
                            ChallengeParams(-2, listOf("Hello", 7))
                    )
            )
    )

    val codefest = Codefest(challenges)

    jacksonObjectMapper().writeValueAsString(codefest)
}

private val onSubmit = Route { req, _ ->
    // check token first
    val id = req.queryParams("id")?.toLong() ?: return@Route "NOT OK"

    // get challenge id that passed tests
    val challengeID = req.queryParams("challengeID")?.toInt() ?: return@Route "NOT OK"

    findUser(id)?.apply { student.solvedChallenges += challengeID } ?: return@Route "NOT OK"

    "OK"
}

private fun findUser(id: Long) = activeUsers.find { it.runtimeID == id }