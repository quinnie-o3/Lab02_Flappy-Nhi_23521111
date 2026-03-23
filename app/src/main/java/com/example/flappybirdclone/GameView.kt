package com.example.flappybirdclone

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class GameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val engine = GameEngine()
    private var birdBitmap: Bitmap? = null
    private val pipePaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }
    private val scorePaint = Paint().apply {
        color = Color.WHITE
        textSize = 80f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    init {
        // Load and scale the bird sprite resource
        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.bird_sprite)
        if (originalBitmap != null) {
            birdBitmap = Bitmap.createScaledBitmap(
                originalBitmap,
                engine.bird.width.toInt(),
                engine.bird.height.toInt(),
                true
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        engine.screenWidth = w
        engine.screenHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Set background to pink
        canvas.drawColor(Color.parseColor("#FFC0CB"))

        engine.update()

        // Draw bird
        birdBitmap?.let {
            canvas.drawBitmap(it, engine.bird.x.toFloat(), engine.bird.y.toFloat(), null)
        } ?: run {
            // Fallback: draw a yellow rectangle if bitmap is missing
            val paint = Paint().apply { color = Color.YELLOW }
            canvas.drawRect(
                engine.bird.x.toFloat(),
                engine.bird.y.toFloat(),
                (engine.bird.x + engine.bird.width).toFloat(),
                (engine.bird.y + engine.bird.height).toFloat(),
                paint
            )
        }

        // Draw pipes
        for (pipe in engine.pipes) {
            // Top pipe
            canvas.drawRect(
                pipe.x.toFloat(),
                0f,
                (pipe.x + pipe.width).toFloat(),
                pipe.topPipeHeight.toFloat(),
                pipePaint
            )
            // Bottom pipe
            canvas.drawRect(
                pipe.x.toFloat(),
                (pipe.topPipeHeight + pipe.gapHeight).toFloat(),
                (pipe.x + pipe.width).toFloat(),
                height.toFloat(),
                pipePaint
            )
        }

        // Draw Score
        canvas.drawText("Score: ${engine.score}", (width / 2).toFloat(), 150f, scorePaint)

        if (!engine.isGameOver) {
            invalidate()
        } else {
            // Game Over message
            val paint = Paint().apply {
                color = Color.RED
                textSize = 100f
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("GAME OVER", (width / 2).toFloat(), (height / 2).toFloat(), paint)
            canvas.drawText("Tap to Restart", (width / 2).toFloat(), (height / 2 + 120).toFloat(), paint.apply { textSize = 50f })
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            performClick()
            if (engine.isGameOver) {
                engine.reset()
                invalidate()
            } else {
                engine.bird.flap()
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
