# EasyBox Android Technical Verification Demo

This directory contains the Phase 1 Android technical verification demo.

It is intentionally not the full EasyBox Android client. It does not contain login, remote config fetching, subscription import, node management, admin features, or the final product UI.

## Goal

Verify that the Android app can:

- Request `VpnService` permission
- Run a VPN service as a Foreground Service
- Show a persistent notification while connected
- Load a local sing-box config from app assets
- Start a local sing-box core process
- Stop the process
- Display basic logs and clear error messages

## Current sing-box Integration Method

The demo expects a sing-box executable to be packaged as an Android native library:

```text
app/src/main/jniLibs/arm64-v8a/libsing-box.so
app/src/main/jniLibs/armeabi-v7a/libsing-box.so
app/src/main/jniLibs/x86_64/libsing-box.so
```

At runtime, Android extracts native libraries into the app native library directory. The demo looks for `libsing-box.so`, copies `assets/sing-box-demo-config.json` to the app cache directory, then starts:

```text
libsing-box.so run -c <cached-config-path>
```

If the binary is missing, the app shows a clear error message in the log panel.

Note: a standalone sing-box process does not automatically receive the Android `VpnService` TUN file descriptor. This demo verifies the app-side lifecycle and local core process execution path first. Full packet routing integration may later require sing-box's Android library integration or an explicit TUN handoff design.

## How To Run

1. Install Android Studio and an Android SDK.
2. Put a compatible Android sing-box executable in the matching ABI folder under `app/src/main/jniLibs/`, renamed to `libsing-box.so`.
3. Open this directory in Android Studio:

   ```text
   apps/android-client
   ```

4. Build and run the `app` module on a device or emulator.
5. Tap `Start Demo`.
6. Grant the VPN permission when Android asks.
7. Confirm that:
   - A persistent notification appears.
   - The service starts.
   - sing-box logs appear in the log panel.
8. Tap `Stop Demo` to stop the service and sing-box process.

## Local Config

The local demo config is stored at:

```text
app/src/main/assets/sing-box-demo-config.json
```

It is deliberately local and static. The demo does not fetch config from an API.

## Not Included

This Phase 1 demo does not include:

- Login
- User profile
- Remote config API
- Subscription parsing
- Node management
- Management panel
- Final production UI
- Config secrecy guarantees

