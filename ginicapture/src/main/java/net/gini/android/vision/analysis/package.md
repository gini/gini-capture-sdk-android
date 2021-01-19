# Package net.gini.android.vision.analysis

Contains the Activity and Fragments used for the Analysis Screen.

## Screen API

Extending the `AnalysisActivity` in your application has been deprecated. The preferred way of adding network calls to the Gini Capture
SDK is by creating a [net.gini.android.vision.GiniCapture] instance with a [net.gini.android.vision.network.GiniCaptureNetworkService] and
a [net.gini.android.vision.network.GiniCaptureNetworkApi] implementation.

**Note:** [net.gini.android.vision.analysis.AnalysisActivity] extends [androidx.appcompat.app.AppCompatActivity], therefore you have to use
*an `AppCompat` theme for your [net.gini.android.vision.analysis.AnalysisActivity] subclass.

## Component API

To use the Component API you have to include the [net.gini.android.vision.analysis.AnalysisFragmentStandard] or the
[net.gini.android.vision.analysis.AnalysisFragmentCompat] in an Activity in your app (a dedicated Activity is recommended). To receive
events from the Fragments your Activity must implement the [net.gini.android.vision.analysis.AnalysisFragmentListener] interface.
