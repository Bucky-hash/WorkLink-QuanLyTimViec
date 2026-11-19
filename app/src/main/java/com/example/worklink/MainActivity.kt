package com.example.worklink

import android.content.Intent
import android.os.Bundle
import com.example.worklink.core.base.BaseActivity
import com.example.worklink.ui.admin.AdminMainActivity
import com.example.worklink.ui.candidate.CandidateMainActivity
import com.example.worklink.ui.employer.EmployerMainActivity

/** TẠM THỜI: chọn vai trò để thử điều hướng. Thay bằng Giới thiệu / Đăng nhập khi các màn đó xong. */
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<android.view.View>(R.id.btn_candidate).setOnClickListener {
            startActivity(Intent(this, CandidateMainActivity::class.java))
        }
        findViewById<android.view.View>(R.id.btn_employer).setOnClickListener {
            startActivity(Intent(this, EmployerMainActivity::class.java))
        }
        findViewById<android.view.View>(R.id.btn_admin).setOnClickListener {
            startActivity(Intent(this, AdminMainActivity::class.java))
        }
    }
}
