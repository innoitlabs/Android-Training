package com.example.coroutinesasync

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        // Setup for tests
    }

    @Test
    fun testFlowCollection() = runTest {
        // Example of testing Flow collection
        val testFlow = kotlinx.coroutines.flow.flow {
            emit(1)
            emit(2)
            emit(3)
        }
        
        val values = mutableListOf<Int>()
        testFlow.collect { values.add(it) }
        
        assertEquals(listOf(1, 2, 3), values)
    }
}
