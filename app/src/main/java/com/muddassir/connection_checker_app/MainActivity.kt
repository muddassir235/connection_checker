package com.muddassir.connection_checker_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.muddassir.connection_checker.ConnectionChecker
import com.muddassir.connection_checker.ConnectivityListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ConnectivityListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectionChecker = ConnectionChecker(this, lifecycle)
        connectionChecker.connectivityListener = this
        connectionChecker.startChecking()
    }

    override fun onConnected() {
        connection_status_tv.text = "Connected"
    }

    override fun onConnectionSlow() {
        connection_status_tv.text = "Slow Internet Connection"
    }

    override fun onDisconnected() {
        connection_status_tv.text = "Disconnected"
    }
}