package codefest

import codefest.data.Student

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
data class User(
        val student: Student,
        val password: String,
        val id: Long
) {
}