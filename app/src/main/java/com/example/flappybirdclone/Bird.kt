package com.example.flappybirdclone

import com.example.flappybirdclone.ai.Genome

/**
 * Bird entity with predictive physics logic or AI control.
 */
data class Bird(
    var x: Double,
    var y: Double,
    var velocity: Double = 0.0,
    var genome: Genome = Genome(3, 8, 1) // Default genome: 3 inputs, 8 hidden, 1 output
) {
    private val gravity = 0.7
    private val flapStrength = -12.0
    val width = 100.0
    val height = 75.0
    var alive = true
    var score = 0
    var fitness = 0.0

    fun flap() {
        velocity = flapStrength
    }

    fun update() {
        if (!alive) return
        velocity += gravity
        y += velocity
        fitness += 0.1 // Increase fitness over time for survival
    }

    /**
     * Predictive logic:
     * Calculates if the bird will fall below the target gap center 
     * in the next few frames and flaps to maintain altitude.
     */
    fun predictAndFlap(pipes: List<Pipe>, screenHeight: Int) {
        if (!alive) return

        // Target: Center of the gap of the next upcoming pipe
        val nextPipe = pipes.find { it.x + it.width > x }
        val targetY = if (nextPipe != null) {
            nextPipe.topPipeHeight + (nextPipe.gapHeight / 2.0) - (height / 2.0)
        } else {
            screenHeight / 2.0
        }

        // Simple look-ahead: If I don't flap, where will I be in 3 frames?
        // If that position is below the target, flap now.
        val futureY = y + (velocity + gravity) + (velocity + 2 * gravity) + (velocity + 3 * gravity)
        
        if (futureY > targetY) {
            flap()
        }
    }
}
