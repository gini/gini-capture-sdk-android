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

### GVL 2.5.0 and older

In `MainActivity#startGiniVisionLibrary()` the `CameraActivity` is launched for result with an explicit `Intent`. The `ReviewActivity` and
`AnalysisActivity` subclasses are added as extras to the `Intent`. Uncomment the additional extras to enable customizations. For a complete
list of extras view the `CameraActivity`'s javadoc.

### GVL 3.0.0 and newer

Since GVL 3.0.0 there is no need to subclass the activities. You only need to configure and create a `GiniCapture` instance and start the 
`CameraActivity` for result.

ReviewActivity
--------------

### GVL 2.5.0 and older

The `ReviewActivity` handles the events from the Review Screen. As it is a subclass of the Gini Capture SDK's `ReviewActivity` you have
to declare it in your `AndroidManifest.xml`. We recommend setting the title to `gc_title_review`, limit the orientation to portrait and use
the `GiniCaptureTheme`

It starts analyzing the taken picture in `ReviewActivity#onShouldAnalyzeDocument()` while the user reviews the picture. If the picture was
rotated it cancels the analysis in `ReviewActivity#onDocumentWasRotated()`. If the analysis fails it calls
`ReviewActivity#onDocumentAnalysisError()` to notify the base class and facilitate showing the error in the `AnalysisActivity`.

When the document analysis successfully completed it calls `ReviewActivity#onDocumentAnalyzed()`. When the user clicks the next button
either `ReviewActivity#onAddDataToResult()` or `ReviewActivity#onProceedToAnalysisScreen()` is called.

The extractions result is added in `ReviewActivity#onAddDataToResult()` to the result `Intent` in a `Bundle`. This is only an example, you
may use any format that the `Intent` allows.

The table below shows you when one of those methods is called:

|Document was rotated|Analysis started|Analysis successful|Next button clicked|
|---|---|---|---|
|no|no|-|`ReviewActivity#onProceedToAnalysisScreen()`|
|no|yes|no|`ReviewActivity#onProceedToAnalysisScreen()`|
|no|yes|yes|`ReviewActivity#onAddDataToResult()`|
|yes|no|-|`ReviewActivity#onProceedToAnalysisScreen()`|
|yes|yes|no|`ReviewActivity#onProceedToAnalysisScreen()`|
|yes|yes|yes|`ReviewActivity#onProceedToAnalysisScreen()`|

The `ReviewActivity` finishes itself after data was added to the result.

### GVL 3.0.0 and newer

There is no need for subclassing the GVL's `ReviewActivity`. You only need to configure and create a `GiniCapture` instance before launching
the `CameraActivity` for result.

AnalysisActivity
----------------

### GVL 2.5.0 and older

The `AnalysisActivity` handles the Analysis Screen's events. As it is a subclass of the Gini Capture SDK's `AnalysisActivity` you have to
declare it in your `AndroidManifest.xml`. We recommend setting the title to `gc_title_analysis` and use the `GiniCaptureTheme`.

The analysis is started or resumed in `AnalysisActivity#onAnalyzeDocument()` (if an error message was given, this method is called only when
the user clicks the retry button). Notifying the base class about successful completion of the analysis is done similarly as in the
`ReviewActivity` with `AnalysisActivity#onDocumentAnalyzed()`.

The `AnalysisActivity#onAddDataToResult()` is called when the analysis was successful. Like in the `ReviewActivity` the extractions result
is added here to the result `Intent` in a `Bundle`. You may use any format that the `Intent` allows.
 
The `AnalysisActivity` finishes itself after data was added to the result.
 
An error message is displayed, if the analysis fails with `AnalysisActivity#showError()`. The activity indicator can be started and stopped
with `AnalysisActivity#startScanAnimation()` and `AnalysisActivity#stopScanAnimation()`.

### GVL 3.0.0 and newer

There is no need for subclassing the GVL's `AnalysisActivity`. You only need to configure and create a `GiniCapture` instance before
launching the `CameraActivity` for result.

ExtractionsActivity
-------------------

### GVL 2.5.0 and older

Displays the extractions with the possibility to send feedback to the Gini API. It only shows the extractions needed for transactions, we
call them the Pay5: payment recipient, IBAN, BIC, amount and payment reference.

Feedback should be sent only for the user visible fields. Other fields should be filtered out.

### GVL 4.0.0 and newer

In addition, if the return assistant was enabled when the `GiniCapture` instance was created and your client id is configured to extract line
items and all the required line item information was successfully extracted, then it will show the line items updated according to the
changes the user did in the return assistant. The amount to pay field is also updated to contain the total price of the selected line items.

NoExtractionsActivity
---------------------

Displays tips to the user, if no Pay5 extractions were found. 

We recommend implementing a similar screen to aid the user in the taking better pictures and improve the quality of the extractions.

SingleDocumentAnalyzer
----------------------

### GVL 2.5.0 and older

Helps with managing the document analysis using our Gini API SDK. 

Analysis can be started in the `ReviewActivity` and resumed in the `AnalysisActivity` or cancelled in the `ReviewActivity` and started anew
in the `AnalysisActivity`. Therefore the `SingleDocumentAnalyzer` allows only one document to be analyzed. Analyzing a new one, requires the
old one to be cancelled (even, if analysis was completed).

### GVL 3.0.0 and newer

Is not used, because when configuring and creating a `GiniCapture` instance we use the default networking plugin which takes care of
communicating with the Gini API.

Gini API SDK
============

### GVL 2.5.0 and older

The Gini API SDK is created in and accessed using the `ScreenApiApp`. The `SingleDocumentAnalyzer` helps with managing document analysis.

### GVL 3.0.0 and newer

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
 