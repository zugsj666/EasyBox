package org.easybox.android

import android.os.Handler
import android.os.Looper
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CopyOnWriteArrayList

object DemoLogStore {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    private val listeners = CopyOnWriteArrayList<(String) -> Unit>()
    private val mainHandler = Handler(Looper.getMainLooper())

    private var text: String = "EasyBox Android demo ready.\n"

    fun current(): String = text

    fun add(message: String) {
        val line = "[${LocalTime.now().format(formatter)}] $message"
        text = (text + line + "\n").takeLast(16_000)
        mainHandler.post {
            listeners.forEach { it(text) }
        }
    }

    fun addListener(listener: (String) -> Unit) {
        listeners += listener
        listener(text)
    }

    fun removeListener(listener: (String) -> Unit) {
        listeners -= listener
    }
}

