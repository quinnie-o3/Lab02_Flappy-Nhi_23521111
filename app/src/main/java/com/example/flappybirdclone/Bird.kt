package com.example.flappybirdclone

/**
 * Bird entity with position, velocity and dimensions.
 */
data class Bird(
    var x: Double,
    var y: Double,
    var velocity: Double = 0.0
) {
    private val gravity = 0.7
    val width = 100.0
    val height = 75.0

    fun flap() {
        velocity = -12.0
    }

    fun update() {
        velocity += gravity
        y += velocity
    }
}
