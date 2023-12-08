package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R
import com.eltex.androidschool.activity.PostActivity.Companion.EXTRA_EDITED_POST_ID
import com.eltex.androidschool.databinding.ActivityEditNoteBinding
import com.eltex.androidschool.utils.toast

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditNoteBinding.inflate(layoutInflater)
        binding.toolbar.title =
            getString(R.string.edit_post_number, intent.getLongExtra(EXTRA_EDITED_POST_ID, 0))
        setContentView(binding.root)

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            val sharedContent: String? = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (sharedContent != null) {
                binding.content.setText(sharedContent)
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> {
                    val content = binding.content.text?.toString().orEmpty()
                    if (content.isNotBlank()) {
                        setResult(
                            RESULT_OK, Intent()
                                .putExtra(Intent.EXTRA_TEXT, content)
                                .putExtra(
                                    EXTRA_EDITED_POST_ID,
                                    intent.getLongExtra(EXTRA_EDITED_POST_ID, 0)
                                )
                        )
                        finish()
                    } else {
                        binding.root.context.toast(R.string.empty_error, true)
                    }
                    true
                }

                else -> false
            }
        }

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}