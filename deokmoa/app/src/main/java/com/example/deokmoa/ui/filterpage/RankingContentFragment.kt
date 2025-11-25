package com.example.deokmoa.ui.filterpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.deokmoa.data.AppDatabase
import com.example.deokmoa.databinding.FragmentRankingContentBinding
import kotlinx.coroutines.launch

class RankingContentFragment : Fragment() {
    private var _binding: FragmentRankingContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAverageRating()
    }

    private fun loadAverageRating() {
        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())

            // 평균 별점 가져오기
            val averageRating = db.reviewDao().getAverageRating() ?: 0f
            binding.rcAverageRating.rating = averageRating
            // 점수 소수점 한자리까지 표시
            binding.tvMostUsedRankingContent.text = String.format("%.1f점", averageRating)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}