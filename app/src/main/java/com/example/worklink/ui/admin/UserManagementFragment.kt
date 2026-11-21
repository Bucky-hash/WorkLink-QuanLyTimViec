
package com.example.worklink.ui.admin

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worklink.R
import com.example.worklink.core.base.BaseFragment

class UserManagementFragment :
    BaseFragment(R.layout.fragment_admin_user_management) {

    private lateinit var edtSearch: EditText
    private lateinit var rvUsers: RecyclerView

    private lateinit var tvAll: TextView
    private lateinit var tvCandidate: TextView
    private lateinit var tvRecruiter: TextView

    private var selectedRole = "ALL"

    private val users = listOf(
        UserItem(
            "Trương Văn Chiến",
            "truongvanchien@gmail.com",
            "Ứng viên",
            "Hoạt động"
        ),
        UserItem(
            "Nguyễn Bảo Phúc",
            "nguyenbaophuc@gmail.com",
            "Ứng viên",
            "Đã khóa"
        ),
        UserItem(
            "Nguyễn Minh Nghĩa",
            "nguyenminhnghia@gmail.com",
            "Ứng viên",
            "Hoạt động"
        ),
        UserItem(
            "Công ty ABC",
            "ctyABC@gmail.com",
            "Nhà tuyển dụng",
            "Hoạt động"
        )
    )

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        edtSearch = view.findViewById(R.id.edt_admin_users_search)
        rvUsers = view.findViewById(R.id.rv_admin_users)

        tvAll = view.findViewById(R.id.tv_admin_filter_all)
        tvCandidate = view.findViewById(R.id.tv_admin_filter_candidate)
        tvRecruiter = view.findViewById(R.id.tv_admin_filter_recruiter)

        rvUsers.layoutManager = LinearLayoutManager(requireContext())

        tvAll.setOnClickListener {
            selectRole("ALL")
        }

        tvCandidate.setOnClickListener {
            selectRole("Ứng viên")
        }

        tvRecruiter.setOnClickListener {
            selectRole("Nhà tuyển dụng")
        }

        edtSearch.doOnTextChanged { text, _, _, _ ->
            filterUsers(text.toString())
        }

        updateFilterUI()
        filterUsers("")
    }

    private fun selectRole(role: String) {
        selectedRole = role
        updateFilterUI()
        filterUsers(edtSearch.text.toString())
    }

    private fun updateFilterUI() {
        val filters = listOf(
            tvAll to "ALL",
            tvCandidate to "Ứng viên",
            tvRecruiter to "Nhà tuyển dụng"
        )

        filters.forEach { (textView, role) ->
            val isSelected = selectedRole == role

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

    private fun filterUsers(keyword: String) {
        val result = users.filter { user ->
            val matchesRole =
                selectedRole == "ALL" || user.role == selectedRole

            val matchesKeyword =
                user.name.contains(keyword, ignoreCase = true) ||
                        user.email.contains(keyword, ignoreCase = true)

            matchesRole && matchesKeyword
        }

        rvUsers.adapter = UserAdapter(result)
    }

    private data class UserItem(
        val name: String,
        val email: String,
        val role: String,
        val status: String
    )

    private inner class UserAdapter(
        private val items: List<UserItem>
    ) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

        inner class UserViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            val avatar: TextView =
                itemView.findViewById(R.id.tv_admin_user_avatar)

            val name: TextView =
                itemView.findViewById(R.id.tv_admin_user_name)

            val email: TextView =
                itemView.findViewById(R.id.tv_admin_user_email)

            val role: TextView =
                itemView.findViewById(R.id.tv_admin_user_role)

            val status: TextView =
                itemView.findViewById(R.id.tv_admin_user_status)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): UserViewHolder {
            val itemView = layoutInflater.inflate(
                R.layout.item_admin_user,
                parent,
                false
            )

            return UserViewHolder(itemView)
        }

        override fun onBindViewHolder(
            holder: UserViewHolder,
            position: Int
        ) {
            val user = items[position]

            holder.avatar.text =
                user.name.firstOrNull()?.uppercase() ?: "?"

            holder.name.text = user.name
            holder.email.text = user.email
            holder.role.text = user.role
            holder.status.text = user.status

            if (user.status == "Đã khóa") {
                holder.status.setTextColor(
                    requireContext().getColor(R.color.wl_error)
                )

                holder.status.setBackgroundResource(
                    R.color.wl_field_bg
                )
            } else {
                holder.status.setTextColor(
                    requireContext().getColor(R.color.wl_primary_dark)
                )

                holder.status.setBackgroundResource(
                    R.color.wl_primary_light
                )
            }
        }

        override fun getItemCount(): Int = items.size
    }
}
