package info.nightscout.comboctl.base

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ProgressReporterTest {
    // NOTE: In the tests here, the progress sequences are fairly
    // arbitrary, and do _not_ reflect how actual sequences used
    // in pairing etc. look like.

    @Test
    fun testBasicProgress() {
        val progressReporter = ProgressReporter(
            listOf(
                BasicProgressStage.EstablishingBtConnection::class,
                BasicProgressStage.PerformingConnectionHandshake::class,
                BasicProgressStage.ComboPairingKeyAndPinRequested::class,
                BasicProgressStage.ComboPairingFinishing::class
            ),
            Unit
        )

        Assertions.assertEquals(
            ProgressReport(0, 4, BasicProgressStage.Idle, 0.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(
            BasicProgressStage.EstablishingBtConnection(1, 3)
        )
        Assertions.assertEquals(
            ProgressReport(0, 4, BasicProgressStage.EstablishingBtConnection(1, 3), 0.0 / 4.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(
            BasicProgressStage.EstablishingBtConnection(2, 3)
        )
        Assertions.assertEquals(
            ProgressReport(0, 4, BasicProgressStage.EstablishingBtConnection(2, 3), 0.0 / 4.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(BasicProgressStage.PerformingConnectionHandshake)
        Assertions.assertEquals(
            ProgressReport(1, 4, BasicProgressStage.PerformingConnectionHandshake, 1.0 / 4.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(BasicProgressStage.ComboPairingKeyAndPinRequested)
        Assertions.assertEquals(
            ProgressReport(2, 4, BasicProgressStage.ComboPairingKeyAndPinRequested, 2.0 / 4.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(BasicProgressStage.ComboPairingFinishing)
        Assertions.assertEquals(
            ProgressReport(3, 4, BasicProgressStage.ComboPairingFinishing, 3.0 / 4.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(BasicProgressStage.Finished)
        Assertions.assertEquals(
            ProgressReport(4, 4, BasicProgressStage.Finished, 4.0 / 4.0),
            progressReporter.progressFlow.value
        )
    }

    @Test
    fun testSkippedSteps() {
        val progressReporter = ProgressReporter(
            listOf(
                BasicProgressStage.EstablishingBtConnection::class,
                BasicProgressStage.PerformingConnectionHandshake::class,
                BasicProgressStage.ComboPairingKeyAndPinRequested::class,
                BasicProgressStage.ComboPairingFinishing::class
            ),
            Unit
        )

        Assertions.assertEquals(
            ProgressReport(0, 4, BasicProgressStage.Idle, 0.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(
            BasicProgressStage.EstablishingBtConnection(1, 3)
        )
        Assertions.assertEquals(
            ProgressReport(0, 4, BasicProgressStage.EstablishingBtConnection(1, 3), 0.0 / 4.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(BasicProgressStage.ComboPairingFinishing)
        Assertions.assertEquals(
            ProgressReport(3, 4, BasicProgressStage.ComboPairingFinishing, 3.0 / 4.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(BasicProgressStage.Finished)
        Assertions.assertEquals(
            ProgressReport(4, 4, BasicProgressStage.Finished, 4.0 / 4.0),
            progressReporter.progressFlow.value
        )
    }

    @Test
    fun testBackwardsProgress() {
        val progressReporter = ProgressReporter(
            listOf(
                BasicProgressStage.EstablishingBtConnection::class,
                BasicProgressStage.PerformingConnectionHandshake::class,
                BasicProgressStage.ComboPairingKeyAndPinRequested::class,
                BasicProgressStage.ComboPairingFinishing::class
            ),
            Unit
        )

        Assertions.assertEquals(
            ProgressReport(0, 4, BasicProgressStage.Idle, 0.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(BasicProgressStage.ComboPairingFinishing)
        Assertions.assertEquals(
            ProgressReport(3, 4, BasicProgressStage.ComboPairingFinishing, 3.0 / 4.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(
            BasicProgressStage.EstablishingBtConnection(1, 3)
        )
        Assertions.assertEquals(
            ProgressReport(0, 4, BasicProgressStage.EstablishingBtConnection(1, 3), 0.0 / 4.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(BasicProgressStage.Finished)
        Assertions.assertEquals(
            ProgressReport(4, 4, BasicProgressStage.Finished, 4.0 / 4.0),
            progressReporter.progressFlow.value
        )
    }

    @Test
    fun testAbort() {
        val progressReporter = ProgressReporter(
            listOf(
                BasicProgressStage.EstablishingBtConnection::class,
                BasicProgressStage.PerformingConnectionHandshake::class,
                BasicProgressStage.ComboPairingKeyAndPinRequested::class,
                BasicProgressStage.ComboPairingFinishing::class
            ),
            Unit
        )

        Assertions.assertEquals(
            ProgressReport(0, 4, BasicProgressStage.Idle, 0.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(
            BasicProgressStage.EstablishingBtConnection(1, 3)
        )
        Assertions.assertEquals(
            ProgressReport(0, 4, BasicProgressStage.EstablishingBtConnection(1, 3), 0.0 / 4.0),
            progressReporter.progressFlow.value
        )

        progressReporter.setCurrentProgressStage(BasicProgressStage.Cancelled)
        Assertions.assertEquals(
            ProgressReport(4, 4, BasicProgressStage.Cancelled, 4.0 / 4.0),
            progressReporter.progressFlow.value
        )
    }
}
