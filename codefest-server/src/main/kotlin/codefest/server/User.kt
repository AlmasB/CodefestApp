package codefest.server

import codefest.common.data.Student

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
data class User(
        val student: Student,
        val password: String,
        var runtimeID: Long,
        val lastAccess: Long
) {
}