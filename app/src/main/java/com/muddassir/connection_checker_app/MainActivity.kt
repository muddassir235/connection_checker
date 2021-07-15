package com.muddassir.connection_checker_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.muddassir.connection_checker.ConnectionChecker
import com.muddassir.connection_checker.ConnectionState
import com.muddassir.connection_checker.ConnectivityListener
import com.muddassir.connection_checker.checkConnection
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ConnectivityListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkConnectionFunctionAndInterface()
    }

    fun checkConnectionClassAndInterface() {
        val connectionChecker = ConnectionChecker(this)
        connectionChecker.connectivityListener = this
    }

    fun checkConnectionFunctionAndInterface() {
        checkConnection(this)
    }

    fun checkConnectionFunctionAndLambda() {
        checkConnection(this) {
            onConnectionState(it)
        }
    }
    
    override fun onConnectionState(state: ConnectionState) {
        connection_status_tv.text = when (state) {
            ConnectionState.CONNECTED -> {
                "Connected"
            }
            ConnectionState.SLOW -> {
                "Slow Internet Connection"
            }
            else -> {
                "Disconnected"
            }
        }

        // Test Lifecycle
//        if(state == ConnectionState.DISCONNECTED) {
//            startActivity(Intent(this, OtherActivity::class.java))
//            finish()
//        }
    }
}

class OtherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}