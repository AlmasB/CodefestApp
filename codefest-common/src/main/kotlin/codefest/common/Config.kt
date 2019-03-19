package codefest.common

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class Config {

    companion object {

        const val IP = "localhost"
        const val PORT = 56677

        /* GET */

        const val PATH_PING = "/ping"
        const val PATH_LOGIN = "/login"
        const val PATH_LOGOUT = "/logout"
        const val PATH_LEADERBOARD = "/leaderboard"
        const val PATH_CHALLENGES = "/challenges"
        const val PATH_REGISTER = "/register"

        /* PUT */

        const val PATH_SUBMIT = "/submit"
    }
}