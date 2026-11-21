package com.example.worklink.ui.employer

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worklink.R
import com.example.worklink.data.mock.EmployerInterviewMockData
import com.example.worklink.data.mock.EmployerInterviewSession
import com.example.worklink.data.mock.EmployerInterviewStatus
import com.google.android.material.button.MaterialButton

class EmployerInterviewScheduleFragment : Fragment() {

    private var btnCreate: MaterialButton? = null
    private var btnPrevMonth: ImageButton? = null
    private var btnNextMonth: ImageButton? = null
    private var tvMonth: TextView? = null
    private var llWeekStrip: LinearLayout? = null
    private var tvDateHeading: TextView? = null
    private var tvCountSummary: TextView? = null
    private var tvEmpty: TextView? = null
    private var rvSessions: RecyclerView? = null

    private var monthIndex = 10
    private var yearValue = 2026
    private var selectedDay = 21
    private val weekDays = EmployerInterviewMockData.getWeekDays()
    private val allSessions = EmployerInterviewMockData.getSessions()
    private lateinit var sessionAdapter: EmployerInterviewSessionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_employer_interview_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setupRecyclerView()
        bindControls()
        renderCalendarAndSessions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        btnCreate = null
        btnPrevMonth = null
        btnNextMonth = null
        tvMonth = null
        llWeekStrip = null
        tvDateHeading = null
        tvCountSummary = null
        tvEmpty = null
        rvSessions = null
    }

    private fun bindViews(root: View) {
        btnCreate = root.findViewById(R.id.btn_employer_interview_create)
        btnPrevMonth = root.findViewById(R.id.btn_employer_interview_prev_month)
        btnNextMonth = root.findViewById(R.id.btn_employer_interview_next_month)
        tvMonth = root.findViewById(R.id.tv_employer_interview_month)
        llWeekStrip = root.findViewById(R.id.ll_employer_interview_week_strip)
        tvDateHeading = root.findViewById(R.id.tv_employer_interview_date_heading)
        tvCountSummary = root.findViewById(R.id.tv_employer_interview_count_summary)
        tvEmpty = root.findViewById(R.id.tv_employer_interview_empty)
        rvSessions = root.findViewById(R.id.rv_employer_interview_list)
    }

    private fun setupRecyclerView() {
        sessionAdapter = EmployerInterviewSessionAdapter { session ->
            Toast.makeText(
                requireContext(),
                getString(R.string.employer_interview_item_toast, session.candidateName, session.startTime),
                Toast.LENGTH_SHORT
            ).show()
        }
        rvSessions?.layoutManager = LinearLayoutManager(requireContext())
        rvSessions?.adapter = sessionAdapter
    }

    private fun bindControls() {
        btnCreate?.setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.employer_interview_create_toast),
                Toast.LENGTH_SHORT
            ).show()
        }

        btnPrevMonth?.setOnClickListener {
            if (monthIndex == 1) {
                monthIndex = 12
                yearValue -= 1
            } else {
                monthIndex -= 1
            }
            renderMonthLabel()
        }

        btnNextMonth?.setOnClickListener {
            if (monthIndex == 12) {
                monthIndex = 1
                yearValue += 1
            } else {
                monthIndex += 1
            }
            renderMonthLabel()
        }
    }

    private fun renderMonthLabel() {
        tvMonth?.text = getString(R.string.employer_interview_month_year, monthIndex, yearValue)
    }

    private fun renderCalendarAndSessions() {
        renderMonthLabel()
        renderWeekStrip()
        renderDaySessions()
    }

    private fun renderWeekStrip() {
        val strip = llWeekStrip ?: return
        strip.removeAllViews()

        weekDays.forEach { dayItem ->
            val isSelected = dayItem.dayOfMonth == selectedDay
            val cell = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    marginStart = dpToPx(2)
                    marginEnd = dpToPx(2)
                }
                setPadding(0, dpToPx(8), 0, dpToPx(8))
                setBackgroundColor(
                    if (isSelected) Color.parseColor("#16A34A") else Color.TRANSPARENT
                )
                setOnClickListener {
                    selectedDay = dayItem.dayOfMonth
                    renderCalendarAndSessions()
                }
            }

            val tvDow = TextView(requireContext()).apply {
                text = getString(dayItem.dayLabelResId)
                textSize = 12f
                setTextColor(if (isSelected) Color.WHITE else Color.parseColor("#6B7280"))
            }

            val tvNum = TextView(requireContext()).apply {
                text = dayItem.dayOfMonth.toString()
                textSize = 15f
                setTextColor(if (isSelected) Color.WHITE else Color.parseColor("#111827"))
            }

            val tvDot = TextView(requireContext()).apply {
                text = "•"
                textSize = 12f
                visibility = if (dayItem.hasEvents) View.VISIBLE else View.INVISIBLE
                setTextColor(if (isSelected) Color.WHITE else Color.parseColor("#16A34A"))
            }

            cell.addView(tvDow)
            cell.addView(tvNum)
            cell.addView(tvDot)
            strip.addView(cell)
        }
    }

    private fun renderDaySessions() {
        val selectedInfo = weekDays.firstOrNull { it.dayOfMonth == selectedDay }
        val dayName = selectedInfo?.let { getString(it.fullDayNameResId) }.orEmpty()
        val daySessions = allSessions.filter { it.dayOfMonth == selectedDay }

        tvDateHeading?.text = if (selectedDay == 21 && monthIndex == 10) {
            getString(R.string.employer_interview_selected_date)
        } else {
            getString(R.string.employer_interview_day_month, selectedDay, monthIndex)
        }

        tvCountSummary?.text = getString(
            R.string.employer_interview_day_summary,
            dayName,
            daySessions.size
        )

        val isEmpty = daySessions.isEmpty()
        tvEmpty?.visibility = if (isEmpty) View.VISIBLE else View.GONE
        rvSessions?.visibility = if (isEmpty) View.GONE else View.VISIBLE
        sessionAdapter.submitList(daySessions)
    }

    private fun dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()
}

