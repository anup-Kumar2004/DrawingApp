package com.example.drawingapp

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import java.io.OutputStream
import androidx.core.graphics.createBitmap
import androidx.core.view.isVisible
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private lateinit var backgroundImage: ImageView
    private lateinit var frameLayout: FrameLayout
    private lateinit var bottomBar: LinearLayout
    private lateinit var colorPalette: LinearLayout

    private lateinit var deleteBgBtn: ImageButton

    private lateinit var brushSizeBtn: ImageButton
    private lateinit var saveBtn: ImageButton
    private lateinit var galleryBtn: ImageButton
    private lateinit var undoBtn: ImageButton
    private lateinit var redoBtn: ImageButton

    private lateinit var pinkItem: FrameLayout
    private lateinit var redItem: FrameLayout
    private lateinit var greenItem: FrameLayout
    private lateinit var blueItem: FrameLayout
    private lateinit var customItem: FrameLayout

    private lateinit var selectorPink: View
    private lateinit var selectorRed: View
    private lateinit var selectorGreen: View
    private lateinit var selectorBlue: View
    private lateinit var selectorCustom: View

    private var isImageLoaded = false
    private var currentBrushColor = Color.BLACK
    private var currentBrushSize = 10

// ---------------- IMAGE PICKERS ----------------

    // ✅ Modern picker (Android 13+)
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                backgroundImage.setImageURI(it)
                isImageLoaded = true
                showDeleteButton()
            }
        }

    // ✅ Legacy picker (Android 12 and below)
    private val legacyPickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                backgroundImage.setImageURI(it)
                isImageLoaded = true
                showDeleteButton()
            }
        }

    // ✅ Permission launcher (only for old Android)
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                legacyPickerLauncher.launch("image/*")
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ Force light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        drawingView = findViewById(R.id.drawing_view)
        backgroundImage = findViewById(R.id.background_image)
        frameLayout = findViewById(R.id.frame_layout)
        bottomBar = findViewById(R.id.bottom_bar)

        brushSizeBtn = findViewById(R.id.btn_brush_size)
        saveBtn = findViewById(R.id.btn_save)
        galleryBtn = findViewById(R.id.btn_gallery)
        undoBtn = findViewById(R.id.btn_undo)
        redoBtn = findViewById(R.id.btn_redo)
        deleteBgBtn = findViewById(R.id.btn_delete_bg)

        colorPalette = findViewById(R.id.color_palette)

        pinkItem = findViewById(R.id.item_pink)
        redItem = findViewById(R.id.item_red)
        greenItem = findViewById(R.id.item_green)
        blueItem = findViewById(R.id.item_blue)
        customItem = findViewById(R.id.item_custom)

        selectorPink = findViewById(R.id.selector_pink)
        selectorRed = findViewById(R.id.selector_red)
        selectorGreen = findViewById(R.id.selector_green)
        selectorBlue = findViewById(R.id.selector_blue)
        selectorCustom = findViewById(R.id.selector_custom)

        setupColors()
        setupButtons()
    }

// ---------------- BUTTONS ----------------

    private fun setupButtons() {

        brushSizeBtn.setOnClickListener { showBrushSizeBottomSheet() }

        saveBtn.setOnClickListener { saveDrawing() }

        galleryBtn.setOnClickListener { openGallery() }

        undoBtn.setOnClickListener {
            if (!drawingView.undo()) {
                Toast.makeText(this, "Nothing to undo", Toast.LENGTH_SHORT).show()
            }
        }

        redoBtn.setOnClickListener {
            if (!drawingView.redo()) {
                Toast.makeText(this, "Nothing to redo", Toast.LENGTH_SHORT).show()
            }
        }

        deleteBgBtn.setOnClickListener { showDeleteConfirmation() }
    }

// ---------------- GALLERY LOGIC ----------------

    private fun openGallery() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // ✅ Android 13+ (NO permission needed)
            imagePickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            // ⚠️ Old Android → permission required
            val permission = Manifest.permission.READ_EXTERNAL_STORAGE

            if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED
            ) {
                legacyPickerLauncher.launch("image/*")
            } else {
                permissionLauncher.launch(permission)
            }
        }
    }

