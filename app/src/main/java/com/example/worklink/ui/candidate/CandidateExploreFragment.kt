package com.example.worklink.ui.candidate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worklink.R
import com.example.worklink.data.mock.CandidateJobItem
import com.example.worklink.data.mock.CandidateJobMockData

class CandidateExploreFragment : Fragment() {

    private lateinit var rvRecommended: RecyclerView
    private lateinit var rvLatest: RecyclerView

    private var selectedCategory: String = "Lập trình"
    private var onlyRemote: Boolean = false
    private var currentKeyword: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_candidate_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tự động ẩn thanh điều hướng dưới của layout Activity để tránh trùng với BaseHostActivity
        (view as? ViewGroup)?.let { rootGroup ->
            if (rootGroup.childCount > 1) {
                val lastChild = rootGroup.getChildAt(rootGroup.childCount - 1)
                if (lastChild is ViewGroup &&
                    lastChild !is NestedScrollView &&
                    lastChild !is ScrollView
                ) {
                    lastChild.visibility = View.GONE
                }
            }
        }

        rvRecommended = view.findViewById(R.id.rv_candidate_recommended_jobs)
        rvLatest = view.findViewById(R.id.rv_candidate_latest_jobs)

        rvRecommended.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvLatest.layoutManager = LinearLayoutManager(requireContext())

        setupSearchAndFilters(view)
        applyFilter()
    }

    private fun setupSearchAndFilters(root: View) {
        val etSearch = root.findViewById<EditText>(R.id.et_candidate_search)
        val chipProgramming = root.findViewById<TextView>(R.id.chip_candidate_programming)
        val chipLocation = root.findViewById<TextView>(R.id.chip_candidate_location)
        val chipRemote = root.findViewById<TextView>(R.id.chip_candidate_remote)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentKeyword = s?.toString()?.trim().orEmpty()
                applyFilter()
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        chipProgramming.setOnClickListener {
            selectedCategory = if (selectedCategory == "Lập trình") "" else "Lập trình"
            updateChipStyles(chipProgramming, chipLocation, chipRemote)
            applyFilter()
        }

        chipLocation.setOnClickListener {
            selectedCategory = if (selectedCategory == "Đà Nẵng") "" else "Đà Nẵng"
            updateChipStyles(chipProgramming, chipLocation, chipRemote)
            applyFilter()
        }

        chipRemote.setOnClickListener {
            onlyRemote = !onlyRemote
            updateChipStyles(chipProgramming, chipLocation, chipRemote)
            applyFilter()
        }

        updateChipStyles(chipProgramming, chipLocation, chipRemote)
    }

    private fun updateChipStyles(
        chipProgramming: TextView,
        chipLocation: TextView,
        chipRemote: TextView
    ) {
        applyChipState(chipProgramming, selectedCategory == "Lập trình")
        applyChipState(chipLocation, selectedCategory == "Đà Nẵng")
        applyChipState(chipRemote, onlyRemote)
    }

    private fun applyChipState(chip: TextView, isSelected: Boolean) {
        val bgRes = if (isSelected) {
            R.drawable.bg_candidate_chip_selected
        } else {
            R.drawable.bg_candidate_chip_normal
        }
        val textColorRes = if (isSelected) {
            R.color.candidate_bg_white
        } else {
            R.color.candidate_text_primary
        }
        chip.setBackgroundResource(bgRes)
        chip.setTextColor(ContextCompat.getColor(requireContext(), textColorRes))
    }

    private fun applyFilter() {
        val recommended = CandidateJobMockData.getRecommendedJobs().filter { matchesFilter(it) }
        val latest = CandidateJobMockData.getLatestJobs().filter { matchesFilter(it) }

        rvRecommended.adapter = CandidateJobAdapter(
            jobs = recommended,
            isHorizontalCard = true,
            onJobClick = { job -> openJobDetail(job) },
            onBookmarkClick = { job -> toggleSaveJob(job) }
        )

        rvLatest.adapter = CandidateJobAdapter(
            jobs = latest,
            isHorizontalCard = false,
            onJobClick = { job -> openJobDetail(job) },
            onBookmarkClick = { job -> toggleSaveJob(job) }
        )
    }

    private fun matchesFilter(job: CandidateJobItem): Boolean {
        val matchKeyword = currentKeyword.isEmpty() ||
                job.title.contains(currentKeyword, ignoreCase = true) ||
                job.company.contains(currentKeyword, ignoreCase = true) ||
                job.location.contains(currentKeyword, ignoreCase = true)

        val matchCategory = when (selectedCategory) {
            "" -> true
            "Đà Nẵng" -> job.location.contains("Đà Nẵng", ignoreCase = true)
            else -> job.category.equals(selectedCategory, ignoreCase = true)
        }

        val matchRemote = !onlyRemote || job.isRemote
        return matchKeyword && matchCategory && matchRemote
    }

    private fun toggleSaveJob(job: CandidateJobItem) {
        job.isSaved = !job.isSaved
        val msg = if (job.isSaved) {
            getString(R.string.candidate_detail_saved_success)
        } else {
            getString(R.string.candidate_detail_unsaved_success)
        }
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun openJobDetail(job: CandidateJobItem) {
        val containerId = (view?.parent as? ViewGroup)?.id ?: android.R.id.content
        parentFragmentManager.beginTransaction()
            .replace(containerId, CandidateJobDetailFragment.newInstance(job))
            .addToBackStack(null)
            .commit()
    }
}