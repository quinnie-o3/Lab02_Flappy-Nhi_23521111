package com.example.flappybirdclone.ai

import kotlin.math.tanh

class NeuralNetwork(
    val inputNodes: Int,
    val hiddenNodes: Int,
    val outputNodes: Int,
    var weightsIH: Array<DoubleArray>,
    var weightsHO: Array<DoubleArray>,
    var biasH: DoubleArray,
    var biasO: DoubleArray
) {
    fun predict(inputs: DoubleArray): DoubleArray {
        val hidden = DoubleArray(hiddenNodes)
        for (i in 0 until hiddenNodes) {
            var sum = 0.0
            for (j in 0 until inputNodes) {
                sum += inputs[j] * weightsIH[i][j]
            }
            hidden[i] = tanh(sum + biasH[i])
        }

        val output = DoubleArray(outputNodes)
        for (i in 0 until outputNodes) {
            var sum = 0.0
            for (j in 0 until hiddenNodes) {
                sum += hidden[j] * weightsHO[i][j]
            }
            output[i] = sigmoid(sum + biasO[i])
        }
        return output
    }

    private fun sigmoid(x: Double): Double = 1.0 / (1.0 + kotlin.math.exp(-x))
}
