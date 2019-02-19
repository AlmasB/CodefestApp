package codefest.client

import com.almasb.sslogger.Logger

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class IOResponse<T> {

    private val log = Logger.get("IOResponseLogger")

    var onSuccess: ((T) -> Unit)? = null
    var onFailure: ((Throwable) -> Unit) = { log.warning("ResponseException", it) }
}