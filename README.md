# ConnectionChecker
Android library for checking the internet connectivity of a device.

Instanciate an object
```
val connectionChecker = ConnectionChecker(this, lifecycle)
```
Add connectivity listener
```
connectionChecker.connectivityListener = object: ConnectivityListener {
    override fun onConnected() {

    }

    override fun onConnectionSlow() {

    }

    override fun onDisconnected() {

    }
}
```
Start checking the connection.
```
 connectionChecker.startChecking()
```

Example in an android activity.
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
