package codefest.common.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
data class Student(
        val firstName: String,
        val lastName: String,
        val solvedChallenges: MutableList<Int> = arrayListOf()
) {
    val numSolvedChallenges
        get() = solvedChallenges.size
}