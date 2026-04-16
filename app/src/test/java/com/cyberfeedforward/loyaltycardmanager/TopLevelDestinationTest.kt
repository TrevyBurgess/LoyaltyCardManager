package com.cyberfeedforward.loyaltycardmanager

import com.cyberfeedforward.loyaltycardmanager.ui.navigation.TopLevelDestination
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TopLevelDestinationTest {

    @Test
    fun routes_areNonBlankAndUnique() {
        val routes = TopLevelDestination.entries.map { it.route }

        assertTrue(routes.all { it.isNotBlank() })
        assertEquals(routes.toSet().size, routes.size)
    }

    @Test
    fun hasExpectedTopLevelDestinations() {
        val labels = TopLevelDestination.entries.map { it.label }.toSet()
        assertTrue(labels.contains("Home"))
        assertTrue(labels.contains("Cards"))
        assertTrue(labels.contains("Settings"))
        assertTrue(labels.contains("About"))
    }
}


