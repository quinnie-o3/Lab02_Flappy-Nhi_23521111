package com.example.flappybirdclone.ai

import com.example.flappybirdclone.Bird

class EvolutionManager(val populationSize: Int) {
    var generation = 1
    
    fun createNextGeneration(deadBirds: List<Bird>): List<Bird> {
        generation++
        
        // Sort by fitness descending
        val sortedBirds = deadBirds.sortedByDescending { it.fitness }
        val nextGen = mutableListOf<Bird>()

        // 1. Elitism: Keep the best 2 birds exactly as they are
        if (sortedBirds.isNotEmpty()) {
            nextGen.add(Bird(100.0, 500.0, genome = sortedBirds[0].genome.copy()))
            if (sortedBirds.size > 1) {
                nextGen.add(Bird(100.0, 500.0, genome = sortedBirds[1].genome.copy()))
            }
        }

        // 2. Fill the rest with mutated offspring from the top 20% performers
        val pool = sortedBirds.take((populationSize * 0.2).toInt().coerceAtLeast(1))
        
        while (nextGen.size < populationSize) {
            val parent = if (pool.isNotEmpty()) pool.random() else Bird(100.0, 500.0)
            val childGenome = parent.genome.copy()
            childGenome.mutate(0.1) // 10% mutation rate
            nextGen.add(Bird(100.0, 500.0, genome = childGenome))
        }

        return nextGen
    }
}
