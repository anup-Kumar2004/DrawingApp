<div align="center">

<img src="screenshots/banner.jpeg" alt="DrawingApp Banner" width="100%"/>

<br/>

# ✏️ &nbsp; Drawing App

**Draw. Color. Create. Save.**

*A zero-dependency, fully custom Android drawing canvas — built from scratch using Kotlin and the Android Canvas API.*

<br/>

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white)
![Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?style=flat-square)
![No API Key](https://img.shields.io/badge/API%20Key-Not%20Required-brightgreen?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)

</div>

---

<div align="center">

## 📲 &nbsp; Get the App

*Scan or tap — works on any Android phone*

<br/>

[![GET APK](https://img.shields.io/badge/──────────%20⬇%20%20DOWNLOAD%20APK%20v1.0%20%20⬇%20──────────-28a745?style=for-the-badge&logo=android&logoColor=white)](https://github.com/anup-Kumar2004/DrawingApp/releases/latest/download/app-release.apk)

<br/>

```
① Tap the button above on your Android phone
② Open the downloaded file from your notifications  
③ If prompted → Settings → Security → Allow unknown sources
④ Tap Install → Open → Start creating 🎨
```

*Requires Android 5.0 or above · Size ~4MB · No sign-up needed*

</div>

---

## 📸 &nbsp; Screenshots

<div align="center">

| Canvas | Colors | Brush Size | Background |
|:------:|:------:|:----------:|:----------:|
| <img src="screenshots/canvas.jpeg" width="160"/> | <img src="screenshots/colors.jpeg" width="160"/> | <img src="screenshots/brush.jpeg" width="160"/> | <img src="screenshots/undo_redo.jpeg" width="160"/> |

</div>

---

## 💡 &nbsp; What Makes This Different

> Most drawing apps use third-party canvas libraries.
> **This one doesn't.**

Every stroke you draw goes through a custom `DrawingView` built directly on Android's `Canvas`, `Paint`, and `Path` APIs — no shortcuts, no wrappers.

```kotlin
// Every finger movement creates a real Path — tracked live
MotionEvent.ACTION_MOVE -> {
    currentPath.lineTo(x, y)
    invalidate() // re-renders canvas in real time
}

// On finger lift — snapshot the stroke with its paint settings
MotionEvent.ACTION_UP -> {
    strokes.add(Stroke(currentPath, Paint(drawPaint)))
}
```

---

## ✨ &nbsp; Features

| | Feature | Details |
|--|---------|---------|
| 🖌️ | **Freehand Drawing** | Smooth anti-aliased strokes via custom `Path` rendering |
| 🎨 | **Color Palette** | Pink, Red, Green, Blue + full-spectrum custom color picker |
| 📏 | **Brush Size** | SeekBar-based bottom sheet — live circle preview updates as you drag |
| ↩️ | **Undo** | Pops the last stroke off a `MutableList<Stroke>` stack |
| ↪️ | **Redo** | Re-applies strokes from a separate undone strokes stack |
| 🖼️ | **Background Image** | Pick any photo from gallery and draw on top of it |
| 🗑️ | **Remove Background** | Confirmation dialog before clearing the background |
| 💾 | **Save to Gallery** | Flattens canvas + background into a `Bitmap` → saves to `Pictures/DrawingApp` |
| 🌈 | **Custom Color Picker** | Powered by Skydoves ColorPickerView with alpha + brightness sliders |
| 📱 | **Edge-to-Edge UI** | Full screen canvas with `enableEdgeToEdge()` |

---

## 🧱 &nbsp; How It's Built

### The Core — `DrawingView.kt`

The entire drawing engine is a single custom `View` subclass. No libraries, no delegates.

```
Finger Down  →  new Path()  →  moveTo(x, y)
Finger Move  →  lineTo(x, y)  →  invalidate()
Finger Up    →  Stroke(path, paint) saved to list
```

Each `Stroke` is a data class holding a **snapshot** of the path and paint at the time of drawing:

```kotlin
data class Stroke(val path: Path, val paint: Paint)
```

This makes undo/redo trivially simple — just move items between two lists.

### The Undo/Redo System

```
strokes list          ←→         undoneStrokes list
[s1, s2, s3]   ──undo──▶   [s1, s2]  +  undone:[s3]
[s1, s2]       ──redo───▶   [s1, s2, s3]  +  undone:[]
```

New stroke drawn? `undoneStrokes.clear()` — redo history is wiped, just like any pro drawing tool.

### Gallery & Permissions

```
Android 13+  →  Photo Picker API  (zero permissions needed)
Android 12-  →  READ_EXTERNAL_STORAGE permission → legacy picker
```

### Saving

Draws the entire `FrameLayout` (background image + strokes) onto a `Bitmap` using `frameLayout.draw(canvas)`, then writes it to `MediaStore` — no file path juggling, works on all modern Android versions.

---

## 🗂️ &nbsp; Project Structure

```
app/src/main/
│
├── java/com/example/drawingapp/
│   ├── DrawingView.kt        ← Custom canvas View (the heart of the app)
│   └── MainActivity.kt       ← UI wiring, toolbar, color, brush, save logic
│
├── res/
│   ├── layout/
│   │   ├── activity_main.xml         ← ConstraintLayout: canvas + palette + toolbar
│   │   └── dialog_brush_size.xml     ← Bottom sheet with SeekBar + live preview
│   │
│   ├── drawable/
│   │   ├── circle_pink/red/green/blue.xml   ← Color circle shapes
│   │   ├── circle_selector.xml              ← Selected state ring overlay
│   │   ├── circle_image_mask.xml            ← Circular mask for color wheel
│   │   ├── color_wheel.png                  ← Custom color picker thumbnail
│   │   ├── bg_canvas.xml                    ← Canvas background (rounded card)
│   │   └── icons/                           ← brush, save, gallery, undo, redo, delete
│   │
│   └── values/
│       ├── colors.xml
│       ├── strings.xml
│       └── themes.xml
│
└── AndroidManifest.xml
```

---

## 🚀 &nbsp; Run It Yourself

```bash
# Clone
git clone https://github.com/anup-Kumar2004/DrawingApp.git

# Open in Android Studio
# File → Open → select folder → let Gradle sync

# Run
# Press Shift+F10 or click ▶
```

**No API keys. No config. Just clone and run.**

---

## 📦 &nbsp; Dependencies

```kotlin
// Full-spectrum color picker with alpha + brightness sliders
implementation("com.github.skydoves:colorpickerview:2.3.0")

// Material 3 components (BottomSheetDialog, etc.)
implementation("com.google.android.material:material:1.11.0")
```

*Everything else — Canvas, Paint, Path, MediaStore, MotionEvent — is pure Android SDK.*

---

<div align="center">

---

Made with ❤️ by **Anup Kumar**

[![GitHub](https://img.shields.io/badge/GitHub-anup--Kumar2004-181717?style=flat-square&logo=github)](https://github.com/anup-Kumar2004)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-0A66C2?style=flat-square&logo=linkedin)](https://linkedin.com/in/YOUR_LINKEDIN)

*Drop a ⭐ if this project impressed you!*

</div>
