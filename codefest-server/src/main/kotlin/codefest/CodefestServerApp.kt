package codefest

import codefest.data.Leaderboard
import codefest.data.Student
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Spark.*

fun main() {
    port(55555)

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
}