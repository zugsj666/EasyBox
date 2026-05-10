package org.easybox.android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import java.io.File

class EasyBoxVpnService : VpnService() {
    private var vpnInterface: ParcelFileDescriptor? = null
    private var runner: SingBoxRunner? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startDemo()
            ACTION_STOP -> stopDemo()
            else -> DemoLogStore.add("Ignoring unknown service action.")
        }
        return START_STICKY
    }

    override fun onDestroy() {
        stopDemo()
        super.onDestroy()
    }

    private fun startDemo() {
        if (runner?.isRunning == true) {
            DemoLogStore.add("Demo is already running.")
            return
        }

        try {
            startForeground(NOTIFICATION_ID, buildNotification("Starting sing-box demo..."))
            DemoLogStore.add("Foreground service started.")

            vpnInterface = Builder()
                .setSession("EasyBox sing-box demo")
                .addAddress("10.88.0.2", 32)
                .setMtu(1500)
                .establish()

            if (vpnInterface == null) {
                DemoLogStore.add("Failed to establish VPN interface.")
                stopSelf()
                return
            }

            DemoLogStore.add("VPN interface established.")

            val configFile = copyAssetConfig()
            val singBoxBinary = findSingBoxBinary()
            val processRunner = SingBoxRunner(singBoxBinary, configFile)
            runner = processRunner
            processRunner.start()

            updateNotification("sing-box demo running")
        } catch (error: Throwable) {
            DemoLogStore.add("Start failed: ${error.message ?: error::class.java.simpleName}")
            stopDemo()
        }
    }

    private fun stopDemo() {
        runner?.stop()
        runner = null

        try {
            vpnInterface?.close()
            if (vpnInterface != null) {
                DemoLogStore.add("VPN interface closed.")
            }
        } catch (error: Throwable) {
            DemoLogStore.add("Failed to close VPN interface: ${error.message}")
        } finally {
            vpnInterface = null
        }

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun copyAssetConfig(): File {
        val output = File(cacheDir, "sing-box-demo-config.json")
        assets.open("sing-box-demo-config.json").use { input ->
            output.outputStream().use { input.copyTo(it) }
        }
        DemoLogStore.add("Local config loaded: ${output.absolutePath}")
        return output
    }

    private fun findSingBoxBinary(): File {
        val binary = File(applicationInfo.nativeLibraryDir, "libsing-box.so")
        if (!binary.exists()) {
            throw IllegalStateException(
                "Missing sing-box binary. Add app/src/main/jniLibs/<abi>/libsing-box.so"
            )
        }
        if (!binary.canExecute()) {
            binary.setExecutable(true)
        }
        DemoLogStore.add("sing-box binary found: ${binary.absolutePath}")
        return binary
    }

    private fun buildNotification(text: String): Notification {
        ensureNotificationChannel()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("EasyBox Demo")
            .setContentText(text)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun updateNotification(text: String) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, buildNotification(text))
    }

    private fun ensureNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = getSystemService(NotificationManager::class.java)
        val existing = manager.getNotificationChannel(CHANNEL_ID)
        if (existing != null) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "EasyBox Demo VPN",
            NotificationManager.IMPORTANCE_LOW
        )
        manager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "easybox_demo_vpn"
        private const val NOTIFICATION_ID = 1001
        private const val ACTION_START = "org.easybox.android.action.START"
        private const val ACTION_STOP = "org.easybox.android.action.STOP"

        fun start(context: Context) {
            val intent = Intent(context, EasyBoxVpnService::class.java).setAction(ACTION_START)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, EasyBoxVpnService::class.java).setAction(ACTION_STOP)
            context.startService(intent)
        }
    }
}
