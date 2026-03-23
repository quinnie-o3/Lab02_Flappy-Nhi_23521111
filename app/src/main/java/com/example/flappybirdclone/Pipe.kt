package com.example.flappybirdclone

import kotlin.random.Random

/**
 * Pipe entity with position and gap dimensions.
 */
data class Pipe(
    var x: Double,
    val width: Double = 150.0,
    val gapHeight: Double = 350.0,
    var topPipeHeight: Double = Random.nextDouble(100.0, 500.0),
    var isPassed: Boolean = false
) {
    fun update(speed: Double) {
        x -= speed
    }
}
