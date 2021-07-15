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

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.lang.Exception

enum class ConnectionState {
    CONNECTED, SLOW, DISCONNECTED
}

interface ConnectivityListener {
    fun onConnectionState(state: ConnectionState)
}

class ConnectionChecker(private val lifecycleOwner: LifecycleOwner?,
                        private val url: String = "https://www.google.com",
                        private val leastLifecycleState: Lifecycle.State = Lifecycle.State.RESUMED) {
    var connectivityListener: ConnectivityListener? = null
        set(value) {
            if(value != null) {
                checkConnection(lifecycleOwner, url, leastLifecycleState) {
                    value.onConnectionState(it)
                }
            }

            field = value
        }
}

fun checkConnection(lifecycleOwner: LifecycleOwner?,
                    url: String = "https://www.google.com",
                    leastLifecycleState: Lifecycle.State = Lifecycle.State.RESUMED
) {
    if(lifecycleOwner == null) throw java.lang.IllegalArgumentException("Unable to find lifecycle scope.")

    checkConnection(lifecycleOwner, url, leastLifecycleState) {
        (lifecycleOwner as? ConnectivityListener)?.onConnectionState(it)
    }
}

fun checkConnection(lifecycleOwner: LifecycleOwner?,
                    url: String = "https://www.google.com",
                    leastLifecycleState: Lifecycle.State = Lifecycle.State.RESUMED,
                    onConnectionState: (connectionState: ConnectionState) -> Unit
) {
    val scope = lifecycleOwner?.lifecycleScope
        ?: throw java.lang.IllegalArgumentException("Unable to find lifecycle scope.")

    when(leastLifecycleState) {
        Lifecycle.State.RESUMED -> scope.launchWhenResumed {
            checkConnection(url, onConnectionState)
        }
        Lifecycle.State.STARTED -> scope.launchWhenStarted {
            checkConnection(url, onConnectionState)
        }
        Lifecycle.State.CREATED -> scope.launchWhenCreated {
            checkConnection(url, onConnectionState)
        }
        else -> throw IllegalArgumentException(
            "leastLifecycleState should be one of CREATED, STARTED, RESUMED")
    }
}

private suspend fun checkConnection(url: String, onConnectionState: (connectionState: ConnectionState) -> Unit) {
    val client: OkHttpClient = OkHttpClient().newBuilder()
        .build()

    withContext(Dispatchers.IO) {
        var currentState = ConnectionState.CONNECTED
        while (true) {
            Log.e("******** Calling", "Connection")
            val startTime = System.currentTimeMillis()
            val request: Request = Request.Builder().url(url)
                .method("GET", null).build()

            val response: Response? = try {
                client.newCall(request).execute()
            } catch (e: Exception) {
                null
            }

            val responseCode = response?.code ?: 500
            val timeTaken = System.currentTimeMillis() - startTime

            response?.close()

            withContext(Dispatchers.Main) {
                val connectionState = if (!responseCode.toString().startsWith("2")) {
                    ConnectionState.DISCONNECTED
                } else {
                    if (timeTaken > 2000) ConnectionState.SLOW else ConnectionState.CONNECTED
                }

                if (connectionState != currentState) {
                    onConnectionState(connectionState)
                    currentState = connectionState
                }
            }

            kotlinx.coroutines.delay(5000)
        }
    }
}