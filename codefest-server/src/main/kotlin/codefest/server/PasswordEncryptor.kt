package codefest.server

private val encryptor: PasswordEncryptor = SimplePasswordEncryptor()

fun encryptPassword(password: String) = encryptor.encrypt(password)

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
interface PasswordEncryptor {

    /**
     * @param password in raw form
     * @return password in encrypted form
     */
    fun encrypt(password: String): String
}

private class SimplePasswordEncryptor : PasswordEncryptor {
    override fun encrypt(password: String): String {
        return password.reversed()
    }
}