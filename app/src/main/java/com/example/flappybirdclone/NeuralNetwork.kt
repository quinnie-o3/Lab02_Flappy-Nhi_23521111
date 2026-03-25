package com.example.flappybirdclone

import java.util.Random
import kotlin.math.tanh

class NeuralNetwork(val inputNodes: Int, val hiddenNodes: Int, val outputNodes: Int) {
    private val random = Random()
    var weightsIH: Array<DoubleArray> = Array(hiddenNodes) { DoubleArray(inputNodes) { random.nextDouble() * 2 - 1 } }
    var weightsHO: Array<DoubleArray> = Array(outputNodes) { DoubleArray(hiddenNodes) { random.nextDouble() * 2 - 1 } }
    var biasH: DoubleArray = DoubleArray(hiddenNodes) { random.nextDouble() * 2 - 1 }
    var biasO: DoubleArray = DoubleArray(outputNodes) { random.nextDouble() * 2 - 1 }

    fun predict(inputs: DoubleArray): DoubleArray {
        // Hidden layer
        val hidden = DoubleArray(hiddenNodes)
        for (i in 0 until hiddenNodes) {
            var sum = 0.0
            for (j in 0 until inputNodes) {
                sum += inputs[j] * weightsIH[i][j]
            }
            hidden[i] = tanh(sum + biasH[i])
        }

        // Output layer
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

    fun copy(): NeuralNetwork {
        val nn = NeuralNetwork(inputNodes, hiddenNodes, outputNodes)
        nn.weightsIH = weightsIH.map { it.clone() }.toTypedArray()
        nn.weightsHO = weightsHO.map { it.clone() }.toTypedArray()
        nn.biasH = biasH.clone()
        nn.biasO = biasO.clone()
        return nn
    }

    fun mutate(rate: Double) {
        fun mutateVal(v: Double): Double {
            return if (random.nextDouble() < rate) v + random.nextGaussian() * 0.1 else v
        }

        weightsIH = weightsIH.map { row -> row.map { mutateVal(it) }.toDoubleArray() }.toTypedArray()
        weightsHO = weightsHO.map { row -> row.map { mutateVal(it) }.toDoubleArray() }.toTypedArray()
        biasH = biasH.map { mutateVal(it) }.toDoubleArray()
        biasO = biasO.map { mutateVal(it) }.toDoubleArray()
    }
}
