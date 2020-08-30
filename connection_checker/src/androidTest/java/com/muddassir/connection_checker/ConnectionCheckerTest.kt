package com.muddassir.connection_checker

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ConnectionCheckerTest {

    @Test
    fun checkConnectedState() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val connectionChecker = ConnectionChecker(context, null)
            connectionChecker.connectivityListener = object: ConnectivityListener {
                override fun onConnected() {
                    assertTrue(false)
                }

                override fun onDisconnected() {
                    assertTrue(true)
                }

                override fun onConnectionSlow() {
                    assertTrue(false)
                }
            }

            // Disconnect from the internet. How do I do this?
        }

        Thread.sleep(30000)
    }
}