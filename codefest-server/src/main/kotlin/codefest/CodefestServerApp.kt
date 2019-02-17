package codefest

import codefest.data.Student
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import spark.Spark.*

fun main() {
    port(55555)

    get("/top") { req, res ->
        val s = Student("AA", "BB", mutableListOf(1, 2, 5))

        jacksonObjectMapper().writeValueAsString(s)
    }
}