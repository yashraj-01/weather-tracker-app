package com.example.weathertracker.ui.history

import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.weathertracker.R
import com.example.weathertracker.data.CheckpointModel
import com.example.weathertracker.data.DbQueryHelper
import android.view.LayoutInflater
import android.widget.ImageView

class CheckpointRecyclerViewAdapter(
    private val context: Context,
    private val arrayList: MutableList<CheckpointModel>
) : RecyclerView.Adapter<CheckpointRecyclerViewAdapter.ViewHolder>() {

    private lateinit var dbHelper: DbQueryHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.fragment_history_checkpoint_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvAddress.text = arrayList[position].getAddress()
        holder.tvLat.text = arrayList[position].getLat().toString()
        holder.tvLng.text = arrayList[position].getLng().toString()
        val temp = String.format("%.2f", arrayList[position].getTemp()) + "℃"
        holder.tvTemp.text = temp
        holder.tvWeather.text = arrayList[position].getWeatherDesc()
        dbHelper = DbQueryHelper(context)
        holder.ivDelete.setOnClickListener {
            dbHelper.deleteCheckpoint(arrayList[position].getID())
            arrayList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAddress: TextView = itemView.findViewById(R.id.tv_address)
        val tvLat: TextView = itemView.findViewById(R.id.tv_latitude)
        val tvLng: TextView = itemView.findViewById(R.id.tv_longitude)
        val tvTemp: TextView = itemView.findViewById(R.id.tv_temp)
        val tvWeather: TextView = itemView.findViewById(R.id.tv_weather)
        val ivDelete: ImageView = itemView.findViewById(R.id.iv_delete)
    }

}