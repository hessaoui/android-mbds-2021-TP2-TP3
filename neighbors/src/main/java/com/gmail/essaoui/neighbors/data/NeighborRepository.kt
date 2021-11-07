package com.gmail.essaoui.neighbors.data

import com.gmail.essaoui.neighbors.data.service.DummyNeighborApiService
import com.gmail.essaoui.neighbors.data.service.NeighborApiService
import com.gmail.essaoui.neighbors.models.Neighbor

class NeighborRepository {
    private val apiService: NeighborApiService

    init {
        apiService = DummyNeighborApiService()
    }

    fun getNeighbours(): MutableList<Neighbor> = apiService.neighbours

    fun createNeighbour(neighbor: Neighbor) = apiService.createNeighbour(neighbor)

    fun deleteNeighbour(neighbor: Neighbor) = apiService.deleteNeighbour(neighbor)

    companion object {
        private var instance: NeighborRepository? = null
        fun getInstance(): NeighborRepository {
            if (instance == null) {
                instance = NeighborRepository()
            }
            return instance!!
        }
    }
}
