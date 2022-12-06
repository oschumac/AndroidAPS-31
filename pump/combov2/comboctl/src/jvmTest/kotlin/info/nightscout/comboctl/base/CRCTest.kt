package info.nightscout.comboctl.base

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CRCTest {
    @Test
    fun verifyChecksum() {
        val inputData = "0123456789abcdef".toByteArray(Charsets.UTF_8).toList()

        val expectedChecksum = 0x02A2
        val actualChecksum = calculateCRC16MCRF4XX(inputData)
        Assertions.assertEquals(expectedChecksum, actualChecksum)
    }
}
