package codefest.server

//import org.eclipse.jetty.util.security.Credential.MD5.digest
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


private val encryptor: PasswordEncryptor = AESEncryptor()

fun encryptPassword(password: String, secretKey : String) = encryptor.encrypt(password, secretKey)

private val AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
private var rnd = SecureRandom()
private val secretKeyLength = 32

fun generateSecretKey(): String {
    val sb = StringBuilder(secretKeyLength)
    for (i in 0 until secretKeyLength)
        sb.append(AB[rnd.nextInt(AB.length)])
    return sb.toString()
}

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
interface PasswordEncryptor {

    /**
     * @param password in raw form
     * @return password in encrypted form
     */
    fun encrypt(password: String, secret: String): String
}

private class AESEncryptor : PasswordEncryptor {
    private var secretKey: SecretKeySpec? = null
    private var key: ByteArray? = null

    fun setKey(myKey: String) {
        var sha: MessageDigest? = null

        key = myKey.toByteArray(charset("UTF-8"))
        sha = MessageDigest.getInstance("SHA-1")
        key = sha!!.digest(key)
        key = Arrays.copyOf(key!!, 16)
        secretKey = SecretKeySpec(key!!, "AES")
    }

    override fun encrypt(password: String, secret: String): String {
        setKey(secret)
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        println(Base64.getEncoder().encodeToString(cipher.doFinal(password.toByteArray(charset("UTF-8")))))
        return Base64.getEncoder().encodeToString(cipher.doFinal(password.toByteArray(charset("UTF-8"))))
    }
}