package com.example.worklink.ui.candidate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.worklink.R
import com.example.worklink.data.mock.CandidateJobItem

class CandidateJobAdapter(
    private var jobs: List<CandidateJobItem>,
    private val isHorizontalCard: Boolean,
    private val onJobClick: (CandidateJobItem) -> Unit,
    private val onBookmarkClick: (CandidateJobItem) -> Unit
) : RecyclerView.Adapter<CandidateJobAdapter.CandidateJobViewHolder>() {

    inner class CandidateJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(job: CandidateJobItem) {
            if (isHorizontalCard) {
                bindRecommendedCard(job)
            } else {
                bindLatestCard(job)
            }
            itemView.setOnClickListener { onJobClick(job) }
        }

        private fun bindRecommendedCard(job: CandidateJobItem) {
            itemView.findViewById<TextView>(R.id.tv_candidate_rec_job_title).text = job.title
            itemView.findViewById<TextView>(R.id.tv_candidate_rec_company_name).text = job.company
            itemView.findViewById<TextView>(R.id.tv_candidate_rec_location).text = job.location
            itemView.findViewById<TextView>(R.id.tv_candidate_rec_posted_time).text = job.postedTime
            itemView.findViewById<TextView>(R.id.tv_candidate_rec_source).text = job.source

            val btnBookmark = itemView.findViewById<ImageButton>(R.id.btn_candidate_rec_bookmark)
            updateBookmarkIcon(btnBookmark, job.isSaved)
            btnBookmark.setOnClickListener {
                job.isSaved = !job.isSaved
                updateBookmarkIcon(btnBookmark, job.isSaved)
                onBookmarkClick(job)
            }
        }

        private fun bindLatestCard(job: CandidateJobItem) {
            itemView.findViewById<TextView>(R.id.tv_candidate_latest_job_title).text = job.title
            itemView.findViewById<TextView>(R.id.tv_candidate_latest_company_name).text = job.company
            itemView.findViewById<TextView>(R.id.tv_candidate_latest_meta).text =
                "${job.location} • ${job.postedTime}"

            val btnBookmark = itemView.findViewById<ImageButton>(R.id.btn_candidate_latest_bookmark)
            updateBookmarkIcon(btnBookmark, job.isSaved)
            btnBookmark.setOnClickListener {
                job.isSaved = !job.isSaved
                updateBookmarkIcon(btnBookmark, job.isSaved)
                onBookmarkClick(job)
            }
        }

        private fun updateBookmarkIcon(button: ImageButton, isSaved: Boolean) {
            val iconRes = if (isSaved) {
                android.R.drawable.btn_star_big_on
            } else {
                android.R.drawable.btn_star_big_off
            }
            button.setImageResource(iconRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateJobViewHolder {
        val layoutRes = if (isHorizontalCard) {
            R.layout.item_candidate_recommended_job
        } else {
            R.layout.item_candidate_latest_job
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return CandidateJobViewHolder(view)
    }

    override fun onBindViewHolder(holder: CandidateJobViewHolder, position: Int) {
        holder.bind(jobs[position])
    }

    override fun getItemCount(): Int = jobs.size

    fun updateData(newJobs: List<CandidateJobItem>) {
        jobs = newJobs
        notifyDataSetChanged()
    }
}