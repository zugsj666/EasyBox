package org.easybox.android

import android.Manifest
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

class MainActivity : ComponentActivity() {
    private lateinit var logView: TextView

    private val vpnPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val permissionIntent = VpnService.prepare(this)
            if (permissionIntent == null) {
                DemoLogStore.add("VPN permission granted.")
                EasyBoxVpnService.start(this)
            } else {
                DemoLogStore.add("VPN permission was not granted.")
            }
        }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                DemoLogStore.add("Notification permission granted.")
            } else {
                DemoLogStore.add("Notification permission denied. Foreground service still starts, but notification visibility may be limited.")
            }
            requestVpnAndStart()
        }

    private val logListener: (String) -> Unit = { text ->
        logView.text = text
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(buildContentView())
    }

    override fun onStart() {
        super.onStart()
        DemoLogStore.addListener(logListener)
    }

    override fun onStop() {
        DemoLogStore.removeListener(logListener)
        super.onStop()
    }

    private fun buildContentView(): LinearLayout {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 40, 32, 32)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val title = TextView(this).apply {
            text = "EasyBox Android Demo"
            textSize = 22f
        }

        val note = TextView(this).apply {
            text = "Phase 1 technical verification only: VPN permission, foreground service, local config, sing-box process, logs."
            textSize = 14f
            setPadding(0, 16, 0, 16)
        }

        val startButton = Button(this).apply {
            text = "Start Demo"
            setOnClickListener { requestNotificationThenStart() }
        }

        val stopButton = Button(this).apply {
            text = "Stop Demo"
            setOnClickListener { EasyBoxVpnService.stop(this@MainActivity) }
        }

        val vpnSettingsButton = Button(this).apply {
            text = "Open VPN Settings"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_VPN_SETTINGS))
            }
        }

        logView = TextView(this).apply {
            textSize = 12f
            setTextIsSelectable(true)
        }

        val scroll = ScrollView(this).apply {
            addView(logView)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            foregroundGravity = Gravity.BOTTOM
        }

        root.addView(title)
        root.addView(note)
        root.addView(startButton)
        root.addView(stopButton)
        root.addView(vpnSettingsButton)
        root.addView(scroll)
        return root
    }

    private fun requestNotificationThenStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }

        requestVpnAndStart()
    }

    private fun requestVpnAndStart() {
        val permissionIntent = VpnService.prepare(this)
        if (permissionIntent != null) {
            DemoLogStore.add("Requesting VPN permission.")
            vpnPermissionLauncher.launch(permissionIntent)
            return
        }

        DemoLogStore.add("VPN permission already granted.")
        EasyBoxVpnService.start(this)
    }
}

