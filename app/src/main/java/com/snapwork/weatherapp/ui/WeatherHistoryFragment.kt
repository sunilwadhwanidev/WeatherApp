package com.snapwork.weatherapp.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapwork.weatherapp.data.local.WeatherDatabase
import com.snapwork.weatherapp.data.repository.WeatherRepository
import com.snapwork.weatherapp.databinding.FragmentWeatherHistoryBinding
import kotlinx.coroutines.launch

class WeatherHistoryFragment : Fragment() {

    private var _binding: FragmentWeatherHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: WeatherRepository
    private lateinit var adapter: WeatherHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherHistoryBinding.inflate(inflater, container, false)

        val dao = WeatherDatabase.getInstance(requireContext()).weatherDao()
        repository = WeatherRepository(dao)

        setupRecyclerView()
        loadHistory()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = WeatherHistoryAdapter()
        binding.historyRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecycler.adapter = adapter
    }

    private fun loadHistory() {
        lifecycleScope.launch {
            repository.getAllWeather().collect { history ->
                adapter.submitList(history)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        // Refresh every time user switches to History tab
        loadHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

