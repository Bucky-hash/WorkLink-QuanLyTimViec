package com.example.worklink.ui.candidate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.worklink.R

data class CandidateSavedJobUiModel(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val postedTime: String,
    val source: String,
    val aboutCompany: String,
    val requirements: String,
    val isSaved: Boolean = true
)

class CandidateSavedJobAdapter(
    private val onJobClick: (CandidateSavedJobUiModel) -> Unit,
    private val onToggleSavedClick: (CandidateSavedJobUiModel) -> Unit
) : RecyclerView.Adapter<CandidateSavedJobAdapter.SavedJobViewHolder>() {

    private val items = mutableListOf<CandidateSavedJobUiModel>()
    private var isEditMode = false

    fun submitList(newItems: List<CandidateSavedJobUiModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setEditMode(enabled: Boolean) {
        if (isEditMode == enabled) return
        isEditMode = enabled
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedJobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_candidate_saved_job, parent, false)
        return SavedJobViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedJobViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class SavedJobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvJobTitle: TextView = itemView.findViewById(R.id.tv_candidate_saved_job_title)
        private val tvCompanyName: TextView = itemView.findViewById(R.id.tv_candidate_saved_company_name)
        private val tvLocation: TextView = itemView.findViewById(R.id.tv_candidate_saved_location)
        private val tvMeta: TextView = itemView.findViewById(R.id.tv_candidate_saved_meta)
        private val tvSource: TextView = itemView.findViewById(R.id.tv_candidate_saved_source)
        private val btnBookmark: ImageButton = itemView.findViewById(R.id.btn_candidate_saved_bookmark)

        fun bind(job: CandidateSavedJobUiModel) {
            tvJobTitle.text = job.title
            tvCompanyName.text = job.company
            tvLocation.text = job.location
            tvMeta.text = job.postedTime
            tvSource.text = job.source

            val bookmarkIcon = if (isEditMode || job.isSaved) {
                android.R.drawable.btn_star_big_on
            } else {
                android.R.drawable.btn_star_big_off
            }
            btnBookmark.setImageResource(bookmarkIcon)

            btnBookmark.setOnClickListener {
                onToggleSavedClick(job)
            }

            itemView.setOnClickListener {
                onJobClick(job)
            }
        }
    }
}