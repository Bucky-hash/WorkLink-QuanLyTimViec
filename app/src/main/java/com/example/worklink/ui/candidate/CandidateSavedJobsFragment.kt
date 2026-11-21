package com.example.worklink.ui.candidate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worklink.R

class CandidateSavedJobsFragment : Fragment() {

    private val savedJobs = mutableListOf<CandidateSavedJobUiModel>()
    private var isEditMode = false

    private var rvSavedJobs: RecyclerView? = null
    private var tvEmptyMessage: TextView? = null
    private var btnAdd: TextView? = null
    private var btnEdit: TextView? = null

    private val adapter = CandidateSavedJobAdapter(
        onJobClick = { job -> openJobDetail(job) },
        onToggleSavedClick = { job -> removeSavedJob(job) }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_candidate_saved_jobs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setupRecyclerView()
        setupHeaderActions()
        loadSavedJobs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rvSavedJobs = null
        tvEmptyMessage = null
        btnAdd = null
        btnEdit = null
    }

    private fun bindViews(root: View) {
        rvSavedJobs = root.findViewById(R.id.rv_candidate_saved_jobs)
        tvEmptyMessage = root.findViewById(R.id.tv_candidate_saved_empty)
        btnAdd = root.findViewById(R.id.btn_candidate_saved_add)
        btnEdit = root.findViewById(R.id.btn_candidate_saved_edit)
    }

    private fun setupRecyclerView() {
        rvSavedJobs?.layoutManager = LinearLayoutManager(requireContext())
        rvSavedJobs?.adapter = adapter
    }

    private fun setupHeaderActions() {
        btnAdd?.setOnClickListener {
            openExploreScreen()
        }

        btnEdit?.setOnClickListener {
            isEditMode = !isEditMode
            btnEdit?.text = if (isEditMode) {
                getString(R.string.candidate_saved_action_done)
            } else {
                getString(R.string.candidate_saved_action_edit)
            }
            adapter.setEditMode(isEditMode)
        }
    }

    private fun loadSavedJobs() {
        savedJobs.clear()
        savedJobs.addAll(buildInitialSavedJobs())
        refreshListState()
    }

    private fun buildInitialSavedJobs(): List<CandidateSavedJobUiModel> {
        val aboutFpt = getString(R.string.candidate_detail_default_about)
        val reqDefault = getString(R.string.candidate_detail_default_requirements)
        return listOf(
            CandidateSavedJobUiModel(
                id = "saved_1",
                title = getString(R.string.candidate_detail_default_job_title),
                company = getString(R.string.candidate_detail_default_company),
                location = getString(R.string.candidate_detail_default_location),
                postedTime = "9 giờ trước",
                source = "Linked in",
                aboutCompany = aboutFpt,
                requirements = reqDefault,
                isSaved = true
            ),
            CandidateSavedJobUiModel(
                id = "saved_2",
                title = "Chuyên viên Marketing",
                company = "VNG",
                location = "TP.HCM (Làm việc từ xa)",
                postedTime = "9 giờ trước",
                source = "Linked in",
                aboutCompany = "VNG phát triển nền tảng số, trò chơi trực tuyến và giải pháp doanh nghiệp.",
                requirements = reqDefault,
                isSaved = true
            ),
            CandidateSavedJobUiModel(
                id = "saved_3",
                title = "Trưởng nhóm lập trình",
                company = "VNG",
                location = "Đà Nẵng (Làm việc từ xa)",
                postedTime = "5 giờ trước",
                source = "glassdoor",
                aboutCompany = "VNG phát triển nền tảng số, trò chơi trực tuyến và giải pháp doanh nghiệp.",
                requirements = reqDefault,
                isSaved = true
            ),
            CandidateSavedJobUiModel(
                id = "saved_4",
                title = "Kế toán tổng hợp",
                company = "Viettel",
                location = "Đà Nẵng (Làm việc từ xa)",
                postedTime = "9 giờ trước",
                source = "MONSTER",
                aboutCompany = "Viettel cung cấp hạ tầng số, viễn thông và giải pháp công nghệ.",
                requirements = reqDefault,
                isSaved = true
            ),
            CandidateSavedJobUiModel(
                id = "saved_5",
                title = "Lập trình viên Frontend",
                company = "Viettel",
                location = "Đà Nẵng (Làm việc từ xa)",
                postedTime = "9 giờ trước",
                source = "glassdoor",
                aboutCompany = "Viettel cung cấp hạ tầng số, viễn thông và giải pháp công nghệ.",
                requirements = reqDefault,
                isSaved = true
            )
        )
    }

    private fun removeSavedJob(job: CandidateSavedJobUiModel) {
        val removed = savedJobs.removeAll { it.id == job.id }
        if (removed) {
            refreshListState()
            Toast.makeText(
                requireContext(),
                getString(R.string.candidate_saved_removed_toast, job.title),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun refreshListState() {
        adapter.submitList(savedJobs.toList())
        val isEmpty = savedJobs.isEmpty()
        tvEmptyMessage?.visibility = if (isEmpty) View.VISIBLE else View.GONE
        rvSavedJobs?.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun openJobDetail(job: CandidateSavedJobUiModel) {
        val containerId = (view?.parent as? ViewGroup)?.id ?: return
        if (containerId == View.NO_ID) return
        val detailFragment = CandidateJobDetailFragment().apply {
            arguments = Bundle().apply {
                putString("arg_job_title", job.title)
                putString("arg_company_name", job.company)
                putString("arg_location", job.location)
                putString("arg_posted_time", job.postedTime)
                putString("arg_source", job.source)
                putString("arg_about_company", job.aboutCompany)
                putString("arg_requirements", job.requirements)
                putBoolean("arg_is_saved", job.isSaved)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(containerId, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun openExploreScreen() {
        val containerId = (view?.parent as? ViewGroup)?.id ?: return
        if (containerId == View.NO_ID) return
        parentFragmentManager.beginTransaction()
            .replace(containerId, CandidateExploreFragment())
            .commit()
    }
}