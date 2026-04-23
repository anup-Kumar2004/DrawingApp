package com.example.drawingapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

data class Stroke(val path: Path, val paint: Paint)

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val drawPaint = Paint()

    private val strokes = mutableListOf<Stroke>()
    private val undoneStrokes = mutableListOf<Stroke>()


    private var currentPath = Path()

    init {
        drawPaint.color = Color.BLACK
        drawPaint.strokeWidth = 8f
        drawPaint.style = Paint.Style.STROKE
        drawPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw completed strokes
        for (stroke in strokes) {
            canvas.drawPath(stroke.path, stroke.paint)
        }

        // Draw current stroke (live)
        canvas.drawPath(currentPath, drawPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x
        val y = event.y

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                currentPath = Path()
                currentPath.moveTo(x, y)

                undoneStrokes.clear()  // reset redo history
            }

            MotionEvent.ACTION_MOVE -> {
                currentPath.lineTo(x, y)
            }

            MotionEvent.ACTION_UP -> {
                val newPaint = Paint(drawPaint)
                strokes.add(Stroke(currentPath, newPaint))

                currentPath = Path() // reset
            }
        }

        invalidate()
        return true
    }

    fun setColor(newColor: Int) {
        drawPaint.color = newColor
    }

    fun undo(): Boolean {
        return if (strokes.isNotEmpty()) {

            val stroke = strokes.removeAt(strokes.size - 1)
            undoneStrokes.add(stroke)

            invalidate()
            true

        } else {
            false
        }
    }

    fun redo(): Boolean {
        return if (undoneStrokes.isNotEmpty()) {

            val stroke = undoneStrokes.removeAt(undoneStrokes.size - 1)
            strokes.add(stroke)

            invalidate()
            true

        } else {
            false
        }
    }

    fun setBrushSize(size: Float) {
        drawPaint.strokeWidth = size
    }


}