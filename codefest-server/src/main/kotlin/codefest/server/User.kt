package codefest.server

import codefest.common.data.Student

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
data class User(
        val student: Student,

        /**
         * Password in encrypted form.
         */
        val password: String,
        var runtimeID: Long = 0,
        val lastAccess: Long = 0
) {

    fun hasName(firstName: String, lastName: String): Boolean {
        return student.firstName == firstName && student.lastName == lastName
    }
}