
package com.example.worklink.ui.admin

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worklink.R
import com.example.worklink.core.base.BaseFragment

class JobApprovalFragment :
    BaseFragment(R.layout.fragment_admin_job_approval) {

    private lateinit var edtSearch: EditText
    private lateinit var rvJobs: RecyclerView

    private lateinit var tvAll: TextView
    private lateinit var tvPending: TextView
    private lateinit var tvApproved: TextView
    private lateinit var tvRejected: TextView

    private var selectedStatus = "ALL"

    private val jobs = mutableListOf(
        JobItem(
            "Lập trình viên Flutter",
            "Công ty ABC",
            "Hà Nội",
            "18 - 25 triệu",
            "21/10/2026",
            "Toàn thời gian",
            "PENDING"
        ),
        JobItem(
            "Lập trình viên Java",
            "FPT Software",
            "Hà Nội",
            "20 - 30 triệu",
            "20/10/2026",
            "Toàn thời gian",
            "PENDING"
        )
    )

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        edtSearch = view.findViewById(R.id.edt_admin_jobs_search)
        rvJobs = view.findViewById(R.id.rv_admin_jobs)

        tvAll = view.findViewById(R.id.tv_admin_job_filter_all)
        tvPending = view.findViewById(R.id.tv_admin_job_filter_pending)
        tvApproved = view.findViewById(R.id.tv_admin_job_filter_approved)
        tvRejected = view.findViewById(R.id.tv_admin_job_filter_rejected)

        rvJobs.layoutManager = LinearLayoutManager(requireContext())

        tvAll.setOnClickListener {
            selectStatus("ALL")
        }

        tvPending.setOnClickListener {
            selectStatus("PENDING")
        }

        tvApproved.setOnClickListener {
            selectStatus("APPROVED")
        }

        tvRejected.setOnClickListener {
            selectStatus("REJECTED")
        }

        edtSearch.doOnTextChanged { text, _, _, _ ->
            filterJobs(text.toString())
        }

        updateFilterUI()
        filterJobs("")
    }

    private fun selectStatus(status: String) {
        selectedStatus = status
        updateFilterUI()
        filterJobs(edtSearch.text.toString())
    }

    private fun updateFilterUI() {
        val filters = listOf(
            tvAll to "ALL",
            tvPending to "PENDING",
            tvApproved to "APPROVED",
            tvRejected to "REJECTED"
        )

        filters.forEach { (textView, status) ->
            val isSelected = selectedStatus == status

            textView.setBackgroundResource(
                if (isSelected) {
                    R.color.wl_primary
                } else {
                    R.color.wl_surface
                }
            )

            textView.setTextColor(
                requireContext().getColor(
                    if (isSelected) {
                        R.color.white
                    } else {
                        R.color.wl_text_secondary
                    }
                )
            )
        }
    }

    private fun filterJobs(keyword: String) {
        val result = jobs.filter { job ->
            val matchesStatus =
                selectedStatus == "ALL" ||
                        job.status == selectedStatus

            val matchesKeyword =
                job.title.contains(keyword, ignoreCase = true) ||
                        job.company.contains(keyword, ignoreCase = true) ||
                        job.location.contains(keyword, ignoreCase = true)

            matchesStatus && matchesKeyword
        }

        rvJobs.adapter = JobAdapter(result)
    }

    private fun approveJob(job: JobItem) {
        job.status = "APPROVED"
        toast("Đã duyệt tin tuyển dụng")
        filterJobs(edtSearch.text.toString())
    }

    private fun rejectJob(job: JobItem, reason: String) {
        if (reason.isBlank()) {
            toast("Vui lòng nhập lý do từ chối")
            return
        }

        job.rejectReason = reason.trim()
        job.status = "REJECTED"

        toast("Đã từ chối tin tuyển dụng")
        filterJobs(edtSearch.text.toString())
    }

    private data class JobItem(
        val title: String,
        val company: String,
        val location: String,
        val salary: String,
        val date: String,
        val jobType: String,
        var status: String,
        var rejectReason: String = ""
    )

    private inner class JobAdapter(
        private val items: List<JobItem>
    ) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

        inner class JobViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            val title: TextView =
                itemView.findViewById(R.id.tv_admin_job_title)

            val company: TextView =
                itemView.findViewById(R.id.tv_admin_job_company)

            val date: TextView =
                itemView.findViewById(R.id.tv_admin_job_date)

            val location: TextView =
                itemView.findViewById(R.id.tv_admin_job_location)

            val salary: TextView =
                itemView.findViewById(R.id.tv_admin_job_salary)

            val jobType: TextView =
                itemView.findViewById(R.id.tv_admin_job_type)

            val status: TextView =
                itemView.findViewById(R.id.tv_admin_job_status)

            val rejectReason: EditText =
                itemView.findViewById(R.id.edt_admin_job_reject_reason)

            val btnDetail: Button =
                itemView.findViewById(R.id.btn_admin_job_detail)

            val btnApprove: Button =
                itemView.findViewById(R.id.btn_admin_job_approve)

            val btnReject: Button =
                itemView.findViewById(R.id.btn_admin_job_reject)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): JobViewHolder {
            val itemView = layoutInflater.inflate(
                R.layout.item_admin_job,
                parent,
                false
            )

            return JobViewHolder(itemView)
        }

        override fun onBindViewHolder(
            holder: JobViewHolder,
            position: Int
        ) {
            val job = items[position]

            holder.title.text = job.title
            holder.company.text =
                "${getString(R.string.admin_job_company)}: ${job.company}"

            holder.date.text = "Ngày gửi: ${job.date}"

            holder.location.text =
                "${getString(R.string.admin_job_location)}: ${job.location}"

            holder.salary.text =
                "${getString(R.string.admin_job_salary)}: ${job.salary}"

            holder.jobType.text = "Hình thức: ${job.jobType}"

            when (job.status) {
                "PENDING" -> {
                    holder.status.text =
                        getString(R.string.admin_job_status_pending)

                    holder.status.setTextColor(
                        requireContext().getColor(R.color.wl_primary_dark)
                    )

                    holder.status.setBackgroundResource(
                        R.color.wl_primary_light
                    )
                }

                "APPROVED" -> {
                    holder.status.text =
                        getString(R.string.admin_job_status_approved)

                    holder.status.setTextColor(
                        requireContext().getColor(R.color.wl_primary_dark)
                    )

                    holder.status.setBackgroundResource(
                        R.color.wl_primary_light
                    )
                }

                "REJECTED" -> {
                    holder.status.text =
                        getString(R.string.admin_job_status_rejected)

                    holder.status.setTextColor(
                        requireContext().getColor(R.color.wl_error)
                    )

                    holder.status.setBackgroundResource(
                        R.color.wl_field_bg
                    )
                }
            }

            val isPending = job.status == "PENDING"

            holder.rejectReason.visibility =
                if (isPending) View.VISIBLE else View.GONE

            holder.rejectReason.setText(job.rejectReason)

            holder.btnApprove.visibility =
                if (isPending) View.VISIBLE else View.GONE

            holder.btnReject.visibility =
                if (isPending) View.VISIBLE else View.GONE

            holder.btnDetail.setOnClickListener {
                toast("${job.title} - ${job.company}")
            }

            holder.btnApprove.setOnClickListener {
                approveJob(job)
            }

            holder.btnReject.setOnClickListener {
                rejectJob(
                    job,
                    holder.rejectReason.text.toString()
                )
            }
        }

        override fun getItemCount(): Int = items.size
    }
}
