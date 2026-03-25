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
import androidx.core.graphics.scale
import androidx.core.graphics.toColorInt

class GameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val engine = GameEngine()
    private var birdBitmap: Bitmap? = null
    private val pipePaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }
    private val scorePaint = Paint().apply {
        color = Color.WHITE
        textSize = 60f
        textAlign = Paint.Align.LEFT
        isFakeBoldText = true
    }

    init {
        // Attempt to load bird sprite. Using standard resource name 'bird_sprite'
        // Make sure to rename your png file to 'bird_sprite.png' in res/drawable
        val resId = resources.getIdentifier("bird_sprite", "drawable", context.packageName)
        val originalBitmap = if (resId != 0) {
            BitmapFactory.decodeResource(resources, resId)
        } else {
            null
        }
        
        if (originalBitmap != null) {
            birdBitmap = originalBitmap.scale(100, 75, true)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        engine.screenWidth = w
        engine.screenHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Background color
        canvas.drawColor("#4EC0CA".toColorInt())

        // Update game state
        engine.update()

        // Draw bird
        birdBitmap?.let {
            canvas.drawBitmap(it, engine.bird.x.toFloat(), engine.bird.y.toFloat(), null)
        } ?: run {
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
        canvas.drawText("Score: ${engine.score}", 50f, 100f, scorePaint)

        if (engine.isGameOver) {
            val overPaint = Paint().apply {
                color = Color.RED
                textSize = 100f
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("GAME OVER", (width / 2).toFloat(), (height / 2).toFloat(), overPaint)
            canvas.drawText("Tap to Restart", (width / 2).toFloat(), (height / 2 + 120).toFloat(), scorePaint.apply { textAlign = Paint.Align.CENTER })
        } else {
            invalidate() // Continue animation
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            performClick()
            if (engine.isGameOver) {
                engine.reset()
                invalidate()
            } else {
                // Manually flapping if the user wants to play, 
                // but the engine currently has autoPlay enabled.
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
