package com.example.worklink.ui.placeholder

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.worklink.R
import com.example.worklink.core.base.BaseFragment

/** Màn tạm cho các tab chưa làm. Khi làm xong màn nào thì thay trong createFragment của Activity tương ứng. */
class PlaceholderFragment : BaseFragment(R.layout.fragment_placeholder) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireArguments().getString(ARG_TITLE).orEmpty()
        view.findViewById<TextView>(R.id.placeholder_text).text =
            getString(R.string.placeholder_screen, title)
    }

    companion object {
        private const val ARG_TITLE = "title"

        fun newInstance(title: String) = PlaceholderFragment().apply {
            arguments = Bundle().apply { putString(ARG_TITLE, title) }
        }
    }
}
