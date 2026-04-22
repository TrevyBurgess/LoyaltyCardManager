package com.cyberfeedforward.loyaltycardmanager2

import com.cyberfeedforward.loyaltycardmanager2.ui.cards.CameraExecutors
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun cameraExecutors_newAnalysisExecutor_executesTasks() {
        val executor = CameraExecutors.newAnalysisExecutor()
        try {
            val latch = CountDownLatch(1)
            executor.execute { latch.countDown() }

            assertTrue(latch.await(2, TimeUnit.SECONDS))
        } finally {
            executor.shutdownNow()
        }
    }
}

