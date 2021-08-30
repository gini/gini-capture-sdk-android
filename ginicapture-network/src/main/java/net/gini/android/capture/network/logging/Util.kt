package net.gini.android.capture.network.logging

import com.android.volley.VolleyError
import net.gini.android.capture.logging.ErrorLog
import java.nio.charset.Charset


fun <T : Exception> errorLogFromException(description: String, exception: T): ErrorLog =
    if (exception is VolleyError) {
        ErrorLog(
            description = "$description\n${exception.responseDetails()}",
            exception = exception
        )
    } else {
        ErrorLog(description = description, exception = exception)
    }

fun VolleyError.responseDetails(): String =
    this.networkResponse?.let { response ->
        val statusCode = response.statusCode
        val headers = response.allHeaders?.toList()?.joinToString("\n") { "${it.name}: ${it.value}" } ?: ""
        val body = response.data?.let { String(it, Charset.forName("UTF-8")) } ?: ""
        return "Status code: $statusCode\nHeaders:\n$headers\nBody:\n$body"
    } ?: this.message ?: toString()
