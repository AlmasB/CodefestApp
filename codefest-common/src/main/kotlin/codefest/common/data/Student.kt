package codefest.common.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Student(
        val firstName: String,
        val lastName: String,
        val solvedChallenges: MutableSet<Int> = mutableSetOf(),
        val solutions: MutableMap<Int, String> = mutableMapOf()
) {
    val numSolvedChallenges
        get() = solvedChallenges.size

    override fun toString(): String {
        return "$firstName $lastName: $numSolvedChallenges"
    }
}
