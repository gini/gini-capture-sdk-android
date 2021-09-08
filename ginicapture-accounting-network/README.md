![Gini Capture SDK for Android](../GiniCapture_Logo.png)

Gini Capture Accounting Network Library for Android
==================================================

**Important:** This library has been deprecated. The information below is outdated and will not be updated.

Implementation using the Gini Accounting API of the network related tasks required by the Gini Capture SDK.

Adding this library along with the Gini Capture SDK to your application is the quickest way to integrate document
scanning with Gini's Accounting API.

In order for the Gini Capture SDK to use this networking library, pass the instances to the `GiniCapture.Builder`
when creating a new `GiniCapture`.

**Important:** You *must not* enable multi-page in the Gini Capture SDK when using it with this networking library.
The multi-page feature is currently only supported by the [default networking
library](https://github.com/gini/gini-capture-sdk-android/tree/master/ginicapture-network).

Documentation
-------------

* [Integration Guide](http://developer.gini.net/gini-capture-sdk-android/html/) and
* [Accounting Network Library Reference Docs](http://developer.gini.net/gini-capture-sdk-android/accounting/network/javadoc/index.html)

Example
-------

The example apps in the Gini Capture SDK demonstrate how to integrate the Gini Capture Accounting Network Library to
easily add document analysis to your app.

We also provide a separate standalone [example app](https://github.com/gini/gini-vision-lib-android-example). This is
more like a real world app and serves as an additional help for you to discover how the Gini Capture Accounting Network
Library along with the Gini Capture SDK should be used.

License
-------

Gini Capture SDK and the Gini Capture Network Library are available under a commercial license. See the LICENSE file
for more info.
