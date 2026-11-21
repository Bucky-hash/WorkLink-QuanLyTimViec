package com.example.worklink.ui.candidate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.worklink.R
import com.example.worklink.data.mock.CandidateJobItem

class CandidateJobDetailActivity : AppCompatActivity() {

    private var isSavedJob = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_job_detail)

        bindJobDataFromIntent()
        setupActions()
    }

    private fun bindJobDataFromIntent() {
        val title = intent.getStringExtra(EXTRA_JOB_TITLE)
            ?: getString(R.string.candidate_detail_default_job_title)
        val company = intent.getStringExtra(EXTRA_COMPANY_NAME)
            ?: getString(R.string.candidate_detail_default_company)
        val location = intent.getStringExtra(EXTRA_LOCATION)
            ?: getString(R.string.candidate_detail_default_location)
        val postedTime = intent.getStringExtra(EXTRA_POSTED_TIME)
            ?: getString(R.string.candidate_detail_default_posted)
        val source = intent.getStringExtra(EXTRA_SOURCE)
            ?: getString(R.string.candidate_detail_default_source)
        val aboutCompany = intent.getStringExtra(EXTRA_ABOUT_COMPANY)
            ?: getString(R.string.candidate_detail_default_about)
        val requirements = intent.getStringExtra(EXTRA_REQUIREMENTS)
            ?: getString(R.string.candidate_detail_default_requirements)
        isSavedJob = intent.getBooleanExtra(EXTRA_IS_SAVED, false)

        findViewById<TextView>(R.id.tv_candidate_detail_job_title).text = title
        findViewById<TextView>(R.id.tv_candidate_detail_company_name).text = company
        findViewById<TextView>(R.id.tv_candidate_detail_location).text = location
        findViewById<TextView>(R.id.tv_candidate_detail_posted_time).text = postedTime
        findViewById<TextView>(R.id.tv_candidate_detail_source).text = source
        findViewById<TextView>(R.id.tv_candidate_detail_about_content).text = aboutCompany
        findViewById<TextView>(R.id.tv_candidate_detail_requirements_content).text = requirements

        updateBookmarkIcon()
    }

    private fun setupActions() {
        findViewById<ImageButton>(R.id.btn_candidate_detail_back).setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.btn_candidate_detail_bookmark).setOnClickListener {
            isSavedJob = !isSavedJob
            updateBookmarkIcon()
            val messageRes = if (isSavedJob) {
                R.string.candidate_detail_saved_success
            } else {
                R.string.candidate_detail_unsaved_success
            }
            Toast.makeText(this, getString(messageRes), Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageButton>(R.id.btn_candidate_detail_chat).setOnClickListener {
            Toast.makeText(
                this,
                getString(R.string.candidate_detail_chat_toast),
                Toast.LENGTH_SHORT
            ).show()
        }

        findViewById<Button>(R.id.btn_candidate_detail_apply_cv).setOnClickListener {
            val jobTitle = findViewById<TextView>(R.id.tv_candidate_detail_job_title).text.toString()
            Toast.makeText(
                this,
                getString(R.string.candidate_detail_apply_toast, jobTitle),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateBookmarkIcon() {
        val bookmarkButton = findViewById<ImageButton>(R.id.btn_candidate_detail_bookmark)
        val iconRes = if (isSavedJob) {
            android.R.drawable.btn_star_big_on
        } else {
            android.R.drawable.btn_star_big_off
        }
        bookmarkButton.setImageResource(iconRes)
    }

    companion object {
        private const val EXTRA_JOB_TITLE = "extra_job_title"
        private const val EXTRA_COMPANY_NAME = "extra_company_name"
        private const val EXTRA_LOCATION = "extra_location"
        private const val EXTRA_POSTED_TIME = "extra_posted_time"
        private const val EXTRA_SOURCE = "extra_source"
        private const val EXTRA_ABOUT_COMPANY = "extra_about_company"
        private const val EXTRA_REQUIREMENTS = "extra_requirements"
        private const val EXTRA_IS_SAVED = "extra_is_saved"

        fun newIntent(context: Context, job: CandidateJobItem): Intent {
            return Intent(context, CandidateJobDetailActivity::class.java).apply {
                putExtra(EXTRA_JOB_TITLE, job.title)
                putExtra(EXTRA_COMPANY_NAME, job.company)
                putExtra(EXTRA_LOCATION, job.location)
                putExtra(EXTRA_POSTED_TIME, job.postedTime)
                putExtra(EXTRA_SOURCE, job.source)
                putExtra(EXTRA_ABOUT_COMPANY, job.aboutCompany)
                putExtra(EXTRA_REQUIREMENTS, job.requirements)
                putExtra(EXTRA_IS_SAVED, job.isSaved)
            }
        }
    }
}