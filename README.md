# ConnectionChecker

[![Release](https://jitpack.io/v/muddassir235/connection_checker.svg?style=flat-square)](https://jitpack.io/#muddassir235/connection_checker/)

Android library for checking the internet connectivity of a device.

Used in https://play.google.com/store/apps/details?id=com.muddassirkhan.quran_android

## Add Dependencies
Add the following in your project level build.gradle
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
and the following in your app level build.gradle
```groovy
dependencies {
    implementation 'com.github.muddassir235:connection_checker:1.7'
}
```

## Use The Library

Use this library in one of the following three ways,

### 1. Using a method and an interface

```kotlin
checkConnection(this) // this: lifecycleOwner (e.g. Activity) which has implemented ConnectivityListener
```
By default it will ping https://www.google.com. The user can set the url to ping.
```kotlin
checkConnection(this, "https://www.site.url")
```
By default the least required lifecycle state is `Lifecycle.State.RESUMED`. The user can set it to what they require.
```kotlin
checkConnection(this, "https://www.site.url", Lifecycle.State.STARTED)
```

Example in an Android Activity.
```kotlin
class MainActivity : AppCompatActivity(), ConnectivityListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkConnection(this)
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
    }
}
```



### 2. [Or] Using a method and a lambda callback

```kotlin
// this is a lifecycleOwner (e.g. Activity or ViewLifecycleOwner)
checkConnection(this) { connectionState ->
    // Your logic here
}
```
By default it will ping https://www.google.com. The user can set the url to ping.
```kotlin
checkConnection(this, "https://www.site.url") { connectionState ->
    // Your logic here
}
```
By default the least required lifecycle state is Lifecycle.State.RESUMED. The user can set it to what they require.
```kotlin
checkConnection(this, "https://www.site.url", Lifecycle.State.STARTED) { connectionState ->
    // Your logic here
}
```

Example in an Android Activity.

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkConnection(this) { connectionState ->
            connection_status_tv.text = when(connectionState) {
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
        }
    }
}
```



### 3. [Or] Using a class object and an interface

```kotlin
val connectionChecker = ConnectionChecker(this)
```
By default it will ping https://www.google.com. The user can set the url to ping.
```kotlin
val connectionChecker = ConnectionChecker(this, "https://www.site.url")
```
By default the least required lifecycle state is Lifecycle.State.RESUMED. The user can set it to what they require.
```kotlin
val connectionChecker = ConnectionChecker(this, "https://www.site.url", Lifecycle.State.STARTED)
```

Add connectivity listener
```kotlin
connectionChecker.connectivityListener = object: ConnectivityListener {
    override fun onConnectionState(state: ConnectionState) {
        // Your logic goes here
    }
}
```

Example in an Android Activity.

```kotlin
class MainActivity : AppCompatActivity(), ConnectivityListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectionChecker = ConnectionChecker(this, lifecycle)
        connectionChecker.connectivityListener = this
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
    }
}
```

## Uses

* https://github.com/muddassir235/kmacros

## [Apps by Muddassir Ahmed](https://play.google.com/store/apps/developer?id=Muddassir+Khan):
* https://play.google.com/store/apps/details?id=com.muddassirkhan.quran_android
* https://play.google.com/store/apps/details?id=com.app.kitaabattawheed


## Muddassir Ahmed Links:

* https://www.linkedin.com/in/muddassir35/
* https://muddassirahmed.medium.com/
* https://stackoverflow.com/users/5841416/muddassir-ahmed
