package codefest.common

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Config {

    companion object {

        const val IP = "localhost"
        const val PORT = 55555

        /* GET */

        const val PATH_PING = "/ping"
        const val PATH_LOGIN = "/login"
        const val PATH_LOGOUT = "/logout"
        const val PATH_LEADERBOARD = "/leaderboard"
        const val PATH_CHALLENGES = "/challenges"

        /* PUT */

        const val PATH_SUBMIT = "/submit"
    }
}