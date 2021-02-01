# Package net.gini.android.capture.review

Contains the Activity and Fragments used for the Review Screen.

## Screen API

Extending the `ReviewActivity` in your application has been deprecated. The preferred way of adding network calls to the Gini Capture SDK
is by creating a [net.gini.android.capture.GiniCapture] instance with a [net.gini.android.capture.network.GiniCaptureNetworkService] and a
[net.gini.android.capture.network.GiniCaptureNetworkApi] implementation.

**Note:** [net.gini.android.capture.review.ReviewActivity] extends [androidx.appcompat.app.AppCompatActivity], therefore you have to use an
`AppCompat` theme for your [net.gini.android.capture.review.ReviewActivity] subclass.

## Component API

To use the Component API you have to include the [net.gini.android.capture.review.ReviewFragmentCompat] 
in an Activity in your app (a dedicated Activity is recommended). To receive events from the Fragments 
your Activity must implement the [net.gini.android.capture.review.ReviewFragmentListener] interface.

