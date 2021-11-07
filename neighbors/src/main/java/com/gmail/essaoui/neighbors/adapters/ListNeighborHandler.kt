package com.gmail.essaoui.neighbors.adapters

import com.gmail.essaoui.neighbors.models.Neighbor

interface ListNeighborHandler {
    fun onDeleteNeighbor(neighbor: Neighbor)
}
