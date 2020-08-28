# ConnectionChecker
Android library for checking the internet connectivity of a device.


```
class MainActivity : AppCompatActivity(), ConnectivityListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectionChecker = ConnectionChecker(this, lifecycle)
        connectionChecker.connectivityListener = this
        
        connectionChecker.startChecking()
    }

    override fun onConnected() {
        
    }

    override fun onConnectionSlow() {

    }

    override fun onDisconnected() {

    }
}
```
