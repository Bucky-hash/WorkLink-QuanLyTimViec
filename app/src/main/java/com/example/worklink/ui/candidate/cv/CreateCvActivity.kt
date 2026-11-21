package com.example.worklink.ui.candidate.cv

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import com.example.worklink.R
import com.example.worklink.core.base.BaseActivity
import com.example.worklink.core.ext.dp
import com.example.worklink.core.ext.hideKeyboard
import com.example.worklink.core.ext.toast
import com.example.worklink.core.ui.ChatFab
import com.example.worklink.ui.candidate.CandidateMainActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView

/** Ứng viên - Tạo CV (Figma "Ứng viên - Tạo CV"). Màn riêng, không nằm trong tab. */
class CreateCvActivity : BaseActivity() {

    // DỮ LIỆU GIẢ: đổi sang repository thật khi có backend.
    private val repository: CvRepository = FakeCvRepository()

    private lateinit var scroll: ScrollView
    private lateinit var avatar: ShapeableImageView
    private lateinit var avatarInitials: TextView

    private lateinit var cvName: CvField
    private lateinit var fullName: CvField
    private lateinit var birth: CvField
    private lateinit var gender: CvField
    private lateinit var email: CvField
    private lateinit var phone: CvField
    private lateinit var address: CvField
    private lateinit var website: CvField
    private lateinit var position: CvField
    private lateinit var objective: CvField
    private lateinit var skillInput: CvField
    private lateinit var skillGroup: ChipGroup

    private lateinit var educationContainer: LinearLayout
    private lateinit var experienceContainer: LinearLayout

    private val educationEntries = mutableListOf<EducationEntry>()
    private val experienceEntries = mutableListOf<ExperienceEntry>()

