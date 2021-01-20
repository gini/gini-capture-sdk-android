Screen API Example App
=========================

This example app provides you with a sample usage of the Gini Capture SDK's Screen API.

The Gini API SDK is used for analyzing documents and sending feedback.

The `ReviewActivity` and `AnalysisActivity` extend the activities with the same name from the Gini Capture SDK.

Before analyzing documents with the Component API example app, you need to set your Gini API Client Id and Secret in the
`src/main/res/values/gini_api_credentials.xml`.

Please note, that large heap is enabled for the example app. Your application using the Gini Capture SDK should also enable large heap to
make sure, that there is enough memory for image handling.

Overview
========

The entry point of the app is the `MainActivity`. It starts the Gini Capture SDK and handles the result.

You only need to configure and create a `GiniCapture` instance and start the 
`CameraActivity` for result.

ReviewActivity
--------------

You only need to configure and create a `GiniCapture` instance before launching
the `CameraActivity` for result.

AnalysisActivity
----------------

You only need to configure and create a `GiniCapture` instance before
launching the `CameraActivity` for result.

ExtractionsActivity
-------------------

Displays the extractions with the possibility to send feedback to the Gini API. It only shows the extractions needed for transactions, we
call them the Pay5: payment recipient, IBAN, BIC, amount and payment reference.

Feedback should be sent only for the user visible fields. Other fields should be filtered out.

NoExtractionsActivity
---------------------

Displays tips to the user, if no Pay5 extractions were found. 

We recommend implementing a similar screen to aid the user in the taking better pictures and improve the quality of the extractions.

SingleDocumentAnalyzer
----------------------

Is not used, because when configuring and creating a `GiniCapture` instance we use the default networking plugin which takes care of
communicating with the Gini API.

Gini API SDK
============

The Gini API SDK is not used directly. The default networking plugin, which was used when configuring and creating a `GiniCapture` instance,
takes care of communicating with the Gini API.

Customization
=============

Customization options are detailed in each Screen API Activity's javadoc: `CameraActivity`, `HelpActivity`, `OnboardingActivity`,
`ReviewActivity` and `AnalysisActivity`.

To experiment with customizing the images used in the Gini Capture SDK you can copy the contents of the folder
`screenapiexample/customized-drawables` to `screenapiexample/src/main/res`.

Text customizations can be tried out by uncommenting and modifying the string resources in the
`screenapiexample/src/main/res/values/strings.xml`.

Text styles and fonts can be customized by uncommenting and altering the styles in the `screenapiexample/src/main/res/values/styles.xml`

To customize the colors you can uncomment and modify the color resources in the `screenapiexample/src/main/res/values/colors.xml`.

Customizing the opacity of the onboarding pages' background you can uncomment and modify the string resource in the
`screenapiexample/src/main/res/values/config.xml`.

ProGuard 
======== 
 
A sample ProGuard configuration file is included in the Screen API example app's directory called `proguard-rules.pro`. 
 
The release build is configured to run ProGuard. You need a keystore with a key to sign it. Create a keystore with a key and provide them in
the `gradle.properties` or as arguments for the build command:

``` 
$ ./gradlew screenapiexample::assembleRelease \ 
    -PreleaseKeystoreFile=<path to keystore> \ 
    -PreleaseKeystorePassword=<keystore password> \ 
    -PreleaseKeyAlias=<key alias> \ 
    -PreleaseKeyPassword=<key password> \
    -PclientId=<Gini API client id> \
    -PclientSecret=<Gini API client secret>
``` 
 