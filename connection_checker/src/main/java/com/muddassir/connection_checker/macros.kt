package com.muddassir.connection_checker

import android.content.Context
import android.os.Handler
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

internal fun delay(millis: Long, task: ((Unit) -> Unit)) {
    Handler().postDelayed({ task.invoke(Unit) }, millis)
}

internal fun pingUrl(context: Context, url: String,
                     onResult: ((failed: Boolean, timeTaken: Long) -> Unit)) {
    val queue: RequestQueue = Volley.newRequestQueue(context)

    val startTime = System.currentTimeMillis()

    val request = StringRequest(Request.Method.GET, url,
        Response.Listener<String> {
            onResult.invoke(false, System.currentTimeMillis() - startTime)
        }, Response.ErrorListener {
            onResult.invoke(true, System.currentTimeMillis() - startTime)
        })

    queue.add(request)
}