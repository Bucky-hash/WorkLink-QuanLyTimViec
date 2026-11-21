package com.example.worklink.ui.candidate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.worklink.R
import com.example.worklink.data.mock.CandidateJobItem
import com.example.worklink.data.mock.CandidateJobMockData

class CandidateExploreFragment : Fragment() {

    private lateinit var recommendedAdapter: CandidateJobAdapter
    private lateinit var latestAdapter: CandidateJobAdapter

    private val allRecommendedJobs = CandidateJobMockData.getRecommendedJobs()
    private val allLatestJobs = CandidateJobMockData.getLatestJobs()

    private var isProgrammingSelected = true
    private var isLocationSelected = true
    private var isRemoteSelected = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_candidate_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ẩn thanh BottomNav bên trong layout vì CandidateMainActivity đã có sẵn thanh điều hướng rồi
        view.findViewById<View>(R.id.bnv_candidate_main)?.visibility = View.GONE

        setupRecyclerViews(view)
        setupSearchAndFilters(view)
    }

    private fun setupRecyclerViews(root: View) {
        val rvRecommended = root.findViewById<RecyclerView>(R.id.rv_candidate_recommended_jobs)
        val rvLatest = root.findViewById<RecyclerView>(R.id.rv_candidate_latest_jobs)

        recommendedAdapter = CandidateJobAdapter(
            jobs = allRecommendedJobs,
            isHorizontalCard = true,
            onJobClick = { job -> showToast("Mở chi tiết: ${job.title} - ${job.company}") },
            onBookmarkClick = { job -> handleBookmarkToast(job) }
        )

        latestAdapter = CandidateJobAdapter(
            jobs = allLatestJobs,
            isHorizontalCard = false,
            onJobClick = { job -> showToast("Mở chi tiết: ${job.title} - ${job.company}") },
            onBookmarkClick = { job -> handleBookmarkToast(job) }
        )

        rvRecommended.adapter = recommendedAdapter
        rvLatest.adapter = latestAdapter
    }

    private fun setupSearchAndFilters(root: View) {
        val etSearch = root.findViewById<EditText>(R.id.et_candidate_search)
        val btnFilter = root.findViewById<ImageButton>(R.id.btn_candidate_filter_settings)
        val chipProgramming = root.findViewById<TextView>(R.id.chip_candidate_programming)
        val chipLocation = root.findViewById<TextView>(R.id.chip_candidate_location)
        val chipRemote = root.findViewById<TextView>(R.id.chip_candidate_remote)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterJobs(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        chipProgramming.setOnClickListener {
            isProgrammingSelected = !isProgrammingSelected
            updateChipStyle(chipProgramming, isProgrammingSelected)
            filterJobs(etSearch.text.toString())
        }

        chipLocation.setOnClickListener {
            isLocationSelected = !isLocationSelected
            updateChipStyle(chipLocation, isLocationSelected)
            filterJobs(etSearch.text.toString())
        }

        chipRemote.setOnClickListener {
            isRemoteSelected = !isRemoteSelected
            updateChipStyle(chipRemote, isRemoteSelected)
            filterJobs(etSearch.text.toString())
        }

        btnFilter.setOnClickListener {
            showToast("Mở bộ lọc tiêu chí việc làm")
        }

        root.findViewById<TextView>(R.id.tv_candidate_recommended_see_all).setOnClickListener {
            showToast("Xem tất cả việc làm đề xuất")
        }

        root.findViewById<TextView>(R.id.tv_candidate_latest_see_all).setOnClickListener {
            showToast("Xem tất cả việc làm cập nhật mới")
        }
    }

    private fun filterJobs(keyword: String) {
        val query = keyword.trim().lowercase()
        val matchFilter: (CandidateJobItem) -> Boolean = { job ->
            val matchText = query.isEmpty() ||
                    job.title.lowercase().contains(query) ||
                    job.company.lowercase().contains(query) ||
                    job.location.lowercase().contains(query)
            val matchRemote = !isRemoteSelected || job.isRemote
            matchText && matchRemote
        }

        recommendedAdapter.updateData(allRecommendedJobs.filter(matchFilter))
        latestAdapter.updateData(allLatestJobs.filter(matchFilter))
    }

    private fun updateChipStyle(chip: TextView, isSelected: Boolean) {
        val ctx = requireContext()
        if (isSelected) {
            chip.setBackgroundResource(R.drawable.bg_candidate_chip_selected)
            chip.setTextColor(ContextCompat.getColor(ctx, R.color.candidate_bg_white))
        } else {
            chip.setBackgroundResource(R.drawable.bg_candidate_chip_normal)
            chip.setTextColor(ContextCompat.getColor(ctx, R.color.candidate_text_secondary))
        }
    }

    private fun handleBookmarkToast(job: CandidateJobItem) {
        val msg = if (job.isSaved) "Đã lưu: ${job.title}" else "Đã bỏ lưu: ${job.title}"
        showToast(msg)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}