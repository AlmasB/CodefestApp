package codefest.client

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class HomeController {

    @FromContext
    private lateinit var context: Context

    fun onLogOut() {
        Server.requestLogout {
            onSuccess = {
                context.clientID.value = -1

                context.showLogin()
            }
        }
    }
}