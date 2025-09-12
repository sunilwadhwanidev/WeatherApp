package com.snapwork.weatherapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snapwork.weatherapp.data.local.WeatherEntity
import com.snapwork.weatherapp.databinding.ItemWeatherHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class WeatherHistoryAdapter :
    ListAdapter<WeatherEntity, WeatherHistoryAdapter.WeatherViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            ItemWeatherHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WeatherViewHolder(private val binding: ItemWeatherHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeatherEntity) {
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            val date = sdf.format(Date(item.timestamp))

            binding.cityTv.text = "${item.city}, ${item.country}"
            binding.tempTv.text = "${item.temperature} °C"
            binding.descTv.text = item.description
            binding.timeTv.text = date
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<WeatherEntity>() {
        override fun areItemsTheSame(oldItem: WeatherEntity, newItem: WeatherEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: WeatherEntity, newItem: WeatherEntity) =
            oldItem == newItem
    }
}

