package com.example.flappybirdclone

import java.util.Random
import kotlin.math.tanh

enum class NodeType { INPUT, HIDDEN, OUTPUT }

data class NodeGene(val id: Int, val type: NodeType)

data class ConnectionGene(
    val fromNode: Int,
    val toNode: Int,
    var weight: Double,
    var enabled: Boolean,
    val innovation: Int
)

class NeatNetwork(val inputCount: Int, val outputCount: Int) {
    private val random = Random()
    val nodes = mutableListOf<NodeGene>()
    val connections = mutableListOf<ConnectionGene>()
    
    // For calculation
    private var nodeValues = mutableMapOf<Int, Double>()

    init {
        // Create input nodes
        for (i in 0 until inputCount) nodes.add(NodeGene(i, NodeType.INPUT))
        // Create output nodes
        for (i in 0 until outputCount) nodes.add(NodeGene(inputCount + i, NodeType.OUTPUT))
        
        // Fully connect input to output initially
        var innov = 0
        for (i in 0 until inputCount) {
            for (o in 0 until outputCount) {
                connections.add(ConnectionGene(i, inputCount + o, random.nextDouble() * 2 - 1, true, innov++))
            }
        }
    }

    fun predict(inputs: DoubleArray): DoubleArray {
        nodeValues.clear()
        // Set input values
        for (i in 0 until inputCount) {
            nodeValues[i] = inputs[i]
        }
        
        // Simplified feed-forward for NEAT (assuming no cycles for Flappy Bird)
        // In a real NEAT, we'd use a more robust activation order
        val outputs = DoubleArray(outputCount)
        
        for (o in 0 until outputCount) {
            outputs[o] = getValue(inputCount + o)
        }
        
        return outputs
    }

    private fun getValue(nodeId: Int): Double {
        if (nodeValues.containsKey(nodeId)) return nodeValues[nodeId]!!
        
        var sum = 0.0
        val incoming = connections.filter { it.toNode == nodeId && it.enabled }
        for (conn in incoming) {
            sum += getValue(conn.fromNode) * conn.weight
        }
        
        val value = if (nodes.find { it.id == nodeId }?.type == NodeType.OUTPUT) {
            1.0 / (1.0 + kotlin.math.exp(-sum)) // Sigmoid for output
        } else {
            tanh(sum) // Tanh for hidden
        }
        
        nodeValues[nodeId] = value
        return value
    }

    fun mutate() {
        val r = random.nextDouble()
        when {
            r < 0.8 -> mutateWeights()
            r < 0.85 -> addConnection()
            r < 0.88 -> addNode()
        }
    }

    private fun mutateWeights() {
        for (conn in connections) {
            if (random.nextDouble() < 0.1) {
                conn.weight += random.nextGaussian() * 0.2
            } else if (random.nextDouble() < 0.05) {
                conn.weight = random.nextDouble() * 2 - 1
            }
        }
    }

    private fun addConnection() {
        val node1 = nodes[random.nextInt(nodes.size)]
        val node2 = nodes[random.nextInt(nodes.size)]
        
        // Basic check to avoid circular/invalid connections
        if (node1.type == NodeType.OUTPUT || node2.type == NodeType.INPUT || node1.id == node2.id) return
        if (connections.any { it.fromNode == node1.id && it.toNode == node2.id }) return
        
        connections.add(ConnectionGene(node1.id, node2.id, random.nextDouble() * 2 - 1, true, connections.size + 1000))
    }

    private fun addNode() {
        if (connections.isEmpty()) return
        val conn = connections[random.nextInt(connections.size)]
        if (!conn.enabled) return
        
        conn.enabled = false
        val newNodeId = nodes.size
        nodes.add(NodeGene(newNodeId, NodeType.HIDDEN))
        
        connections.add(ConnectionGene(conn.fromNode, newNodeId, 1.0, true, connections.size + 2000))
        connections.add(ConnectionGene(newNodeId, conn.toNode, conn.weight, true, connections.size + 3000))
    }

    fun copy(): NeatNetwork {
        val newNet = NeatNetwork(inputCount, outputCount)
        newNet.nodes.clear()
        newNet.nodes.addAll(nodes)
        newNet.connections.clear()
        newNet.connections.addAll(connections.map { it.copy() })
        return newNet
    }
}