// ---------------- SAVE ----------------

    private fun saveDrawing() {

        val bitmap = createBitmap(frameLayout.width, frameLayout.height)
        val canvas = Canvas(bitmap)
        frameLayout.draw(canvas)

        val filename = "Drawing_${System.currentTimeMillis()}.png"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/DrawingApp")
        }

        val uri: Uri? = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )

        try {
            uri?.let {
                val stream: OutputStream? = contentResolver.openOutputStream(it)
                stream?.use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                Toast.makeText(this, "Saved to Gallery 🎉", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show()
        }
    }

// ---------------- DELETE ----------------

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Remove Background")
            .setMessage("Do you want to remove the background image?")
            .setPositiveButton("Yes") { _, _ -> removeBackgroundImage() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun removeBackgroundImage() {
        backgroundImage.setImageDrawable(null)
        isImageLoaded = false
        hideDeleteButton()
    }

    private fun showDeleteButton() {
        deleteBgBtn.visibility = View.VISIBLE
        updateWeights()
    }

    private fun hideDeleteButton() {
        deleteBgBtn.visibility = View.GONE
        updateWeights()
    }

    private fun updateWeights() {
        for (i in 0 until bottomBar.childCount) {
            val child = bottomBar.getChildAt(i)
            val params = child.layoutParams as LinearLayout.LayoutParams
            if (child.isVisible) params.weight = 1f
            child.layoutParams = params
        }
    }

// ---------------- BRUSH ----------------

    private fun showBrushSizeBottomSheet() {

        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_brush_size, frameLayout, false)

        val seekBar = view.findViewById<SeekBar>(R.id.seekBarBrush)
        val preview = view.findViewById<View>(R.id.brush_preview)
        val sizeText = view.findViewById<TextView>(R.id.brush_size_text)

        seekBar.progress = currentBrushSize

        fun updatePreview(progress: Int) {
            val size = progress + 5
            drawingView.setBrushSize(size.toFloat())

            val params = preview.layoutParams
            params.width = size * 2
            params.height = size * 2
            preview.layoutParams = params

            preview.background = android.graphics.drawable.GradientDrawable().apply {
                shape = android.graphics.drawable.GradientDrawable.OVAL
                setColor(currentBrushColor)
            }

            sizeText.text = getString(R.string.brush_size, size)
        }

        updatePreview(currentBrushSize)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentBrushSize = progress
                updatePreview(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

// ---------------- COLORS ----------------

    private fun setupColors() {

        pinkItem.setOnClickListener {
            currentBrushColor = "#FF69B4".toColorInt()
            drawingView.setColor(currentBrushColor)
            resetSelectors()
            selectorPink.visibility = View.VISIBLE
        }

        redItem.setOnClickListener {
            currentBrushColor = Color.RED
            drawingView.setColor(currentBrushColor)
            resetSelectors()
            selectorRed.visibility = View.VISIBLE
        }

        greenItem.setOnClickListener {
            currentBrushColor = Color.GREEN
            drawingView.setColor(currentBrushColor)
            resetSelectors()
            selectorGreen.visibility = View.VISIBLE
        }

        blueItem.setOnClickListener {
            currentBrushColor = Color.BLUE
            drawingView.setColor(currentBrushColor)
            resetSelectors()
            selectorBlue.visibility = View.VISIBLE
        }

        customItem.setOnClickListener {

            ColorPickerDialog.Builder(this)
                .setTitle("Pick Color")

                // ✅ THIS SAVES + RESTORES LAST COLOR
                .setPreferenceName("MyColorPicker")

                .setPositiveButton("OK",
                    ColorEnvelopeListener { envelope, _ ->

                        var selectedColor = envelope.color

                        // 🔥 FIX: avoid pure black (edge case)
                        if (selectedColor == Color.BLACK) {
                            selectedColor = "#010101".toColorInt()
                        } else if (selectedColor == Color.WHITE) {
                            selectedColor = "#FEFEFE".toColorInt()
                        }

                        currentBrushColor = selectedColor
                        drawingView.setColor(currentBrushColor)

                        drawingView.setColor(currentBrushColor)

                        resetSelectors()
                        selectorCustom.visibility = View.VISIBLE
                    })

                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .show()
        }
    }

    private fun resetSelectors() {
        selectorPink.visibility = View.GONE
        selectorRed.visibility = View.GONE
        selectorGreen.visibility = View.GONE
        selectorBlue.visibility = View.GONE
        selectorCustom.visibility = View.GONE
    }

}
