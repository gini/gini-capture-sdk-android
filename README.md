![Gini Capture SDK for Android](GiniCapture_Logo.png)

Gini Capture SDK for Android
============================

**Deprecation Notice**
----------------------

Development of the Gini Capture SDK for Android will be continued in a 
[new repository](https://github.com/gini/gini-mobile-android/tree/main/capture-sdk).

No new versions will be released from this repository and we kindly ask you to update to the 
[version](https://github.com/gini/gini-mobile-android/releases/tag/capture-sdk%3B1.5.0) released from the new repository.

A few breaking changes were necessary, but these are easy to fix. You can find the steps in this 
[migration guide](https://github.com/gini/gini-mobile-android/blob/bank-sdk%3B1.4.0/capture-sdk/migrate-from-other-capture-sdk.md).

Introduction
------------

The Gini Capture SDK for Android provides Activities and Fragments to capture documents and prepare them for upload to
the Gini Pay API. It also allows documents to be imported from other apps. The captured images can be reviewed and are
optimized on the device to provide the best results when used with the Gini Pay API. 

The Gini Pay API provides an information extraction service for analyzing documents (e.g. invoices or contracts).
Specifically it extracts information such as the document sender or the payment relevant information (amount to pay,
IBAN, BIC, payment reference, etc.) in an invoice.

Documentation
-------------

* [Integration Guide](http://developer.gini.net/gini-capture-sdk-android/html/) and
* [Gini Capture Reference Docs](http://developer.gini.net/gini-capture-sdk-android/ginicapture/dokka/index.html)
* [Network Library Reference Docs](http://developer.gini.net/gini-capture-sdk-android/network/javadoc/index.html)

Examples
--------

We are providing example apps for the Screen API and the Component API. These apps demonstrate how
to integrate the Gini Capture SDK and how to use it with the Gini Capture Network Library to
analyze photos of documents.

We also provide a separate standalone [example
app](https://github.com/gini/gini-vision-lib-android-example). This is more like a real world app
and serves as an additional help for you to discover how the Gini Capture SDK (and the Gini
Capture Network Library) should be used.

License
-------

Gini Capture SDK and the Gini Capture Network Library are available under a commercial license.
See the LICENSE file for more info.
