package codefest.common.data

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
data class Challenge(
        val id: Int,

        // method signature, e.g.
        // public int challenge(String a, int b)
        val signature: String,

        //val paramTypes: ChallengeParamTypes,
        val params: List<ChallengeParams>
)

data class ChallengeParamTypes(
        val output: Class<*>,
        val inputs: List<Class<*>>
)

data class ChallengeParams(
        val output: Any,
        val inputs: List<Any>
)

data class Codefest(val challenges: List<Challenge>)