package com.example.performancesecurity.NotesApp

import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmarkTest {
    @get:Rule val benchmarkRule = MacrobenchmarkRule()
    
    @Test fun startup() = benchmarkRule.measureRepeated {
        startActivityAndWait()
    }
    
    @Test fun startupWithNotes() = benchmarkRule.measureRepeated {
        startActivityAndWait()
        // Add some notes to test performance with data
        // This would require additional setup
    }
}
