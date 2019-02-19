package codefest.server

import codefest.common.Config
import codefest.common.Config.Companion.PATH_CHALLENGES
import codefest.common.Config.Companion.PATH_LEADERBOARD
import codefest.common.Config.Companion.PATH_LOGIN
import codefest.common.Config.Companion.PATH_LOGOUT
import codefest.common.Config.Companion.PATH_PING
import codefest.common.data.Challenge
import codefest.common.data.Leaderboard
import codefest.common.data.Student
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
}

private fun setUpRoutes() {
    get(PATH_LEADERBOARD, onLeaderboard)
    get(PATH_LOGIN, onLogin)
    get(PATH_LOGOUT, onLogout)
    get(PATH_PING, onPing)
    get(PATH_CHALLENGES, onChallenges)
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
    val students = listOf(
            Student("AA", "BB", mutableListOf(1, 2, 5)),
            Student("CC", "DD", mutableListOf(1, 2, 4)),
            Student("EE", "FF", mutableListOf(1, 3, 6)),
            Student("GG", "HH", mutableListOf(1, 7, 8)),
            Student("II", "JJ", mutableListOf(1, 4, 5))
    )

    jacksonObjectMapper().writeValueAsString(Leaderboard(students))
}

private val onChallenges = Route { _, _ ->
    val challenges = listOf(
            Challenge(1, "This is a simple question")
    )

    jacksonObjectMapper().writeValueAsString(challenges.first())
}