package com.example.flappybirdclone

class GameEngine {
    val bird = Bird(100.0, 500.0)
    val pipes = mutableListOf<Pipe>()
    var isGameOver = false
    var score = 0
    private val speed = 5.0
    private val pipeSpawnInterval = 100
    private var tickCount = 0

    var screenWidth = 0
    var screenHeight = 0

    fun update() {
        if (isGameOver || screenHeight <= 0) return

        bird.update()
        
        tickCount++
        if (tickCount >= pipeSpawnInterval) {
            pipes.add(Pipe(x = screenWidth.toDouble()))
            tickCount = 0
        }

        val iterator = pipes.iterator()
        while (iterator.hasNext()) {
            val pipe = iterator.next()
            pipe.update(speed)
            
            // Check if pipe is passed
            if (!pipe.isPassed && bird.x > pipe.x + pipe.width) {
                pipe.isPassed = true
                score++
            }

            if (pipe.x + pipe.width < 0) {
                iterator.remove()
            }
        }

        checkCollision()
    }

    private fun checkCollision() {
        // Ground and ceiling collision
        if (bird.y <= 0 || bird.y + bird.height >= screenHeight) {
            isGameOver = true
        }

        // Pipe collision
        for (pipe in pipes) {
            // Bounding box collision
            if (bird.x + bird.width > pipe.x && bird.x < pipe.x + pipe.width) {
                if (bird.y < pipe.topPipeHeight || bird.y + bird.height > pipe.topPipeHeight + pipe.gapHeight) {
                    isGameOver = true
                }
            }
        }
    }
    
    fun reset() {
        bird.x = 100.0
        bird.y = 500.0
        bird.velocity = 0.0
        pipes.clear()
        isGameOver = false
        score = 0
        tickCount = 0
    }
}
