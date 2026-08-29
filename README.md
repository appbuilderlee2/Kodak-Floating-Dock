# Kodak Floating Dock

A tiny Android overlay made for the **Kodak RWF-108H** photo frame running Android 8.1.

為 **Kodak RWF-108H Android 8.1 相框**製作的輕量浮動切換按鈕。

## 中文說明

### 功能

- 首次啟動時顯示在畫面左下角
- 小型半透明圓形按鈕，減少遮擋相片
- 按一下由 Kodak 相框切換至 LocalSend
- 再按一下由 LocalSend 返回 Kodak 相框
- 圖示會顯示下一個將會開啟的程式
- 按住拖動可改變按鈕位置，位置會自動保存
- 相框重新開機後自動恢復浮動按鈕
- 不需要網絡、儲存空間、電話、Accessibility 或 Root 權限
- 使用固定的專案簽名，v1.1.1 之後的版本可以直接覆蓋更新

### 下載 APK

1. 開啟 [GitHub Actions](https://github.com/appbuilderlee2/Kodak-Floating-Dock/actions/workflows/build-apk.yml)。
2. 選擇最新一個成功的 **Build APK**。
3. 在頁面底部的 **Artifacts** 下載 `Kodak-Floating-Dock-APK`。
4. 解壓 ZIP，取得 `app-debug.apk`。

### 首次安裝

先連接相框的 ADB，然後執行：

```bash
adb install app-debug.apk
adb shell appops set com.alphalee.kodakdock SYSTEM_ALERT_WINDOW allow
adb shell monkey -p com.alphalee.kodakdock -c android.intent.category.LAUNCHER 1
```

如果使用無線 ADB，相框 IP 是 `192.168.1.121` 時：

```bash
adb connect 192.168.1.121:5555
```

### 更新版本

由 v1.1.1 或之後版本更新：

```bash
adb install -r app-debug.apk
adb shell monkey -p com.alphalee.kodakdock -c android.intent.category.LAUNCHER 1
```

如果安裝的是早期臨時簽名版本，首次更新可能需要先移除舊版：

```bash
adb uninstall com.alphalee.kodakdock
adb install app-debug.apk
adb shell appops set com.alphalee.kodakdock SYSTEM_ALERT_WINDOW allow
adb shell monkey -p com.alphalee.kodakdock -c android.intent.category.LAUNCHER 1
```

### 相關程式套件

- LocalSend：`org.localsend.localsend_app`
- Kodak 原裝相框：`com.cloudhearing.digital.kodakframe.android`
- Kodak Floating Dock：`com.alphalee.kodakdock`

## English

### Features

- Starts at the bottom-left corner
- Small translucent circular button that avoids covering photos
- Tap once to switch from Kodak to LocalSend
- Tap again to switch back to Kodak
- The icon shows which app will open next
- Drag to reposition; the position is saved automatically
- Restarts automatically after boot
- No network, storage, phone, accessibility, or root permission
- Uses a project-only signing key for in-place updates from v1.1.1 onward

### Install

Download the APK artifact from the latest successful
[Build APK workflow](https://github.com/appbuilderlee2/Kodak-Floating-Dock/actions/workflows/build-apk.yml), unzip it, then run:

```bash
adb install app-debug.apk
adb shell appops set com.alphalee.kodakdock SYSTEM_ALERT_WINDOW allow
adb shell monkey -p com.alphalee.kodakdock -c android.intent.category.LAUNCHER 1
```

### Update

```bash
adb install -r app-debug.apk
adb shell monkey -p com.alphalee.kodakdock -c android.intent.category.LAUNCHER 1
```

## Changelog

### v1.2.0

- Changed the default position to the bottom-left corner
- Reduced the button from 54 dp to 40 dp
- Replaced opaque application icons with custom transparent icons
- Added a subtle translucent circular background and border
- Idle opacity is 58%; the button becomes clearer while touched
- Existing installations reset to the new bottom-left default once

### v1.1.1

- Added a stable project signing key for future in-place updates

### v1.1.0

- Combined the separate LocalSend and Kodak buttons into one toggle button
- Moved the drag gesture onto the button itself

### v1.0.0

- Initial two-button floating dock

## Build from source

The included GitHub Actions workflow builds the Android debug APK automatically.
The project targets Android 8.1 (API 27) and does not require Android Studio for installation.
