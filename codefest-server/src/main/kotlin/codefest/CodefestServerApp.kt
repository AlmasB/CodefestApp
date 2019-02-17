package codefest

import codefest.data.Leaderboard
import codefest.data.Student
import com.almasb.sslogger.ConsoleOutput
import com.almasb.sslogger.Logger
import com.almasb.sslogger.LoggerConfig
import com.almasb.sslogger.LoggerLevel
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Request
import spark.Response
import spark.Route
import spark.Spark.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

private val log = Logger.get("Codefest Server")

private const val PORT = 55555

private val activeUsers = CopyOnWriteArrayList<Student>()

// id starts at 1
private val nextActive = AtomicLong(1)

fun main() {
    Logger.configure(LoggerConfig())
    Logger.addOutput(ConsoleOutput(), LoggerLevel.DEBUG)

    log.info("Starting server on port: $PORT")

    port(PORT)

    get("/top") { req, res ->

        val students = listOf(
                Student("AA", "BB", mutableListOf(1, 2, 5)),
                Student("CC", "DD", mutableListOf(1, 2, 4)),
                Student("EE", "FF", mutableListOf(1, 3, 6)),
                Student("GG", "HH", mutableListOf(1, 7, 8)),
                Student("II", "JJ", mutableListOf(1, 4, 5))
        )

        jacksonObjectMapper().writeValueAsString(Leaderboard(students))
    }

    get("/login", onLogin)
}

private val onLogin = Route { req, res ->

    val firstName = req.queryParams("firstName")
    val lastName = req.queryParams("lastName")

    //log.debug("Received login request with params: ${req.queryParams("user")}")

    nextActive.getAndIncrement()
}