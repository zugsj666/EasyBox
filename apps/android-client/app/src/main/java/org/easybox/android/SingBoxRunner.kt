package org.easybox.android

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class SingBoxRunner(
    private val binary: File,
    private val config: File
) {
    private val executor = Executors.newSingleThreadExecutor()
    private val running = AtomicBoolean(false)
    private var process: Process? = null

    val isRunning: Boolean
        get() = running.get()

    fun start() {
        if (!running.compareAndSet(false, true)) {
            DemoLogStore.add("sing-box process is already running.")
            return
        }

        executor.execute {
            try {
                DemoLogStore.add("Starting sing-box process.")
                val command = listOf(binary.absolutePath, "run", "-c", config.absolutePath)
                process = ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .start()

                val currentProcess = process ?: return@execute
                BufferedReader(InputStreamReader(currentProcess.inputStream)).useLines { lines ->
                    lines.forEach { line ->
                        DemoLogStore.add("sing-box: $line")
                    }
                }

                val exitCode = currentProcess.waitFor()
                DemoLogStore.add("sing-box exited with code $exitCode.")
            } catch (error: Throwable) {
                DemoLogStore.add("sing-box start failed: ${error.message ?: error::class.java.simpleName}")
            } finally {
                running.set(false)
                process = null
            }
        }
    }

    fun stop() {
        if (!running.get()) {
            DemoLogStore.add("sing-box process is not running.")
            return
        }

        DemoLogStore.add("Stopping sing-box process.")
        process?.destroy()
        running.set(false)
    }
}

