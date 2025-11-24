package com.example.deokmoa.ui.filterpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.deokmoa.data.AppDatabase
import com.example.deokmoa.data.Category
import com.example.deokmoa.databinding.FragmentCategoryBinding
import kotlinx.coroutines.launch

class CategoryFragment : Fragment() {
    // 뷰 바인딩
    private var _binding: FragmentCategoryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadTopCategories()
    }

    private fun loadTopCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            // reviewDao를 통해 상위 4개 카테고리 가져오기
            val topList = db.reviewDao().getTopCategories()

            // 1위 -> 큰 박스 (tvMostUsedCategory)
            if (topList.isNotEmpty()) {
                val item = topList[0]
                binding.tvMostUsedCategory.text = getCategoryDisplayName(item.category)
            } else {
                binding.tvMostUsedCategory.text = "데이터 없음"
            }

            // 2위 -> 리스트 첫 줄 (tvRankCategory1)
            if (topList.size >= 2) {
                val item = topList[1]
                binding.tvRankCategory1.text = getCategoryDisplayName(item.category)
            } else {
                binding.tvRankCategory1.text = "-"
            }

            // 3위 -> 리스트 둘째 줄 (tvRankCategory2)
            if (topList.size >= 3) {
                val item = topList[2]
                binding.tvRankCategory2.text = getCategoryDisplayName(item.category)
            } else {
                binding.tvRankCategory2.text = "-"
            }

            // 4위 -> 리스트 셋째 줄 (tvRankCategory3)
            if (topList.size >= 4) {
                val item = topList[3]
                binding.tvRankCategory3.text = getCategoryDisplayName(item.category)
            } else {
                binding.tvRankCategory3.text = "-"
            }
        }
    }

    // 영어를 한글로 바꾸는 함수
    private fun getCategoryDisplayName(dbName: String): String {
        return try {
            Category.valueOf(dbName).displayName
        } catch (e: Exception) {
            dbName
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}