package com.example.worklink.ui.candidate

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.worklink.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider

data class CandidateJobPreferenceOption(
    val label: String,
    val isSelected: Boolean
)

class CandidateJobPreferencesFragment : Fragment() {

    private var btnCancel: TextView? = null
    private var btnConfirm: TextView? = null
    private var cgRoles: ChipGroup? = null
    private var btnAddRole: TextView? = null
    private var cgLocations: ChipGroup? = null
    private var btnAddLocation: TextView? = null
    private var cgJobTypes: ChipGroup? = null
    private var btnAddJobType: TextView? = null
    private var tvSalaryValue: TextView? = null
    private var rsSalary: RangeSlider? = null

    private val roles = mutableListOf<CandidateJobPreferenceOption>()
    private val locations = mutableListOf<CandidateJobPreferenceOption>()
    private val jobTypes = mutableListOf<CandidateJobPreferenceOption>()
    private var minSalaryMillions = DEFAULT_MIN_SALARY
    private var maxSalaryMillions = DEFAULT_MAX_SALARY

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_candidate_job_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        initDefaultPreferences()
        renderAllSections()
        bindActions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        btnCancel = null
        btnConfirm = null
        cgRoles = null
        btnAddRole = null
        cgLocations = null
        btnAddLocation = null
        cgJobTypes = null
        btnAddJobType = null
        tvSalaryValue = null
        rsSalary = null
    }

    private fun bindViews(root: View) {
        btnCancel = root.findViewById(R.id.btn_candidate_pref_cancel)
        btnConfirm = root.findViewById(R.id.btn_candidate_pref_confirm)
        cgRoles = root.findViewById(R.id.cg_candidate_pref_roles)
        btnAddRole = root.findViewById(R.id.btn_candidate_pref_add_role)
        cgLocations = root.findViewById(R.id.cg_candidate_pref_locations)
        btnAddLocation = root.findViewById(R.id.btn_candidate_pref_add_location)
        cgJobTypes = root.findViewById(R.id.cg_candidate_pref_job_types)
        btnAddJobType = root.findViewById(R.id.btn_candidate_pref_add_job_type)
        tvSalaryValue = root.findViewById(R.id.tv_candidate_pref_salary_value)
        rsSalary = root.findViewById(R.id.rs_candidate_pref_salary)
    }

    private fun initDefaultPreferences() {
        roles.clear()
        roles.addAll(
            listOf(
                CandidateJobPreferenceOption(getString(R.string.candidate_chip_role_programming), true),
                CandidateJobPreferenceOption(getString(R.string.candidate_chip_role_ui_ux), false),
                CandidateJobPreferenceOption(getString(R.string.candidate_chip_role_frontend), false)
            )
        )

        locations.clear()
        locations.addAll(
            listOf(
                CandidateJobPreferenceOption(getString(R.string.candidate_chip_loc_hanoi), true),
                CandidateJobPreferenceOption(getString(R.string.candidate_chip_loc_danang), true),
                CandidateJobPreferenceOption(getString(R.string.candidate_chip_loc_hcm), false)
            )
        )

        jobTypes.clear()
        jobTypes.addAll(
            listOf(
                CandidateJobPreferenceOption(getString(R.string.candidate_chip_remote), true),
                CandidateJobPreferenceOption(getString(R.string.candidate_chip_type_fulltime), false),
                CandidateJobPreferenceOption(getString(R.string.candidate_chip_type_parttime), false)
            )
        )

        minSalaryMillions = DEFAULT_MIN_SALARY
        maxSalaryMillions = DEFAULT_MAX_SALARY
    }

    private fun renderAllSections() {
        renderChipGroup(cgRoles, roles) { updated ->
            roles.clear()
            roles.addAll(updated)
        }
        renderChipGroup(cgLocations, locations) { updated ->
            locations.clear()
            locations.addAll(updated)
        }
        renderChipGroup(cgJobTypes, jobTypes) { updated ->
            jobTypes.clear()
            jobTypes.addAll(updated)
        }
        setupSalarySlider()
    }

    private fun renderChipGroup(
        group: ChipGroup?,
        source: List<CandidateJobPreferenceOption>,
        onChanged: (List<CandidateJobPreferenceOption>) -> Unit
    ) {
        val chipGroup = group ?: return
        chipGroup.removeAllViews()
        val mutable = source.toMutableList()

        mutable.forEachIndexed { index, option ->
            val chip = Chip(requireContext()).apply {
                text = option.label
                isCheckable = true
                isChecked = option.isSelected
                setOnCheckedChangeListener { _, checked ->
                    mutable[index] = mutable[index].copy(isSelected = checked)
                    onChanged(mutable)
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun setupSalarySlider() {
        val slider = rsSalary ?: return
        slider.values = listOf(minSalaryMillions.toFloat(), maxSalaryMillions.toFloat())
        updateSalaryLabel(minSalaryMillions, maxSalaryMillions)

        slider.addOnChangeListener { rangeSlider, _, _ ->
            val values = rangeSlider.values
            if (values.size >= 2) {
                minSalaryMillions = values[0].toInt()
                maxSalaryMillions = values[1].toInt()
                updateSalaryLabel(minSalaryMillions, maxSalaryMillions)
            }
        }
    }

    private fun updateSalaryLabel(minVal: Int, maxVal: Int) {
        tvSalaryValue?.text = resources.getString(
            R.string.candidate_pref_salary_summary,
            minVal,
            maxVal
        )
    }

    private fun bindActions() {
        btnCancel?.setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.candidate_pref_cancelled_toast),
                Toast.LENGTH_SHORT
            ).show()
            finishPreferencesStep()
        }

        btnConfirm?.setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.candidate_pref_saved_toast),
                Toast.LENGTH_SHORT
            ).show()
            finishPreferencesStep()
        }

        btnAddRole?.setOnClickListener {
            showAddOptionDialog(getString(R.string.candidate_pref_input_role_title)) { newLabel ->
                roles.add(CandidateJobPreferenceOption(newLabel, isSelected = true))
                renderChipGroup(cgRoles, roles) { updated ->
                    roles.clear()
                    roles.addAll(updated)
                }
            }
        }

        btnAddLocation?.setOnClickListener {
            showAddOptionDialog(getString(R.string.candidate_pref_input_location_title)) { newLabel ->
                locations.add(CandidateJobPreferenceOption(newLabel, isSelected = true))
                renderChipGroup(cgLocations, locations) { updated ->
                    locations.clear()
                    locations.addAll(updated)
                }
            }
        }

        btnAddJobType?.setOnClickListener {
            showAddOptionDialog(getString(R.string.candidate_pref_input_job_type_title)) { newLabel ->
                jobTypes.add(CandidateJobPreferenceOption(newLabel, isSelected = true))
                renderChipGroup(cgJobTypes, jobTypes) { updated ->
                    jobTypes.clear()
                    jobTypes.addAll(updated)
                }
            }
        }
    }

    private fun finishPreferencesStep() {
        val hostActivity = activity as? CandidateMainActivity
        if (hostActivity != null) {
            hostActivity.completePreferencesAndOpenExplore()
        } else {
            parentFragmentManager.popBackStack()
        }
    }

    private fun showAddOptionDialog(dialogTitle: String, onAdded: (String) -> Unit) {
        val input = EditText(requireContext()).apply {
            hint = getString(R.string.candidate_pref_input_hint)
        }
        AlertDialog.Builder(requireContext())
            .setTitle(dialogTitle)
            .setView(input)
            .setPositiveButton(getString(R.string.candidate_pref_action_confirm)) { _, _ ->
                val value = input.text?.toString()?.trim().orEmpty()
                if (value.isNotEmpty()) {
                    onAdded(value)
                }
            }
            .setNegativeButton(getString(R.string.candidate_pref_action_cancel), null)
            .show()
    }

    companion object {
        private const val DEFAULT_MIN_SALARY = 10
        private const val DEFAULT_MAX_SALARY = 30
    }
}