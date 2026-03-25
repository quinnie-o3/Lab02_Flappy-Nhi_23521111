package com.example.flappybirdclone.ai

import java.util.Random

class Genome(val inputNodes: Int, val hiddenNodes: Int, val outputNodes: Int) {
    private val random = Random()
    
    var weightsIH = Array(hiddenNodes) { DoubleArray(inputNodes) { random.nextDouble() * 2 - 1 } }
    var weightsHO = Array(outputNodes) { DoubleArray(hiddenNodes) { random.nextDouble() * 2 - 1 } }
    var biasH = DoubleArray(hiddenNodes) { random.nextDouble() * 2 - 1 }
    var biasO = DoubleArray(outputNodes) { random.nextDouble() * 2 - 1 }

    fun mutate(rate: Double) {
        fun mutateVal(v: Double): Double {
            return if (random.nextDouble() < rate) v + random.nextGaussian() * 0.2 else v
        }

        weightsIH = weightsIH.map { row -> row.map { mutateVal(it) }.toDoubleArray() }.toTypedArray()
        weightsHO = weightsHO.map { row -> row.map { mutateVal(it) }.toDoubleArray() }.toTypedArray()
        biasH = biasH.map { mutateVal(it) }.toDoubleArray()
        biasO = biasO.map { mutateVal(it) }.toDoubleArray()
    }

    fun copy(): Genome {
        val newGenome = Genome(inputNodes, hiddenNodes, outputNodes)
        newGenome.weightsIH = Array(hiddenNodes) { i -> weightsIH[i].clone() }
        newGenome.weightsHO = Array(outputNodes) { i -> weightsHO[i].clone() }
        newGenome.biasH = biasH.clone()
        newGenome.biasO = biasO.clone()
        return newGenome
    }
}