private class EmployerInterviewSessionAdapter(
    private val onItemClick: (EmployerInterviewSession) -> Unit
) : RecyclerView.Adapter<EmployerInterviewSessionAdapter.SessionViewHolder>() {

    private val items = mutableListOf<EmployerInterviewSession>()

    fun submitList(newItems: List<EmployerInterviewSession>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_employer_interview_session, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount(): Int = items.size

    class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvStartTime: TextView = itemView.findViewById(R.id.tv_employer_interview_start_time)
        private val tvEndTime: TextView = itemView.findViewById(R.id.tv_employer_interview_end_time)
        private val tvDuration: TextView = itemView.findViewById(R.id.tv_employer_interview_duration)
        private val tvCandidateName: TextView = itemView.findViewById(R.id.tv_employer_interview_candidate_name)
        private val tvJobTitle: TextView = itemView.findViewById(R.id.tv_employer_interview_job_title)
        private val tvLocation: TextView = itemView.findViewById(R.id.tv_employer_interview_location)
        private val tvStatus: TextView = itemView.findViewById(R.id.tv_employer_interview_status)

        fun bind(session: EmployerInterviewSession, onItemClick: (EmployerInterviewSession) -> Unit) {
            val context = itemView.context
            tvStartTime.text = session.startTime
            tvEndTime.text = session.endTime
            tvDuration.text = session.durationText
            tvCandidateName.text = session.candidateName
            tvJobTitle.text = session.jobTitle
            tvLocation.text = session.locationOrLink

            when (session.status) {
                EmployerInterviewStatus.UPCOMING -> {
                    tvStatus.text = context.getString(R.string.employer_interview_status_upcoming)
                    tvStatus.setTextColor(Color.parseColor("#B45309"))
                    tvStatus.setBackgroundColor(Color.parseColor("#FEF3C7"))
                }
                EmployerInterviewStatus.CONFIRMED -> {
                    tvStatus.text = context.getString(R.string.employer_interview_status_confirmed)
                    tvStatus.setTextColor(Color.parseColor("#16A34A"))
                    tvStatus.setBackgroundColor(Color.parseColor("#DCFCE7"))
                }
            }

            itemView.setOnClickListener { onItemClick(session) }
        }
    }
}