    private var avatarUri: String? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) onImagePicked(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_cv)

        bindViews()
        bindActions()
        bindDraft(repository.initialDraft())
    }

    // ---------------------------------------------------------------- khởi tạo

    private fun bindViews() {
        scroll = findViewById(R.id.cv_scroll)
        avatar = findViewById(R.id.cv_avatar)
        avatarInitials = findViewById(R.id.cv_avatar_initials)

        cvName = findViewById(R.id.cv_name)
        fullName = findViewById(R.id.cv_full_name)
        birth = findViewById(R.id.cv_birth)
        gender = findViewById(R.id.cv_gender)
        email = findViewById(R.id.cv_email)
        phone = findViewById(R.id.cv_phone)
        address = findViewById(R.id.cv_address)
        website = findViewById(R.id.cv_website)
        position = findViewById(R.id.cv_position)
        objective = findViewById(R.id.cv_objective)
        skillInput = findViewById(R.id.cv_skill_input)
        skillGroup = findViewById(R.id.cv_skill_group)
        educationContainer = findViewById(R.id.cv_education_container)
        experienceContainer = findViewById(R.id.cv_experience_container)
    }

    private fun bindActions() {
        findViewById<View>(R.id.btn_cv_back).setOnClickListener { finish() }
        findViewById<View>(R.id.tv_cv_save_draft_top).setOnClickListener { saveDraft() }
        findViewById<View>(R.id.btn_cv_save_draft).setOnClickListener { saveDraft() }
        findViewById<View>(R.id.btn_cv_save).setOnClickListener { saveCv() }
        // Chưa có màn xem trước / mục bổ sung trong Figma.
        findViewById<View>(R.id.btn_cv_preview).setOnClickListener { toast(R.string.auth_feature_wip) }
        findViewById<View>(R.id.btn_add_section).setOnClickListener { toast(R.string.auth_feature_wip) }

        findViewById<ChatFab>(R.id.cv_chat_fab).apply {
            setUnreadCount(3) // số giả theo Figma
            setOnClickListener { toast(R.string.auth_feature_wip) }
        }

        findViewById<View>(R.id.cv_change_photo).setOnClickListener { pickImage.launch("image/*") }
        fullName.editText.doAfterTextChanged { updateInitials() }

        birth.onActionClick = { pickDate(birth, CvDates.FULL_DATE, R.string.cv_pick_birth) }
        gender.onActionClick = { pickOption(gender, R.array.cv_gender_options) }

        findViewById<View>(R.id.btn_add_education).setOnClickListener {
            val root = addEducation(CvEducation()).root
            root.post { scrollTo(root) }
        }
        findViewById<View>(R.id.btn_add_experience).setOnClickListener {
            val root = addExperience(CvExperience()).root
            root.post { scrollTo(root) }
        }

        skillInput.onActionClick = { addSkill(skillInput.text) }
        skillInput.editText.imeOptions = EditorInfo.IME_ACTION_DONE
        skillInput.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addSkill(skillInput.text)
                true
            } else {
                false
            }
        }
    }

    // ---------------------------------------------------------------- đổ / đọc dữ liệu

    private fun bindDraft(draft: CvDraft) {
        cvName.text = draft.title
        fullName.text = draft.fullName
        birth.text = draft.birthDate
        gender.text = draft.gender
        email.text = draft.email
        phone.text = draft.phone
        address.text = draft.address
        website.text = draft.website
        position.text = draft.targetPosition
        objective.text = draft.careerObjective

        avatarUri = draft.avatarUri
        draft.avatarUri?.let {
            avatar.setImageURI(Uri.parse(it))
            avatarInitials.visibility = View.GONE
        }
        updateInitials()

        educationContainer.removeAllViews()
        educationEntries.clear()
        draft.educations.forEach { addEducation(it) }

        experienceContainer.removeAllViews()
        experienceEntries.clear()
        draft.experiences.forEach { addExperience(it) }

        skillGroup.removeAllViews()
        draft.skills.forEach { addSkillChip(it) }
    }

    private fun collectDraft() = CvDraft(
        title = cvName.text,
        fullName = fullName.text,
        birthDate = birth.text,
        gender = gender.text,
        email = email.text,
        phone = phone.text,
        address = address.text,
        website = website.text,
        targetPosition = position.text,
        careerObjective = objective.text,
        avatarUri = avatarUri,
        educations = educationEntries.map { it.read() },
        experiences = experienceEntries.map { it.read() },
        skills = skillGroup.children.filterIsInstance<Chip>().map { it.text.toString() }.toList()
    )

    // ---------------------------------------------------------------- lưu

    private fun saveDraft() {
        currentFocus?.hideKeyboard()
        when (val result = repository.saveDraft(collectDraft())) {
            is CvResult.Success -> toast(R.string.cv_saved_draft)
            is CvResult.Failure -> toast(result.messageRes)
        }
    }

    private fun saveCv() {
        currentFocus?.hideKeyboard()
        val firstInvalid = validate()
        if (firstInvalid != null) {
            scrollTo(firstInvalid)
            return
        }
        when (val result = repository.saveCv(collectDraft())) {
            is CvResult.Success -> {
                toast(R.string.cv_saved)
                startActivity(
                    Intent(this, CandidateMainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }
            is CvResult.Failure -> toast(result.messageRes)
        }
    }

    /** Kiểm tra các ô có dấu *; trả về ô lỗi đầu tiên (từ trên xuống) hoặc null nếu hợp lệ. */
    private fun validate(): CvField? {
        var first: CvField? = null

        fun check(field: CvField, extra: ((String) -> Int?)? = null) {
            val value = field.text
            val error = if (value.isBlank()) R.string.cv_error_required else extra?.invoke(value)
            field.setError(error?.let { getString(it) })
            if (error != null && first == null) first = field
        }

        check(cvName)
        check(fullName)
        check(email) { value ->
            if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) null else R.string.auth_error_email_invalid
        }
        check(phone) { value ->
            if (value.count { it.isDigit() } in 9..15) null else R.string.cv_error_phone_invalid
        }
        check(position)
        educationEntries.forEach {
            check(it.school)
            check(it.major)
        }
        experienceEntries.forEach {
            check(it.company)
            check(it.position)
        }
        return first
    }

    private fun scrollTo(target: View) {
        var y = 0
        var view: View = target
        while (view !== scroll) {
            y += view.top
            view = view.parent as? View ?: break
        }
        scroll.smoothScrollTo(0, (y - dp(16)).coerceAtLeast(0))
        if (target is CvField) target.requestFocus()
    }

    // ---------------------------------------------------------------- ảnh đại diện

    private fun onImagePicked(uri: Uri) {
        val type = contentResolver.getType(uri)
        if (type != "image/jpeg" && type != "image/png") {
            toast(R.string.cv_error_photo_type)
            return
        }
        val size = contentResolver.openAssetFileDescriptor(uri, "r")?.use { it.length } ?: -1L
        if (size > MAX_PHOTO_BYTES) {
            toast(R.string.cv_error_photo_size)
            return
        }
        avatarUri = uri.toString()
        avatar.setImageURI(uri)
        avatarInitials.visibility = View.GONE
    }

    /** Chữ viết tắt: chữ cái đầu của tối đa 3 từ trong họ tên (Trương Văn Chiến -> TVC). */
    private fun updateInitials() {
        val letters = fullName.text.split(Regex("\\s+"))
            .filter { it.isNotEmpty() }
            .take(3)
            .map { it.first().uppercaseChar() }
        avatarInitials.text = if (letters.isEmpty()) "CV" else letters.joinToString("")
    }

    // ---------------------------------------------------------------- chọn ngày / lựa chọn

    private fun pickDate(field: CvField, pattern: String, titleRes: Int) {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(titleRes)
            .apply { CvDates.parse(field.text, pattern)?.let { setSelection(it) } }
            .build()
        picker.addOnPositiveButtonClickListener { millis ->
            field.text = CvDates.format(millis, pattern)
        }
        picker.show(supportFragmentManager, TAG_DATE_PICKER)
    }

    private fun pickOption(field: CvField, optionsRes: Int) {
        val options = resources.getStringArray(optionsRes)
        MaterialAlertDialogBuilder(this)
            .setTitle(field.labelText.removeSuffix(" *"))
            .setSingleChoiceItems(options, options.indexOf(field.text)) { dialog, which ->
                field.text = options[which]
                dialog.dismiss()
            }
            .show()
    }

    // ---------------------------------------------------------------- học vấn / kinh nghiệm

    private fun addEducation(data: CvEducation): EducationEntry {
        val root = layoutInflater.inflate(R.layout.item_cv_education, educationContainer, false)
        // Các khối dùng chung id con, không để hệ thống lưu / khôi phục theo id.
        root.isSaveFromParentEnabled = false
        val entry = EducationEntry(root)
        entry.degree.onActionClick = { pickOption(entry.degree, R.array.cv_degree_options) }
        entry.start.onActionClick = { pickDate(entry.start, CvDates.MONTH_YEAR, R.string.cv_pick_month) }
        entry.end.onActionClick = { pickDate(entry.end, CvDates.MONTH_YEAR, R.string.cv_pick_month) }
        entry.write(data)
        educationContainer.addView(root)
        educationEntries += entry
        return entry
    }

    private fun addExperience(data: CvExperience): ExperienceEntry {
        val root = layoutInflater.inflate(R.layout.item_cv_experience, experienceContainer, false)
        root.isSaveFromParentEnabled = false
        val entry = ExperienceEntry(root)
        entry.start.onActionClick = { pickDate(entry.start, CvDates.MONTH_YEAR, R.string.cv_pick_month) }
        entry.end.onActionClick = { pickDate(entry.end, CvDates.MONTH_YEAR, R.string.cv_pick_month) }
        entry.current.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                entry.end.text = getString(R.string.cv_current)
                entry.end.setFieldEnabled(false)
            } else {
                if (entry.end.text == getString(R.string.cv_current)) entry.end.text = ""
                entry.end.setFieldEnabled(true)
            }
        }
        entry.write(data)
        experienceContainer.addView(root)
        experienceEntries += entry
        return entry
    }

    private class EducationEntry(val root: View) {
        val school: CvField = root.findViewById(R.id.edu_school)
        val major: CvField = root.findViewById(R.id.edu_major)
        val degree: CvField = root.findViewById(R.id.edu_degree)
        val start: CvField = root.findViewById(R.id.edu_start)
        val end: CvField = root.findViewById(R.id.edu_end)
        val description: CvField = root.findViewById(R.id.edu_desc)

        fun write(data: CvEducation) {
            school.text = data.school
            major.text = data.major
            degree.text = data.degree
            start.text = data.start
            end.text = data.end
            description.text = data.description
        }

        fun read() = CvEducation(
            school = school.text,
            major = major.text,
            degree = degree.text,
            start = start.text,
            end = end.text,
            description = description.text
        )
    }

    private class ExperienceEntry(val root: View) {
        val company: CvField = root.findViewById(R.id.exp_company)
        val position: CvField = root.findViewById(R.id.exp_position)
        val start: CvField = root.findViewById(R.id.exp_start)
        val end: CvField = root.findViewById(R.id.exp_end)
        val current: CheckBox = root.findViewById(R.id.exp_current)
        val description: CvField = root.findViewById(R.id.exp_desc)

        fun write(data: CvExperience) {
            company.text = data.company
            position.text = data.position
            start.text = data.start
            description.text = data.description
            end.text = data.end
            // Đặt sau cùng: bật "đang làm việc" sẽ tự điền "Hiện tại" và khóa ô Kết thúc.
            current.isChecked = data.isCurrent
        }

        fun read() = CvExperience(
            company = company.text,
            position = position.text,
            start = start.text,
            end = if (current.isChecked) "" else end.text,
            isCurrent = current.isChecked,
            description = description.text
        )
    }

    // ---------------------------------------------------------------- kỹ năng

    private fun addSkill(raw: String) {
        val skill = raw.trim()
        if (skill.isEmpty()) return
        val exists = skillGroup.children.filterIsInstance<Chip>()
            .any { it.text.toString().equals(skill, ignoreCase = true) }
        if (!exists) addSkillChip(skill)
        skillInput.text = ""
    }

    private fun addSkillChip(skill: String) {
        val chip = layoutInflater.inflate(R.layout.item_cv_skill_chip, skillGroup, false) as Chip
        chip.text = skill
        chip.closeIconContentDescription = getString(R.string.cd_remove_skill, skill)
        chip.setOnCloseIconClickListener { skillGroup.removeView(chip) }
        skillGroup.addView(chip)
    }

    private companion object {
        const val MAX_PHOTO_BYTES = 5L * 1024 * 1024
        const val TAG_DATE_PICKER = "cv_date_picker"
    }
}
