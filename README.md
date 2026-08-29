# Kodak Floating Dock

Minimal Android 8.1 overlay for the Kodak RWF-108H photo frame.

- One bottom-right semi-transparent toggle button
- Tap once to switch from Kodak to LocalSend
- Tap again to switch back to Kodak
- The icon shows the app that will open next
- Drag the button to reposition it
- Restarts after boot
- No network, storage, phone, accessibility, or root permission

## Install

Build the debug APK with the included GitHub Actions workflow, then run:

    adb install -r app-debug.apk
    adb shell appops set com.alphalee.kodakdock SYSTEM_ALERT_WINDOW allow
    adb shell monkey -p com.alphalee.kodakdock -c android.intent.category.LAUNCHER 1
