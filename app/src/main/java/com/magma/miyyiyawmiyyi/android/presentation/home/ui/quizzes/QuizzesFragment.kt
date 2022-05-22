package com.magma.miyyiyawmiyyi.android.presentation.home.ui.quizzes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.magma.miyyiyawmiyyi.android.MAGMA
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.data.remote.controller.ErrorManager
import com.magma.miyyiyawmiyyi.android.data.remote.controller.Resource
import com.magma.miyyiyawmiyyi.android.data.remote.requests.MarkAsDoneTasksRequest
import com.magma.miyyiyawmiyyi.android.databinding.FragmentQuizzesBinding
import com.magma.miyyiyawmiyyi.android.model.TaskObj
import com.magma.miyyiyawmiyyi.android.presentation.base.ProgressBarFragments
import com.magma.miyyiyawmiyyi.android.utils.Const
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import com.magma.miyyiyawmiyyi.android.utils.user_management.ContactManager
import javax.inject.Inject

class QuizzesFragment : ProgressBarFragments() {

    private var _binding: FragmentQuizzesBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var tasksQuizzes: ArrayList<TaskObj> = arrayListOf()

    private var currentQuestionIndex = 0

    //waiting time settings
    var startTime: Long = 0
    var fraction: Long = 0

    //time in mel
    var maxWaitingTime = 8000
    var minWaitingTime = 3000

    private val request = MarkAsDoneTasksRequest()

    private val viewModel: QuizzesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[QuizzesViewModel::class.java]
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentQuizzesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setup()
        setUpObservers()

        return binding.root
    }

    private fun setup() {
    }

    private fun setUpObservers() {
        viewModel.actions.observe(
            viewLifecycleOwner,
            EventObserver(
                object :
                    EventObserver.EventUnhandledContent<QuizzesActions> {
                    override fun onEventUnhandledContent(t: QuizzesActions) {
                        when (t) {
                            QuizzesActions.ANSWER_1_CLICKED -> {
                                onSolveQuestion(0)
                            }
                            QuizzesActions.ANSWER_2_CLICKED -> {
                                onSolveQuestion(1)
                            }
                            QuizzesActions.ANSWER_3_CLICKED -> {
                                onSolveQuestion(2)
                            }
                            QuizzesActions.BACK_TO_TASKS_CLICKED -> {
                                findNavController().navigateUp()
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
                    tasksQuizzes.clear()
                    val tasksPerTicket =
                        ContactManager.getCurrentInfo()?.currentRound?.config?.tasksPerTicket ?: 0
                    if (tasksPerTicket != 0) {
                        if (tasksPerTicket < t.size)
                            tasksQuizzes.addAll(t.subList(0, tasksPerTicket))
                        else tasksQuizzes.addAll(t)
                    }

                    if (tasksQuizzes.isNotEmpty()) {
                        val level = String.format(
                            getString(R.string.question_1_of_10),
                            currentQuestionIndex + 1,
                            tasksQuizzes.size
                        )
                        binding.txtLevel.text = level
                        binding.progressRemainingTickets.progress = 0
                        binding.progressRemainingTickets.max = tasksQuizzes.size
                        binding.txtQuestion.text = tasksQuizzes[0].quizTask?.question
                        binding.txtAnswer1.text = tasksQuizzes[0].quizTask?.options?.get(0)
                        binding.txtAnswer2.text = tasksQuizzes[0].quizTask?.options?.get(1)
                        binding.txtAnswer3.text = tasksQuizzes[0].quizTask?.options?.get(2)
                    }
                }
            })
        )

        // listen to mark as done tasks api result
        viewModel.response.observe(
            viewLifecycleOwner,
            EventObserver
                (object :
                EventObserver.EventUnhandledContent<Resource<Any?>> {
                override fun onEventUnhandledContent(t: Resource<Any?>) {
                    when (t) {
                        is Resource.Loading -> {
                            showLoadingDialog()
                        }
                        is Resource.Success -> {
                            // response is ok get the data and display it in the list
                            hideLoadingDialog()
                            val response = t.response
                            Log.d(TAG, "response: $response")
                            findNavController().navigateUp()
                        }
                        is Resource.DataError -> {
                            hideLoadingDialog()
                            // usually this happening when there is server error
                            val response = t.response as ErrorManager
                            Log.d(TAG, "response: DataError $response")
                            showErrorToast(response.failureMessage)
                        }
                        is Resource.Exception -> {
                            hideLoadingDialog()
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

    private fun onSolveQuestion(solvedIndex: Int) {
        if (tasksQuizzes.size > currentQuestionIndex) {

            val correctIndex = tasksQuizzes[currentQuestionIndex].quizTask?.correctIndex
            if (solvedIndex == correctIndex) {
                showSuccessToast(getString(R.string.question_true))
                if (viewModel.isEnableAds())
                    showAd()
                else requestMarkAsDoneQuizTask()
            } else {
                showErrorToast(getString(R.string.question_false))
                /*val activity = requireActivity() as HomeActivity
                activity.startGame()
                request.tasks.add(tasksQuizzes[currentQuestionIndex]._id)*/
                if (viewModel.isEnableAds())
                    showAd()
                else requestMarkAsDoneQuizTask()
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
            requestMarkAsDoneQuizTask()
        }
    }

    private fun requestMarkAsDoneQuizTask() {
        request.tasks.add(tasksQuizzes[currentQuestionIndex]._id)
        //Next Question
        currentQuestionIndex++

        if (tasksQuizzes.size > currentQuestionIndex) {
            val level = String.format(
                getString(R.string.question_1_of_10),
                currentQuestionIndex + 1,
                tasksQuizzes.size
            )
            binding.txtLevel.text = level
            binding.progressRemainingTickets.progress += 1
            binding.txtQuestion.text = tasksQuizzes[currentQuestionIndex].quizTask?.question
            binding.txtAnswer1.text =
                tasksQuizzes[currentQuestionIndex].quizTask?.options?.get(0)
            binding.txtAnswer2.text =
                tasksQuizzes[currentQuestionIndex].quizTask?.options?.get(1)
            binding.txtAnswer3.text =
                tasksQuizzes[currentQuestionIndex].quizTask?.options?.get(2)
        } else {
            viewModel.doServerMarkAsDone(request)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)

        viewModel.loadAllTasks(Const.TYPE_QUIZ)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "QuizzesFragment"
    }
}