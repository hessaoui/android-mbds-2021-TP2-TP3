package com.gmail.essaoui.neighbors.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.essaoui.neighbors.NavigationListener
import com.gmail.essaoui.neighbors.R
import com.gmail.essaoui.neighbors.adapters.ListNeighborHandler
import com.gmail.essaoui.neighbors.adapters.ListNeighborsAdapter
import com.gmail.essaoui.neighbors.data.NeighborRepository
import com.gmail.essaoui.neighbors.models.Neighbor
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListNeighborsFragment : Fragment(), ListNeighborHandler {
    /**
     * Function permanent de definer une vue à attached à un fragment
     */
    private lateinit var recyclerView: RecyclerView
    private lateinit var addNeighbor: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_neighbors_fragment, container, false)

        (activity as? NavigationListener)?.updateTitle(R.string.list_neighbours_toolbar_name)

        recyclerView = view.findViewById(R.id.neighbors_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        addNeighbor = view.findViewById(R.id.add_neighbor)
        addNeighbor.setOnClickListener {
            (activity as? NavigationListener)?.showFragment(AddNeighbourFragment())
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val neighbors = NeighborRepository.getInstance().getNeighbours()
        val adapter = ListNeighborsAdapter(neighbors, this)
        recyclerView.adapter = adapter
    }

    override fun onDeleteNeighbor(neighbor: Neighbor) {
        activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(context?.getString(R.string.delete_neighbor))
                .setPositiveButton(
                    context?.getString(R.string.yes)
                ) { dialog, _ ->
                    NeighborRepository.getInstance().deleteNeighbour(neighbor)
                    dialog.dismiss()
                    refreshPage()
                }
                .setNegativeButton(
                    getString(R.string.cancel)
                ) { _, _ ->
                }
            builder.create().show()
        }
    }

    private fun refreshPage() {
        val neighbors = NeighborRepository.getInstance().getNeighbours()
        val adapter = ListNeighborsAdapter(neighbors, this)
        recyclerView.adapter = adapter
    }
}
