package com.example.deokmoa.ui.filterpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.deokmoa.data.AppDatabase
import com.example.deokmoa.data.Tag
import com.example.deokmoa.databinding.FragmentTagBinding
import kotlinx.coroutines.launch

class TagFragment : Fragment() {
    // 뷰 바인딩
    private var _binding: FragmentTagBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadTopTags()
    }

    private fun loadTopTags() {
        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            // 모든 태그 문자열 가져오기
            val rawTags = db.reviewDao().getAllTags()

            // 분리 및 카운팅
            val tagCounts = rawTags
                .flatMap { it.split(",") }       // 쉼표로 쪼개기
                .filter { it.isNotBlank() }      // 빈 문자열 제거
                .map { it.trim() }               // 공백 제거
                .groupingBy { it }               // 태그 이름끼리
                .eachCount()                     // 개수 세기
                .toList()                        // 리스트로 변환
                .sortedByDescending { it.second } // 개수 많은 순 정렬

            // [1위]
            if (tagCounts.isNotEmpty()) {
                val (tagName, _) = tagCounts[0]
                binding.tvMostUsedTag.text = getTagDisplayName(tagName)
            } else {
                binding.tvMostUsedTag.text = "데이터 없음"
            }

            // [2위]
            if (tagCounts.size >= 2) {
                val (tagName, _) = tagCounts[1]
                binding.tvRankTag1.text = getTagDisplayName(tagName)
            } else {
                binding.tvRankTag1.text = "-"
            }

            // [3위]
            if (tagCounts.size >= 3) {
                val (tagName, _) = tagCounts[2]
                binding.tvRankTag2.text = getTagDisplayName(tagName)
            } else {
                binding.tvRankTag2.text = "-"
            }

            // [4위]
            if (tagCounts.size >= 4) {
                val (tagName, _) = tagCounts[3]
                binding.tvRankTag3.text = getTagDisplayName(tagName)
            } else {
                binding.tvRankTag3.text = "-"
            }
        }
    }

    private fun getTagDisplayName(dbTagName: String): String {
        return try {
            Tag.valueOf(dbTagName).displayName
        } catch (e: Exception) {
            dbTagName
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}