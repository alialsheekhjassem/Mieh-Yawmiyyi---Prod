package com.magma.miyyiyawmiyyi.android.presentation.home.ui.quizzes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.magma.miyyiyawmiyyi.android.R
import com.magma.miyyiyawmiyyi.android.databinding.FragmentQuizzesBinding
import com.magma.miyyiyawmiyyi.android.model.TaskObj
import com.magma.miyyiyawmiyyi.android.utils.Const
import dagger.android.support.AndroidSupportInjection
import com.magma.miyyiyawmiyyi.android.utils.EventObserver
import com.magma.miyyiyawmiyyi.android.utils.ViewModelFactory
import javax.inject.Inject

class QuizzesFragment : Fragment() {

    private var _binding: FragmentQuizzesBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var tasksQuizzes: ArrayList<TaskObj> = arrayListOf()

    private var currentQuestionIndex = 0

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
                    tasksQuizzes.addAll(t)

                    if (tasksQuizzes.isNotEmpty()) {
                        val level = String.format(
                            getString(R.string.question_1_of_10),
                            currentQuestionIndex + 1,
                            tasksQuizzes.size
                        )
                        binding.txtLevel.text = level
                        binding.progressRemainingTickets.progress = 1
                        binding.progressRemainingTickets.max = tasksQuizzes.size
                        binding.txtQuestion.text = tasksQuizzes[0].quizTask?.question
                        binding.txtAnswer1.text = tasksQuizzes[0].quizTask?.options?.get(0)
                        binding.txtAnswer2.text = tasksQuizzes[0].quizTask?.options?.get(1)
                        binding.txtAnswer3.text = tasksQuizzes[0].quizTask?.options?.get(2)
                    }
                }
            })
        )
    }

    private fun onSolveQuestion(solvedIndex: Int) {
        if (tasksQuizzes.size > currentQuestionIndex) {

            val correctIndex = tasksQuizzes[currentQuestionIndex].quizTask?.correctIndex
            if (solvedIndex == correctIndex) {
                Toast.makeText(requireActivity(), "True", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "False", Toast.LENGTH_SHORT).show()
            }

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
                findNavController().navigateUp()
            }
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
}