# ConnectionChecker

Status: [![Release](https://jitpack.io/v/muddassir235/connection_checker.svg)]
(https://jitpack.io/#muddassir235/connection_checker/)

Android library for checking the internet connectivity of a device.

## Add Dependencies
Add the following in your project level build.gradle
```groovy
// ...
allprojects {
    repositories {
        // ...
        
        maven { url 'https://jitpack.io' }
    }
    // ...
}
```
and the following in your app level build.gradle
```groovy
dependencies {
    // ...
    implementation 'com.github.muddassir235:connection_checker:v1.3'
}
```

## Use The Library

Instantiate an object
```kotlin
val connectionChecker = ConnectionChecker(this, lifecycle)
```
Add connectivity listener
```kotlin
connectionChecker.connectivityListener = object: ConnectivityListener {
    override fun onConnectionState(state: ConnectionState) {

    }
}
```

## Example
Example in an android activity.
```kotlin
class MainActivity : AppCompatActivity(), ConnectivityListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectionChecker = ConnectionChecker(this, lifecycle)
        connectionChecker.connectivityListener = this
    }

    override fun onConnectionState(state: ConnectionState) {
        connection_status_tv.text = if(state == ConnectionState.CONNECTED) {
            "Connected"
        } else if(state == ConnectionState.SLOW) {
            "Slow Internet Connection"
        } else {
            "Disconnected"
        }
    }
}
```
