package com.example.worklink.ui.candidate

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.worklink.R
import com.google.android.material.button.MaterialButton

class CandidateProfileFragment : Fragment() {

    private var tvHeaderName: TextView? = null
    private var tvAvatarBadge: TextView? = null
    private var tvName: TextView? = null
    private var tvEmail: TextView? = null
    private var tvPhone: TextView? = null
    private var llCvList: LinearLayout? = null
    private var btnLogout: MaterialButton? = null
    private var btnCreateCv: MaterialButton? = null
    private var btnEditInfo: MaterialButton? = null
    private var btnChangePassword: MaterialButton? = null

    private var fullName: String = ""
    private var email: String = ""
    private var phone: String = ""
    private val cvFiles = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_candidate_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        initProfileData()
        renderProfile()
        renderCvList()
        bindActions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tvHeaderName = null
        tvAvatarBadge = null
        tvName = null
        tvEmail = null
        tvPhone = null
        llCvList = null
        btnLogout = null
        btnCreateCv = null
        btnEditInfo = null
        btnChangePassword = null
    }

    private fun bindViews(root: View) {
        tvHeaderName = root.findViewById(R.id.tv_candidate_profile_header_name)
        tvAvatarBadge = root.findViewById(R.id.tv_candidate_profile_avatar_badge)
        tvName = root.findViewById(R.id.tv_candidate_profile_name)
        tvEmail = root.findViewById(R.id.tv_candidate_profile_email)
        tvPhone = root.findViewById(R.id.tv_candidate_profile_phone)
        llCvList = root.findViewById(R.id.ll_candidate_profile_cv_list)
        btnLogout = root.findViewById(R.id.btn_candidate_profile_logout)
        btnCreateCv = root.findViewById(R.id.btn_candidate_profile_create_cv)
        btnEditInfo = root.findViewById(R.id.btn_candidate_profile_edit_info)
        btnChangePassword = root.findViewById(R.id.btn_candidate_profile_change_password)
    }

    private fun initProfileData() {
        fullName = getString(R.string.candidate_profile_default_name)
        email = getString(R.string.candidate_profile_default_email)
        phone = getString(R.string.candidate_profile_default_phone)
        if (cvFiles.isEmpty()) {
            cvFiles.add(getString(R.string.candidate_profile_default_cv_file))
        }
    }

    private fun renderProfile() {
        tvHeaderName?.text = fullName
        tvName?.text = fullName
        tvEmail?.text = email
        tvPhone?.text = phone
        val initial = fullName.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "C"
        tvAvatarBadge?.text = initial
    }

    private fun renderCvList() {
        val container = llCvList ?: return
        container.removeAllViews()

        if (cvFiles.isEmpty()) {
            val emptyView = TextView(requireContext()).apply {
                text = getString(R.string.candidate_profile_cv_empty)
                textSize = 14f
            }
            container.addView(emptyView)
            return
        }

        cvFiles.forEachIndexed { index, fileName ->
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    if (index > 0) topMargin = dpToPx(8)
                }
            }

            val tvFile = TextView(requireContext()).apply {
                text = fileName
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            val btnDelete = TextView(requireContext()).apply {
                text = getString(R.string.candidate_profile_cv_delete)
                textSize = 13f
                setOnClickListener {
                    val removed = cvFiles.removeAt(index)
                    renderCvList()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.candidate_profile_cv_deleted_toast, removed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            row.addView(tvFile)
            row.addView(btnDelete)
            container.addView(row)
        }
    }

    private fun bindActions() {
        btnLogout?.setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.candidate_profile_logout_toast),
                Toast.LENGTH_SHORT
            ).show()
            activity?.finish()
        }

        btnCreateCv?.setOnClickListener {
            Toast.makeText(
                requireContext(),
                getString(R.string.candidate_profile_create_cv_toast),
                Toast.LENGTH_SHORT
            ).show()
        }

        btnEditInfo?.setOnClickListener {
            showEditInfoDialog()
        }

        btnChangePassword?.setOnClickListener {
            showChangePasswordDialog()
        }
    }

    private fun showEditInfoDialog() {
        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            val pad = dpToPx(16)
            setPadding(pad, pad, pad, 0)
        }

        val edtName = EditText(requireContext()).apply {
            hint = getString(R.string.candidate_profile_hint_name)
            setText(fullName)
        }

        val edtPhone = EditText(requireContext()).apply {
            hint = getString(R.string.candidate_profile_hint_phone)
            setText(phone)
        }

        container.addView(edtName)
        container.addView(edtPhone)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.candidate_profile_dialog_edit_title))
            .setView(container)
            .setPositiveButton(getString(R.string.candidate_profile_action_save)) { _, _ ->
                val newName = edtName.text?.toString()?.trim().orEmpty()
                val newPhone = edtPhone.text?.toString()?.trim().orEmpty()
                if (newName.isNotEmpty()) fullName = newName
                if (newPhone.isNotEmpty()) phone = newPhone
                renderProfile()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.candidate_profile_updated_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(getString(R.string.candidate_profile_action_cancel), null)
            .show()
    }

    private fun showChangePasswordDialog() {
        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            val pad = dpToPx(16)
            setPadding(pad, pad, pad, 0)
        }

        val edtOldPass = EditText(requireContext()).apply {
            hint = getString(R.string.candidate_profile_hint_old_password)
        }

        val edtNewPass = EditText(requireContext()).apply {
            hint = getString(R.string.candidate_profile_hint_new_password)
        }

        container.addView(edtOldPass)
        container.addView(edtNewPass)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.candidate_profile_dialog_password_title))
            .setView(container)
            .setPositiveButton(getString(R.string.candidate_profile_action_save)) { _, _ ->
                val oldPass = edtOldPass.text?.toString()?.trim().orEmpty()
                val newPass = edtNewPass.text?.toString()?.trim().orEmpty()
                if (oldPass.isEmpty() || newPass.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.candidate_profile_password_empty_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.candidate_profile_password_changed_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton(getString(R.string.candidate_profile_action_cancel), null)
            .show()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}