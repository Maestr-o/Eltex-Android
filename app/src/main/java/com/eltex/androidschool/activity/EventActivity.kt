package com.eltex.androidschool.activity

import androidx.appcompat.app.AppCompatActivity

class EventActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EDITED_EVENT_ID = "EXTRA_EDITED_EVENT_ID"
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val viewModel by viewModels<EventViewModel> {
//            viewModelFactory {
//                initializer {
//                    EventViewModel(
//                        SQLiteEventRepository(AppDb.getInstance(applicationContext).eventsDao)
//                    )
//                }
//            }
//        }
//
//        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
//        setContentView(binding.root)
//
//        val newEventContract =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//                val content = it.data?.getStringExtra(Intent.EXTRA_TEXT)
//                if (content != null) {
//                    viewModel.addEvent(content)
//                }
//            }
//
//        val editEventContract =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//                val content = it.data?.getStringExtra(Intent.EXTRA_TEXT)
//                val id = it.data?.getLongExtra(EXTRA_EDITED_EVENT_ID, 0)
//                if (content != null && id != null) {
//                    viewModel.editEvent(content, id)
//                }
//            }
//
//        binding.newCard.setOnClickListener {
//            newEventContract.launch(Intent(binding.root.context, NewEventActivity::class.java))
//        }
//
//        if (intent.action == Intent.ACTION_SEND) {
//            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
//            intent.removeExtra(Intent.EXTRA_TEXT)
//            if (!text.isNullOrBlank()) {
//                newEventContract.launch(
//                    Intent(this, NewEventActivity::class.java)
//                        .putExtra(Intent.EXTRA_TEXT, text)
//                )
//            }
//        }
//
//        val adapter = EventsAdapter(
//            object : EventsAdapter.EventListener {
//                override fun onLikeClickListener(event: Event) {
//                    viewModel.likeById(event.id)
//                }
//
//                override fun onParticipateClickListener(event: Event) {
//                    viewModel.participateById(event.id)
//                }
//
//                override fun onShareClickListener(event: Event) {
//                    Intent().apply {
//                        action = Intent.ACTION_SEND
//                        putExtra(
//                            Intent.EXTRA_TEXT,
//                            getString(R.string.share_text, event.author, event.content)
//                        )
//                        type = "text/plain"
//                        startActivity(Intent.createChooser(this, null))
//                    }
//                }
//
//                override fun onDeleteClickListener(event: Event) {
//                    viewModel.deleteById(event.id)
//                }
//
//                override fun onEditClickListener(event: Event) {
//                    Intent(binding.root.context, EditEventActivity::class.java).apply {
//                        putExtra(Intent.EXTRA_TEXT, event.content)
//                        putExtra(EXTRA_EDITED_EVENT_ID, event.id)
//                        editEventContract.launch(this)
//                    }
//                }
//            }
//        )
//
//        binding.list.adapter = adapter
//        binding.list.addItemDecoration(
//            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
//        )
//
//        viewModel.uiState
//            .flowWithLifecycle(lifecycle)
//            .onEach {
//                adapter.submitList(it.events)
//            }
//            .launchIn(lifecycleScope)
//    }
//
}