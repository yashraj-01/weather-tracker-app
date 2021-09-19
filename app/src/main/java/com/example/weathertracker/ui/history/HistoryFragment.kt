package com.example.weathertracker.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathertracker.data.CheckpointModel
import com.example.weathertracker.data.DbQueryHelper
import com.example.weathertracker.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val checkpointList = mutableListOf<CheckpointModel>()
    private lateinit var dbQueryHelper: DbQueryHelper
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        dbQueryHelper = DbQueryHelper(this.requireContext())
        recyclerView = binding.checkpointRecyclerView
        checkpointList.addAll(dbQueryHelper.fetchAllCheckpoints())
        recyclerView.layoutManager = LinearLayoutManager(
            this.requireContext(), LinearLayoutManager.VERTICAL, false
        )
        recyclerView.itemAnimator = DefaultItemAnimator()
        val checkpointAdapter = CheckpointRecyclerViewAdapter(this.requireContext(), checkpointList)
        recyclerView.adapter = checkpointAdapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}