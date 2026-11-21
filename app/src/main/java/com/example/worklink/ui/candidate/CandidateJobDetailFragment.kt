package com.example.worklink.ui.candidate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.worklink.R
import com.example.worklink.data.mock.CandidateJobItem

class CandidateJobDetailFragment : Fragment() {

    private var isSavedJob = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_candidate_job_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindJobData(view)
        setupActions(view)
    }

    private fun bindJobData(root: View) {
        val args = arguments
        val title = args?.getString(ARG_JOB_TITLE)
            ?: getString(R.string.candidate_detail_default_job_title)
        val company = args?.getString(ARG_COMPANY_NAME)
            ?: getString(R.string.candidate_detail_default_company)
        val location = args?.getString(ARG_LOCATION)
            ?: getString(R.string.candidate_detail_default_location)
        val postedTime = args?.getString(ARG_POSTED_TIME)
            ?: getString(R.string.candidate_detail_default_posted)
        val source = args?.getString(ARG_SOURCE)
            ?: getString(R.string.candidate_detail_default_source)
        val aboutCompany = args?.getString(ARG_ABOUT_COMPANY)
            ?: getString(R.string.candidate_detail_default_about)
        val requirements = args?.getString(ARG_REQUIREMENTS)
            ?: getString(R.string.candidate_detail_default_requirements)
        isSavedJob = args?.getBoolean(ARG_IS_SAVED, false) ?: false

        root.findViewById<TextView>(R.id.tv_candidate_detail_job_title).text = title
        root.findViewById<TextView>(R.id.tv_candidate_detail_company_name).text = company
        root.findViewById<TextView>(R.id.tv_candidate_detail_location).text = location
        root.findViewById<TextView>(R.id.tv_candidate_detail_posted_time).text = postedTime
        root.findViewById<TextView>(R.id.tv_candidate_detail_source).text = source
        root.findViewById<TextView>(R.id.tv_candidate_detail_about_content).text = aboutCompany
        root.findViewById<TextView>(R.id.tv_candidate_detail_requirements_content).text = requirements

        updateBookmarkIcon(root)
    }

    private fun setupActions(root: View) {
        root.findViewById<ImageButton>(R.id.btn_candidate_detail_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        root.findViewById<ImageButton>(R.id.btn_candidate_detail_bookmark).setOnClickListener {
            isSavedJob = !isSavedJob
            updateBookmarkIcon(root)
            val messageRes = if (isSavedJob) {
                R.string.candidate_detail_saved_success
            } else {
                R.string.candidate_detail_unsaved_success
            }
            Toast.makeText(requireContext(), getString(messageRes), Toast.LENGTH_SHORT).show()
        }

        root.findViewById<ImageButton>(R.id.btn_candidate_detail_chat).setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.candidate_detail_chat_toast),
                Toast.LENGTH_SHORT
            ).show()
        }

        root.findViewById<Button>(R.id.btn_candidate_detail_apply_cv).setOnClickListener {
            val jobTitle = root.findViewById<TextView>(R.id.tv_candidate_detail_job_title).text.toString()
            Toast.makeText(
                requireContext(),
                getString(R.string.candidate_detail_apply_toast, jobTitle),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateBookmarkIcon(root: View) {
        val bookmarkButton = root.findViewById<ImageButton>(R.id.btn_candidate_detail_bookmark)
        val iconRes = if (isSavedJob) {
            android.R.drawable.btn_star_big_on
        } else {
            android.R.drawable.btn_star_big_off
        }
        bookmarkButton.setImageResource(iconRes)
    }

    companion object {
        private const val ARG_JOB_TITLE = "arg_job_title"
        private const val ARG_COMPANY_NAME = "arg_company_name"
        private const val ARG_LOCATION = "arg_location"
        private const val ARG_POSTED_TIME = "arg_posted_time"
        private const val ARG_SOURCE = "arg_source"
        private const val ARG_ABOUT_COMPANY = "arg_about_company"
        private const val ARG_REQUIREMENTS = "arg_requirements"
        private const val ARG_IS_SAVED = "arg_is_saved"

        fun newInstance(job: CandidateJobItem): CandidateJobDetailFragment {
            return CandidateJobDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_JOB_TITLE, job.title)
                    putString(ARG_COMPANY_NAME, job.company)
                    putString(ARG_LOCATION, job.location)
                    putString(ARG_POSTED_TIME, job.postedTime)
                    putString(ARG_SOURCE, job.source)
                    putString(ARG_ABOUT_COMPANY, job.aboutCompany)
                    putString(ARG_REQUIREMENTS, job.requirements)
                    putBoolean(ARG_IS_SAVED, job.isSaved)
                }
            }
        }
    }
}