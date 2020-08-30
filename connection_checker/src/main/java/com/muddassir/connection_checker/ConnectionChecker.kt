/**
 * Author          : Muddassir Ahmed Khan
 * Contact         : muddassir.ahmed235@gmail.com
 * Github Username : muddassir235
 *
 * ConnectionChecker.kt
 *
 * Class used to check connectivity of the application to the internet. It works as follows
 *
 * - Every five seconds the app tries to ping google.com
 *    - If google.com loads within 2 seconds the app is marked as CONNECTED
 *    - If google.com takes more than 2 seconds to load the connection is deemed SLOW.
 *    - If google.com doesn't load the app is marked as DISCONNECTED
 */
package com.muddassir.connection_checker

import android.content.Context
import androidx.lifecycle.Lifecycle

enum class ConnectionState {
    CONNECTED, SLOW, DISCONNECTED
}

interface ConnectivityListener {
    fun onConnectionState(state: ConnectionState)
}

class ConnectionChecker(private val context: Context, private val lifecycle: Lifecycle?) {
    private var currentState = ConnectionState.CONNECTED

    /* The connectivity checker will ping this url continuously */
    private val url = "https://www.google.com"

    var connectivityListener: ConnectivityListener? = null
        set(value) {
            delay(5000) { check() }
            field = value
        }

    private val lifecycleIsStarted: Boolean get() = (lifecycle?.currentState?.isAtLeast(
            Lifecycle.State.RESUMED) ?: false)

    private fun check() {
        evaluateConnection { connectionState ->
            if(lifecycleIsStarted and (connectionState != currentState)) {
                connectivityListener?.onConnectionState(connectionState)
                currentState = connectionState
            }

            if(connectivityListener != null) checkAgain()
        }
    }

    private fun checkAgain() {
        delay(5000) { check() }
    }

    private fun evaluateConnection(onResult: ((connectionState: ConnectionState)->Unit)) {
        pingUrl(context, url) { failed, timeTaken ->
            val connectionState = if(failed) {
                ConnectionState.DISCONNECTED
            } else {
                if(timeTaken > 2000) ConnectionState.SLOW else ConnectionState.CONNECTED
            }

            onResult.invoke(connectionState)
        }
    }
}