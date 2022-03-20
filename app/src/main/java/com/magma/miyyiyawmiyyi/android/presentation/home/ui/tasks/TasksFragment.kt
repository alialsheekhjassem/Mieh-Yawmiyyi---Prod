package com.magma.miyyiyawmiyyi.android.presentation.home.ui.tasks

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.responses.TasksResponse
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.databinding.FragmentTasksBinding
import com.magma.miyyiyawmiyyi.android.model.TaskObj
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import javax.inject.Inject

class TasksFragment : ProgressBarFragments(), RecyclerItemListener<TaskObj> {

    lateinit var binding: FragmentTasksBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var tasksAdapter: TasksAdapter

    private val viewModel: TasksViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TasksViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setup()
        setUpObservers()

        return binding.root
    }

    private fun setUpObservers() {
        viewModel.actions.observe(
            viewLifecycleOwner, EventObserver(
                object : EventObserver.EventUnhandledContent<TasksActions> {
                    override fun onEventUnhandledContent(t: TasksActions) {
                        when (t) {
                            TasksActions.QUIZZES_CLICKED -> {
                                if (tasksAdapter.currentList.isNotEmpty()) {
                                    findNavController().navigate(
                                        TasksFragmentDirections
                                            .actionTasksToQuizzes()
                                    )
                                }
                            }
                        }
                    }
                })
        )
        viewModel.tasksDb.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<List<TaskObj>> {
                override fun onEventUnhandledContent(t: List<TaskObj>) {
                    tasksAdapter.submitList(t)
                    if (t.isNotEmpty()) {
                        binding.progress.visibility = View.GONE
                        binding.txtEmpty.visibility = View.GONE
                    } else {
                        binding.txtEmpty.visibility = View.VISIBLE
                    }
                }
            })
        )
        // listen to api result
        viewModel.response.observe(
            this,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<TasksResponse>> {
                override fun onEventUnhandledContent(t: Resource<TasksResponse>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            binding.progress.visibility = View.VISIBLE
                            binding.txtEmpty.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            binding.progress.visibility = View.GONE
                            val response = t.response as TasksResponse
                            Log.d(TAG, "response: $response")

                            viewModel.deleteAndSaveTasks(response)
                            //setupData(response.items)
                        }
                        is Resource.DataError -> {
                            binding.progress.visibility = View.GONE
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                            showErrorToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            binding.progress.visibility = View.GONE
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "response: $response")
                            showErrorToast(response.toString())
                        }
                    }
                }
            })
        )
    }

    private fun setup() {

        viewModel.loadAllTasks(Const.TYPE_SOCIAL_MEDIA)

        tasksAdapter.setListener(this)
        tasksAdapter.submitList(arrayListOf())
        binding.recyclerTasks.adapter = tasksAdapter

        binding.include2.btnWatchNow.setOnClickListener {
            viewModel.onQuizzesClicked()
        }

        val level = ContactManager.getCurrentInfo()?.activeRound?.let {
            it.config?.tasksPerTicket?.let { tasksCount ->
                binding.progressRemainingTickets.progress = tasksCount
                String.format(
                    getString(R.string.task_level_2_of_5_completed),
                    tasksCount,
                    it.config?.maxTicketsPerContestant ?: 0
                )
            } ?: String.format(
                getString(R.string.task_level_2_of_5_completed),
                0,
                it.config?.maxTicketsPerContestant ?: 0
            )
        } ?: String.format(
            getString(R.string.task_level_2_of_5_completed),
            0, 0
        )
        binding.txtLevel.text = level
    }

    private fun setupData(tasksList: ArrayList<TaskObj>) {
        val list = tasksList.filter { taskObj ->
            taskObj.smTask != null &&
                    taskObj.type.equals(Const.TYPE_SOCIAL_MEDIA)
        }
        tasksAdapter.submitList(list)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

        viewModel.getTasks(limit = 20, offset = 0)
    }

    override fun onItemClicked(item: TaskObj, index: Int) {

    }

    companion object {
        private const val TAG = "TasksFragment"
    }
}