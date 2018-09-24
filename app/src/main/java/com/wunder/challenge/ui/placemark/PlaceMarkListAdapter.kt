package com.wunder.challenge.ui.placemark

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wunder.challenge.R
import com.wunder.challenge.databinding.ItemPlaceMarkBinding
import com.wunder.challenge.model.PlaceMark

class PlaceMarkListAdapter: RecyclerView.Adapter<PlaceMarkListAdapter.ViewHolder>() {
    private lateinit var placeMarkList:List<PlaceMark>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceMarkListAdapter.ViewHolder {
        val binding: ItemPlaceMarkBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_place_mark, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceMarkListAdapter.ViewHolder, position: Int) {
        holder.bind(placeMarkList[position])
    }

    override fun getItemCount(): Int {
        return if(::placeMarkList.isInitialized) placeMarkList.size else 0
    }

    fun updatePlaceMarkList(placeMarkList:List<PlaceMark>){
        this.placeMarkList = placeMarkList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemPlaceMarkBinding):RecyclerView.ViewHolder(binding.root){
        private val viewModel = PlaceMarkViewModel()

        fun bind(placeMark: PlaceMark){
            viewModel.bind(placeMark)
            binding.viewModel = viewModel
        }
    }
}