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

private val log = Logger.get("Codefest Server")

private val dbUsers = CopyOnWriteArrayList<User>()

private val activeUsers = CopyOnWriteArrayList<User>()

// runtimeID starts at 1
private val nextActive = AtomicLong(1)

fun main() {
    Logger.configure(LoggerConfig())
    Logger.addOutput(ConsoleOutput(), LoggerLevel.DEBUG)

    log.info("Starting server on port: ${Config.PORT}")

    port(Config.PORT)

    setUpRoutes()

    dbUsers += User(Student("Almas", "Baim"), "test", 1, 0)
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

private val onLogin = Route { req, res ->
    val firstName = req.queryParams("first")
    val lastName = req.queryParams("last")
    val password = req.queryParams("pass")

    log.debug("Received login request from: $firstName $lastName")

    val user = dbUsers.find { it.student.firstName == firstName && it.student.lastName == lastName && it.password == password }

    user?.let {
        val id = nextActive.getAndIncrement()

        it.runtimeID = id

        activeUsers += it

        return@Route id
    }

    return@Route -1L
}

private val onLogout = Route { req, _ ->
    val id = req.queryParams("id").toLong()

    activeUsers.removeIf { it.runtimeID == id }

    "OK"
}

private val onLeaderboard = Route { _, _ ->
    val students = dbUsers.map { it.student }

    jacksonObjectMapper().writeValueAsString(Leaderboard(students))
}

private val onChallenges = Route { _, _ ->
    val challenges = listOf(
            Challenge(1, "public int challenge(String a, int b)", listOf(
                    ChallengeParams(5, listOf("Hello", 0)),
                    ChallengeParams(7, listOf("Hello w", 0)),
                    ChallengeParams(5, listOf("Hello World", 6)),
                    ChallengeParams(-2, listOf("Hello", 7))
            )),
            Challenge(2, "public int challenge(String a, int b)", listOf(
                    ChallengeParams(1, listOf("Hello", 0)),
                    ChallengeParams(8, listOf("Hello w", 0)),
                    ChallengeParams(5, listOf("Hello World", 6)),
                    ChallengeParams(-2, listOf("Hello", 7))
            )),
            Challenge(3, "public int challenge(String a, int b)", listOf(
                    ChallengeParams(0, listOf("Hello", 0)),
                    ChallengeParams(7, listOf("Hello w", 0)),
                    ChallengeParams(5, listOf("Hello World", 6)),
                    ChallengeParams(-2, listOf("Hello", 7))
            )),
            Challenge(4, "public int challenge(String a, int b)", listOf(
                    ChallengeParams(3, listOf("Hello", 0)),
                    ChallengeParams(7, listOf("Hello w", 0)),
                    ChallengeParams(5, listOf("Hello World", 6)),
                    ChallengeParams(-2, listOf("Hello", 7))
            ))
    )

    val codefest = Codefest(challenges)

    jacksonObjectMapper().writeValueAsString(codefest)
}

private val onSubmit = Route { req, _ ->
    // check token first
    val id = req.queryParams("id").toLong()

    // get challenge id that passed tests
    val challengeID = req.queryParams("challengeID").toInt()

    findUser(id)?.apply {
        student.solvedChallenges += challengeID
    }

    "OK"
}

private fun findUser(id: Long) = activeUsers.find { it.runtimeID == id }