/**
 * Al Quran Al Kareem Recitation by Various Qaris القرآن الكريم
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
import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

/**
 * ConnectivityListener - Interface with methods that get triggered when connectivity status changes.
 */
interface ConnectivityListener {
    fun onConnected()
    fun onConnectionSlow()
    fun onDisconnected()
}

class ConnectionChecker(context: Context, private val lifecycle: Lifecycle) {
    private enum class ConnectionState{
        CONNECTED,
        SLOW,
        DISCONNECTED,
    }
    private var currentState: ConnectionState? =
        ConnectionState.CONNECTED

    /* The connectivity checker will ping this url continuously */
    private val url = "https://www.google.com"
    private var queue: RequestQueue = Volley.newRequestQueue(context) // Volley Request Queue
    /** Whether to check next time 5 seconds pass or not. No need to check if the
     * Activity/Fragment is in the paused state.
     */
    private var shouldCheckNextTime = true
    var connectivityListener : ConnectivityListener? = null

    private val isResumed /* Activity/Fragment is in resumed state */: Boolean get() =
        lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)

    /**
     * startChecking - Start checking the connection
     */
    fun startChecking() {
        pingGoogle()
    }

    /**
     * stopChecking - Stop checking the connection (In case the container is paused/stopped/destroyed)
     */
    private fun stopChecking() {
        shouldCheckNextTime = false
    }

    // region Lifecycle Methods
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        startChecking()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        stopChecking()
    }
    // endregion

    /**
     * pingGoogle - Ping google every 5 seconds and check connection status based on the response.
     */
    private fun pingGoogle() {
        var startTime = 0L

        val pingGoogle = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                /* Time taken  (Current Time - Start Time) */
                val timeTaken = System.currentTimeMillis() - startTime

                currentState = if(timeTaken > 2000 /* loading google.com took more that 2 secs */) {
                    if(isResumed && currentState != ConnectionState.SLOW)
                        connectivityListener?.onConnectionSlow()

                    ConnectionState.SLOW /* The connection is slow */
                } else {
                    if(isResumed && currentState != ConnectionState.CONNECTED)
                        connectivityListener?.onConnected()

                    ConnectionState.CONNECTED /* The connection is normal */
                }

                pingGoogle(/* Call this method recursively every 5 seconds*/)
            }, Response.ErrorListener {
                if(isResumed && currentState != ConnectionState.DISCONNECTED)
                    connectivityListener?.onDisconnected()

                currentState =
                    ConnectionState.DISCONNECTED  /* The app is disconnected */

                pingGoogle(/* Call this method recursively every 5 seconds*/)
            })

        Handler().postDelayed({
            startTime = System.currentTimeMillis() /* Mark start time */

            queue.add(pingGoogle)
        }, 5000 /* Delay of 5 seconds */)
    }
}