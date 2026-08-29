# Kodak Floating Dock

Minimal Android 8.1 overlay for the Kodak RWF-108H photo frame.

- Bottom-right semi-transparent dock
- LocalSend button launches org.localsend.localsend_app
- Kodak button launches com.cloudhearing.digital.kodakframe.android
- Restarts after boot
- No network, storage, phone, accessibility, or root permission

## Install

Build the debug APK with the included GitHub Actions workflow, then run:

    adb install -r app-debug.apk
    adb shell appops set com.alphalee.kodakdock SYSTEM_ALERT_WINDOW allow
    adb shell monkey -p com.alphalee.kodakdock -c android.intent.category.LAUNCHER 1
