package codefest.common

import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test

class ConfigTest {

    @Test
    fun `Simple test`() {
        assertThat(Config.IP, `is`("localhost"))
    }
}