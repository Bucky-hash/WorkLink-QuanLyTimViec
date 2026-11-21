package com.example.worklink.ui.candidate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.worklink.R
import com.example.worklink.data.mock.CandidateJobItem
import com.example.worklink.data.mock.CandidateJobMockData
import com.google.android.material.bottomnavigation.BottomNavigationView

class CandidateExploreActivity : AppCompatActivity() {

    private lateinit var recommendedAdapter: CandidateJobAdapter
    private lateinit var latestAdapter: CandidateJobAdapter

    private val allRecommendedJobs = CandidateJobMockData.getRecommendedJobs()
    private val allLatestJobs = CandidateJobMockData.getLatestJobs()

    private var isProgrammingSelected = true
    private var isLocationSelected = true
    private var isRemoteSelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_explore)

        setupRecyclerViews()
        setupSearchAndFilters()
        setupBottomNavigation()
    }

    private fun setupRecyclerViews() {
        val rvRecommended = findViewById<RecyclerView>(R.id.rv_candidate_recommended_jobs)
        val rvLatest = findViewById<RecyclerView>(R.id.rv_candidate_latest_jobs)

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

    private fun setupSearchAndFilters() {
        val etSearch = findViewById<EditText>(R.id.et_candidate_search)
        val btnFilter = findViewById<ImageButton>(R.id.btn_candidate_filter_settings)
        val chipProgramming = findViewById<TextView>(R.id.chip_candidate_programming)
        val chipLocation = findViewById<TextView>(R.id.chip_candidate_location)
        val chipRemote = findViewById<TextView>(R.id.chip_candidate_remote)

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

        findViewById<TextView>(R.id.tv_candidate_recommended_see_all).setOnClickListener {
            showToast("Xem tất cả việc làm đề xuất")
        }

        findViewById<TextView>(R.id.tv_candidate_latest_see_all).setOnClickListener {
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
        if (isSelected) {
            chip.setBackgroundResource(R.drawable.bg_candidate_chip_selected)
            chip.setTextColor(ContextCompat.getColor(this, R.color.candidate_bg_white))
        } else {
            chip.setBackgroundResource(R.drawable.bg_candidate_chip_normal)
            chip.setTextColor(ContextCompat.getColor(this, R.color.candidate_text_secondary))
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bnv_candidate_main)
        bottomNav.selectedItemId = R.id.nav_candidate_explore

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_candidate_explore -> true
                R.id.nav_candidate_saved -> {
                    showToast("Chuyển sang Công việc đã lưu")
                    true
                }
                R.id.nav_candidate_applied -> {
                    showToast("Chuyển sang Công việc đã ứng tuyển")
                    true
                }
                R.id.nav_candidate_notifications -> {
                    showToast("Chuyển sang Thông báo")
                    true
                }
                R.id.nav_candidate_account -> {
                    showToast("Chuyển sang Tài khoản cá nhân")
                    true
                }
                else -> false
            }
        }
    }

    private fun handleBookmarkToast(job: CandidateJobItem) {
        val msg = if (job.isSaved) "Đã lưu: ${job.title}" else "Đã bỏ lưu: ${job.title}"
        showToast(msg)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}