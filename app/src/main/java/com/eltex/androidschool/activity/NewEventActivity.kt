package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.ActivityEditNoteBinding
import com.eltex.androidschool.utils.toast

class NewEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditNoteBinding.inflate(layoutInflater)
        binding.toolbar.title = getString(R.string.new_event)
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