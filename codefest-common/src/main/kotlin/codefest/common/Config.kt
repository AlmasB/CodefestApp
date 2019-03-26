package codefest.common

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Config {

    val IP = "localhost"
    val PORT = 56679

    /* GET */

    val PATH_PING = "/ping"

    /**
     * /login?first=FIRST_NAME&last=LAST_NAME&pass=PASSWORD
     */
    val PATH_LOGIN = "/login"

    val PATH_LOGOUT = "/logout"
    val PATH_LEADERBOARD = "/leaderboard"
    val PATH_CHALLENGES = "/challenges"
    val PATH_REGISTER = "/register"

    /* PUT */

    val PATH_SUBMIT = "/submit"
}