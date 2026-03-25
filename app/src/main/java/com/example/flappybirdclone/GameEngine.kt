package com.example.flappybirdclone

class GameEngine {
    var bird = Bird(100.0, 500.0)
    val pipes = mutableListOf<Pipe>()
    var score = 0
    var isGameOver = false
    
    private val speed = 5.0
    private val pipeSpawnInterval = 100
    private var tickCount = 0

    var screenWidth = 0
    var screenHeight = 0

    fun update() {
        if (isGameOver || screenHeight <= 0) return

        // Predictive logic: the bird looks at the next pipe's gap 
        // and maintains its height accordingly.
        bird.predictAndFlap(pipes, screenHeight)
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
        if (bird.y <= 0 || bird.y + bird.height >= screenHeight) {
            isGameOver = true
            bird.alive = false
        } else {
            for (pipe in pipes) {
                if (bird.x + bird.width > pipe.x && bird.x < pipe.x + pipe.width) {
                    if (bird.y < pipe.topPipeHeight || bird.y + bird.height > pipe.topPipeHeight + pipe.gapHeight) {
                        isGameOver = true
                        bird.alive = false
                        break
                    }
                }
            }
        }
    }

    fun reset() {
        bird = Bird(100.0, 500.0)
        pipes.clear()
        score = 0
        isGameOver = false
        tickCount = 0
    }
}
