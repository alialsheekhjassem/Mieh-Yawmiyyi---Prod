package com.magma.miyyiyawmiyyi.android.presentation.home.ui.tasks

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.magma.miyyiyawmiyyi.android.MAGMA
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.MarkAsDoneTasksRequest
import com.magma.miyyiyawmiyyi.android.data.remote.responses.GroupedTasksResponse
import com.magma.miyyiyawmiyyi.android.data.remote.responses.TasksResponse
import com.magma.miyyiyawmiyyi.android.databinding.DialogDisableTasksBinding
import com.magma.miyyiyawmiyyi.android.databinding.FragmentTasksBinding
import com.magma.miyyiyawmiyyi.android.model.TaskObj
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.presentation.home.ui.confirm_task.ConfirmEndTaskFragment
import com.magma.miyyiyawmiyyi.android.utils.Const
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.OnConfirmTaskListener
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.listeners.RecyclerItemListener
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class TasksFragment : ProgressBarFragments(), RecyclerItemListener<TaskObj> {

    lateinit var binding: FragmentTasksBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var tasksAdapter: TasksAdapter

    private var quizzesTasks: ArrayList<TaskObj> = arrayListOf()
    private var adTasks: ArrayList<TaskObj> = arrayListOf()

    private var currentTask: TaskObj? = null

    private var isTaskOpened = false
    private var taskId: String? = null

    //waiting time settings
    var startTime: Long = 0
    var fraction: Long = 0

    //time in mel
    var maxWaitingTime = 8000
    var minWaitingTime = 3000

    private var isGeneratedTasks = false

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
                                findNavController().navigate(
                                    TasksFragmentDirections
                                        .actionTasksToQuizzes()
                                )
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
                    var type: String? = null
                    if (t.isNotEmpty()) {
                        type = t.first().type
                        binding.progress.visibility = View.GONE
                        binding.txtEmpty.visibility = View.GONE
                    } /*else {
                        binding.txtEmpty.visibility = View.VISIBLE
                    }*/

                    when (type) {
                        Const.TYPE_SOCIAL_MEDIA -> {
                            if (t.isEmpty()){
                                viewModel.getTasks(limit = 20, offset = 0, false, Const.TYPE_SOCIAL_MEDIA)
                            }
                            tasksAdapter.submitList(t)
                        }
                        Const.TYPE_AD -> {
                            if (t.isEmpty()){
                                viewModel.getTasks(limit = 20, offset = 0, false, Const.TYPE_AD)
                            }
                            adTasks.clear()
                            adTasks.addAll(t)
                        }
                        Const.TYPE_QUIZ -> {
                            if (t.isEmpty()){
                                viewModel.getTasks(limit = 20, offset = 0, false, Const.TYPE_QUIZ)
                            }
                            quizzesTasks.clear()
                            quizzesTasks.addAll(t)
                        }
                    }
                }
            })
        )
        // listen to api result
        viewModel.response.observe(
            viewLifecycleOwner,
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

                            var type: String? = null
                            if (response.items.isNotEmpty())
                                type = response.items.first().type
                            else if (type == Const.TYPE_SOCIAL_MEDIA) {
                                binding.txtEmpty.text = getString(R.string.no_items)
                                binding.txtEmpty.visibility = View.VISIBLE
                            }

                            viewModel.deleteAndSaveTasks(response.items, type)

                            /*if (!isGeneratedTasks && response.items.isEmpty())
                                viewModel.generateTasks()*/
                            /*else {
                                //Quizzes & Ad Tasks
                                adTasks =
                                    response.items.filter { taskObj -> taskObj.type == Const.TYPE_AD }
                                quizzesTasks =
                                    response.items.filter { taskObj -> taskObj.type == Const.TYPE_QUIZ }
                            }*/
                        }
                        is Resource.DataError -> {
                            binding.progress.visibility = View.GONE
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                            binding.recyclerTasks.visibility = View.GONE
                            if (response.status == 400) {
                                binding.txtEmpty.text = getString(R.string.no_active_round)
                                binding.txtEmpty.visibility = View.VISIBLE
                            } else if (response.status == 409) {
                                viewModel.deleteAllTasks()
                            }
                            //showErrorToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            binding.progress.visibility = View.GONE
                            binding.recyclerTasks.visibility = View.GONE
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "response: $response")
                            showErrorToast(response.toString())
                        }
                    }
                }
            })
        )
        // listen to api result
        viewModel.responseGroupedTasks.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<ArrayList<GroupedTasksResponse>>> {
                override fun onEventUnhandledContent(t: Resource<ArrayList<GroupedTasksResponse>>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            binding.progress.visibility = View.VISIBLE
                            binding.txtEmpty.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            binding.progress.visibility = View.GONE
                            val response = t.response as ArrayList<GroupedTasksResponse>
                            Log.d(TAG, "response: $response")

                            if (response.isNotEmpty() && response.first().socialMedias.isNotEmpty()) {
                                binding.txtEmpty.text = getString(R.string.no_items)
                                binding.txtEmpty.visibility = View.VISIBLE
                            }

                            val groupedTasks = arrayListOf<TaskObj>()
                            response.first().socialMedias.forEach {
                                val taskObj = TaskObj(
                                    it._id ?: "",
                                    Const.TYPE_SOCIAL_MEDIA, null, it,
                                    null, null, null, false, null, null
                                )
                                groupedTasks.add(taskObj)
                            }
                            response.first().quizzes.forEach {
                                val taskObj = TaskObj(
                                    it._id ?: "",
                                    Const.TYPE_QUIZ, null, null,
                                    it, null, null, false, null, null
                                )
                                groupedTasks.add(taskObj)
                            }
                            response.first().ads.forEach {
                                val taskObj = TaskObj(
                                    it._id ?: "",
                                    Const.TYPE_AD, null, null,
                                    null, it, null, false, null, null
                                )
                                groupedTasks.add(taskObj)
                            }

                            viewModel.deleteAndSaveTasks(groupedTasks)

                        }
                        is Resource.DataError -> {
                            binding.progress.visibility = View.GONE
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                            binding.recyclerTasks.visibility = View.GONE
                            if (response.status == 400) {
                                binding.txtEmpty.text = getString(R.string.no_active_round)
                                binding.txtEmpty.visibility = View.VISIBLE
                            } else if (response.status == 409) {
                                viewModel.deleteAllTasks()
                            }
                            //showErrorToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            binding.progress.visibility = View.GONE
                            binding.recyclerTasks.visibility = View.GONE
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "response: $response")
                            showErrorToast(response.toString())
                        }
                    }
                }
            })
        )
        // listen to api result
        viewModel.responseGenerate.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<Any?>> {
                override fun onEventUnhandledContent(t: Resource<Any?>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            binding.progress.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            binding.progress.visibility = View.GONE
                            val response = t.response
                            Log.d(TAG, "responseGenerate: $response")

                            isGeneratedTasks = true

                            viewModel.getTasks(
                                limit = 20,
                                offset = 0,
                                false,
                                Const.TYPE_SOCIAL_MEDIA
                            )
                            viewModel.getTasks(limit = 20, offset = 0, false, Const.TYPE_AD)
                            viewModel.getTasks(limit = 20, offset = 0, false, Const.TYPE_QUIZ)
                        }
                        is Resource.DataError -> {
                            binding.progress.visibility = View.GONE
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "responseGenerate: DataError $response")
                            binding.recyclerTasks.visibility = View.GONE
                            showErrorToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            binding.progress.visibility = View.GONE
                            binding.recyclerTasks.visibility = View.GONE
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "responseGenerate: $response")
                            showErrorToast(response.toString())
                        }
                    }
                }
            })
        )
        // listen to api result
        viewModel.responseMarkAsDone.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<Any?>> {
                override fun onEventUnhandledContent(t: Resource<Any?>) {
                    when (t) {
                        is Resource.Loading -> {
                            // show progress bar and remove no data layout while loading
                            showLoadingDialog()
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            hideLoadingDialog()
                            val response = t.response
                            Log.d(TAG, "responseGenerate: $response")
                            when (currentTask?.type) {
                                Const.TYPE_AD -> {
                                    adTasks.remove(currentTask)
                                }
                                Const.TYPE_QUIZ -> {
                                    quizzesTasks.remove(currentTask)
                                }
                            }
                            taskId?.let {
                                viewModel.deleteTask(it)
                            }

                            //increase tasks count or reset
                            val tasksLocalCount = viewModel.getTasksCount()
                            val currentTasksCount = tasksLocalCount + 1
                            val tasksPerTicket =
                                ContactManager.getCurrentInfo()?.currentRound?.config?.tasksPerTicket
                                    ?: 0
                            if (currentTasksCount >= tasksPerTicket) {
                                viewModel.setTasksCount(0)
                            } else {
                                viewModel.setTasksCount(currentTasksCount)
                            }

                            refreshTasksBar()
                        }
                        is Resource.DataError -> {
                            hideLoadingDialog()
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "responseGenerate: DataError $response")
                            showErrorToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            hideLoadingDialog()
                            // usually this happening when there is no internet
                            val response = t.response
                            Log.d(TAG, "responseGenerate: $response")
                            showErrorToast(response.toString())
                        }
                    }
                }
            })
        )
    }

    private fun setup() {

        if (ContactManager.getCurrentInfo()?.currentRound != null) {
            viewModel.loadAllTasks(Const.TYPE_SOCIAL_MEDIA)
            viewModel.loadAllTasks(Const.TYPE_AD)
            viewModel.loadAllTasks(Const.TYPE_QUIZ)
        }

        tasksAdapter.setListener(this)
        tasksAdapter.submitList(arrayListOf())
        binding.recyclerTasks.adapter = tasksAdapter

        binding.include2.btnWatchNow.setOnClickListener {
            if (quizzesTasks.isNotEmpty())
                viewModel.onQuizzesClicked()
            else showToast(getString(R.string.no_items_))
        }

        refreshTasksBar()

        //Set Settings
        if (!viewModel.isShowAdTask()) {
            binding.include1.btnWatchNow.visibility = View.INVISIBLE
        } else {
            binding.include1.btnWatchNow.visibility = View.VISIBLE
        }
        if (!viewModel.isShowQuizTask()) {
            binding.include2.btnWatchNow.visibility = View.INVISIBLE
        } else {
            binding.include2.btnWatchNow.visibility = View.VISIBLE
        }
        if (!viewModel.isShowSocialMediaTask()) {
            binding.recyclerTasks.visibility = View.GONE
        } else {
            binding.recyclerTasks.visibility = View.VISIBLE
        }

        binding.include1.btnWatchNow.setOnClickListener {
            //val activity = requireActivity() as HomeActivity
            //activity.startGame()
            Log.d(TAG, "MPL setup: adTasks $adTasks")
            Log.d(TAG, "MPL setup: quizzesTasks $quizzesTasks")
            if (adTasks.isNotEmpty()) {
                if (viewModel.isEnableAds())
                    showAd()
                else requestMarkAsDoneAdTask()
            } else {
                refreshTasksBar()
                //showErrorToast(getString(R.string.reach_max_tickets))
            }
        }
    }

    private fun refreshTasksBar() {
        val tasksLocalCount = viewModel.getTasksCount()
        val level = ContactManager.getCurrentInfo()?.currentRound?.let {
            it.config?.tasksPerTicket?.let { tasksCount ->
                binding.progressRemainingTasks.progress = tasksLocalCount
                binding.progressRemainingTasks.max = tasksCount
                String.format(
                    getString(R.string.task_level_2_of_5_completed),
                    tasksLocalCount,
                    tasksCount
                )
            } ?: String.format(
                getString(R.string.task_level_2_of_5_completed),
                0, 0
            )
        } ?: String.format(
            getString(R.string.task_level_2_of_5_completed),
            0, 0
        )
        binding.txtLevel.text = level
        ContactManager.getCurrentInfo()?.let {
            val tasksPerTicket = it.currentRound?.config?.tasksPerTicket ?: 0
            if (it.currentRound == null) {
                viewModel.setTasksCount(0)
                showUpdateDialog(getString(R.string.no_active_round))
            } else if (tasksLocalCount >= tasksPerTicket) {
                viewModel.setTasksCount(0)
            } else if ((it.currentRoundTickets ?: 0)
                == (it.currentRound.config?.maxTicketsPerContestant ?: 0)
            ) {
                showUpdateDialog(getString(R.string.reach_max_tickets))
            }
        }
    }

    private fun showAd() {
        if (MAGMA.getInstance().rewardedAd == null && !MAGMA.getInstance().isLoading) {
            Toast.makeText(requireContext(), "just a second", Toast.LENGTH_SHORT).show()
            MAGMA.getInstance()
            return
        }

        MAGMA.getInstance().rewardedAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    MAGMA.getInstance().rewardedAd = null
                    showErrorToast(adError.message)
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    MAGMA.getInstance().rewardedAd = null
                    startTime = System.currentTimeMillis()
                    //waiting time
                    fraction =
                        (Math.random() * (maxWaitingTime - minWaitingTime)).toLong() + minWaitingTime
                    // Preload the next rewarded ad.
                    MAGMA.getInstance().loadRewardedAd()
                }
            }

        MAGMA.getInstance().rewardedAd?.show(
            requireActivity()
        ) {
            //Mark As Done
            requestMarkAsDoneAdTask()
        }
    }

    private fun requestMarkAsDoneAdTask() {
        if (adTasks.isNotEmpty()) {
            val request = MarkAsDoneTasksRequest()
            request.tasks.add(adTasks.first()._id)
            currentTask = adTasks.first()
            viewModel.doServerMarkAsDone(request)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isTaskOpened) {
            isTaskOpened = false
            val timer = Timer()
            val task: TimerTask = object : TimerTask() {
                override fun run() {
                    if (isAdded) {
                        val fm = requireActivity().supportFragmentManager
                        val dialog = ConfirmEndTaskFragment()
                        fm.beginTransaction().commitAllowingStateLoss()
                        dialog.setOnConfirmTaskListener(object : OnConfirmTaskListener {
                            override fun onConfirm() {
                                //Mark as Done
                                taskId?.let { id ->
                                    val request = MarkAsDoneTasksRequest()
                                    request.tasks.add(id)
                                    viewModel.doServerMarkAsDone(request)
                                }
                            }
                        })
                        try {
                            dialog.show(fm, "Dialog")
                        } catch (ignored: IllegalStateException) {
                        }
                    }
                }
            }
            //[force waiting]
            try {
                showLoadingDialog()
            } catch (ignored: Exception) {
            }
            //[timing]
            val handler = Handler()
            handler.postDelayed(
                { hideLoadingDialog() },
                3650
            )
            timer.schedule(task, 4000)
        }
    }

    private fun showUpdateDialog(title: String) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding: DialogDisableTasksBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()), R.layout.dialog_disable_tasks, null, false
        )
        builder.setView(dialogBinding.root)
        val alertDialog = builder.create()
        dialogBinding.txtBody.text = title
        dialogBinding.btnBack.setOnClickListener {
            findNavController().navigateUp()
            alertDialog.dismiss()
        }
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

        viewModel.getGroupedTasks(limit = 20)
        /*viewModel.getTasks(limit = 20, offset = 0, false, Const.TYPE_SOCIAL_MEDIA)
        viewModel.getTasks(limit = 20, offset = 0, false, Const.TYPE_AD)
        viewModel.getTasks(limit = 20, offset = 0, false, Const.TYPE_QUIZ)*/
    }

    override fun onItemClicked(item: TaskObj, index: Int) {
        Log.d(TAG, "onItemClicked: app " + item.smTask?.app?._id)
        Log.d(TAG, "onItemClicked: link " + item.smTask?.link)
        Log.d(TAG, "onItemClicked: action " + item.smTask?.action)
        when (item.smTask?.app?._id) {
            Const.INSTAGRAM_ID -> {
                item.smTask?.link?.let { openWebUrl(it) }
            }
            Const.YOUTUBE_ID -> {
                item.smTask?.link?.let { openWebUrl(it) }
            }
            else -> {
                item.smTask?.link?.let { openWebUrl(it) }
            }
        }
        isTaskOpened = true
        taskId = item._id
    }

    companion object {
        private const val TAG = "TasksFragment"
    }
}