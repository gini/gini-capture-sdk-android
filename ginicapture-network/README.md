![Gini Capture SDK for Android](../GiniCapture_Logo.png)

Gini Capture Network Library for Android
===============================

 The Gini Capture Network Library for Android provides a default implementation of the network related
 tasks required by the Gini Capture SDK.
    
Adding this library along with the Gini Capture SDK to your application is the quickest way to
integrate invoice scanning.

In order for the Gini Capture SDK to use the default implementations, pass the instances to the
`GiniCapture.Builder` when creating a new `GiniCapture`.

Example
-------

The example apps in the Gini Capture SDK demonstrate how to integrate the Gini Capture Network
Library to easily add document analysis to your app.

We also provide a separate standalone [example
app](https://github.com/gini/gini-vision-lib-android-example). This is more like a real world app
and serves as an additional help for you to discover how the Gini Capture Network Library along with
the Gini Capture SDK should be used.

Requirements
------------

Android 4.4+ (API Level 19+)

Installation
------------

To install add our Maven repo to the root build.gradle file and add it as a dependency to your app
module's build.gradle along with the Gini Capture SDK.

build.gradle:

```
repositories {
    maven {
        url 'https://repo.gini.net/nexus/content/repositories/open
    }
}
```

app/build.gradle:

```
dependencies {
    implementation 'net.gini:gini-capture-sdk:1.0.0-beta04'
    implementation 'net.gini:gini-capture-network-lib:1.0.0-beta04'
}
```

## License

Gini Capture SDK and the Gini Capture Network Library are available under a commercial license.
See the LICENSE file for more info.
