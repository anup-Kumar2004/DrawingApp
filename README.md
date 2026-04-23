<div align="center">

# 🎨 DrawingApp

### *A smooth, interactive drawing experience — built for creativity on Android*

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/Min%20API-21%20(Android%205)-orange?style=for-the-badge)](https://developer.android.com)
[![Architecture](https://img.shields.io/badge/Architecture-Custom%20View-blue?style=for-the-badge)](https://developer.android.com)

<br/>

> *A lightweight drawing app with real-time strokes, color selection, and undo/redo support*

<br/>

---

## 📥 Download & Install

### Try the app directly on your Android device!

<br/>

[![Download APK](https://img.shields.io/badge/⬇️%20DOWNLOAD%20APK-Click%20Here%20to%20Install-2ea44f?style=for-the-badge&logoColor=white)](https://github.com/anup-Kumar2004/DrawingApp/releases/latest/download/app-release.apk)

<br/>

> ⚠️ You may need to allow installation from unknown sources.
> Go to **Settings → Security → Install unknown apps**

<br/>

| Step | Action |
|:----:|--------|
| **1** | 📲 Tap the **Download APK** button above on your Android phone |
| **2** | 📂 Open the downloaded file from your notifications or Downloads folder |
| **3** | ✅ Tap **Install** when prompted |
| **4** | 🎨 Start drawing! |

<br/>

**Requirements:** Android 5.0 (API 21) or higher · Lightweight · No account needed

---

</div>

## 📱 Screenshots

> *Add your screenshots here by replacing the placeholder paths below*

<div align="center">

| Drawing Canvas | Color Selection | Brush Size Control | Undo / Redo |
|:--------------:|:---------------:|:------------------:|:-----------:|
| <img src="screenshots/canvas.jpeg" width="160"/> | <img src="screenshots/colors.jpeg" width="160"/> | <img src="screenshots/brush.jpeg" width="160"/> | <img src="screenshots/undo_redo.jpeg" width="160"/> |

</div>

<br/>

---

## ✨ Features

<table>
<tr>
<td width="50%">

### 🎨 Drawing Experience
- Smooth real-time drawing on canvas
- Custom **Path-based rendering**
- Anti-aliased strokes for clean output
- Supports continuous finger gestures

</td>
<td width="50%">

### 🌈 Color Selection
- Predefined colors (Pink, Red, Green, Blue)
- Custom color picker with full spectrum
- Visual selection indicator

</td>
</tr>
<tr>
<td width="50%">

### ✏️ Brush Control
- Adjustable brush size via **SeekBar dialog**
- Dynamic stroke width updates in real-time

</td>
<td width="50%">

### 🔁 Undo / Redo
- Multi-step undo support
- Redo previously undone strokes
- Efficient stroke stack handling

</td>
</tr>
<tr>
<td width="50%">

### 🖼️ Background Support
- Set image as drawing background
- Draw on top of gallery images

</td>
<td width="50%">

### ⚡ Lightweight & Fast
- No heavy dependencies
- Optimized drawing performance
- Runs smoothly on low-end devices

</td>
</tr>
</table>

<br/>

---

## 🏗️ Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Kotlin |
| **UI** | XML Layouts, ConstraintLayout |
| **Custom Drawing** | Canvas, Paint, Path |
| **Architecture** | Custom View-based approach |
| **Dialogs** | Android Dialog + SeekBar |
| **Color Picker** | Skydoves ColorPickerView |

<br/>

---

## 🗂️ Project Structure

```
app/src/main/
│
├── java/com/example/drawingapp/
│   ├── DrawingView.kt               # Custom View — canvas, paint, path logic
│   └── MainActivity.kt              # Main screen — toolbar, color, brush controls
│
├── res/
│   ├── layout/
│   │   ├── activity_main.xml
│   │   └── dialog_brush_size.xml
│   │
│   ├── drawable/
│   │   ├── circle_* files           # Color circle drawables
│   │   ├── circle_selector.xml      # Selection state drawable
│   │   ├── color_wheel.png
│   │   └── icons/                   # undo, redo, save, gallery icons
│   │
│   ├── values/
│   │   ├── colors.xml
│   │   ├── strings.xml
│   │   └── themes.xml
│   │
│   └── xml/
│       ├── backup_rules.xml
│       └── data_extraction_rules.xml
│
└── AndroidManifest.xml
```

<br/>

---

## 🚀 Getting Started

### Prerequisites
- Android Studio **Hedgehog** or later
- Android device / emulator running **API 21+**

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/anup-Kumar2004/DrawingApp.git

# 2. Open in Android Studio
# File → Open → select the cloned folder

# 3. Let Gradle sync complete

# 4. Run on your device or emulator
# Click ▶ Run or press Shift+F10
```

<br/>

---

## 📦 Dependencies

```kotlin
// Color Picker
implementation("com.github.skydoves:colorpickerview:2.3.0")

// Material 3
implementation("com.google.android.material:material:1.11.0")
```

<br/>

---

## 🧠 Architecture Overview

```
User Touch → MotionEvent → Path → Canvas Draw
                               ↓
                       Stored as Stroke
                               ↓
                   Undo / Redo Stack System
                               ↓
                       View.invalidate()
```

<br/>

---

## 🙋‍♂️ About the Developer

<div align="center">

Built with ❤️ by **Anup Kumar**

*This project demonstrates custom Android View development including:*
*Canvas drawing · Paint & Path API · gesture handling · undo/redo stack logic*

<br/>

[![GitHub](https://img.shields.io/badge/GitHub-Follow-181717?style=for-the-badge&logo=github)](https://github.com/anup-Kumar2004)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-0A66C2?style=for-the-badge&logo=linkedin)](https://linkedin.com/in/YOUR_LINKEDIN)

<br/>

*If you found this project helpful or interesting, please consider giving it a ⭐*

**© 2026 DrawingApp — Built with Kotlin & ☕**

</div>
