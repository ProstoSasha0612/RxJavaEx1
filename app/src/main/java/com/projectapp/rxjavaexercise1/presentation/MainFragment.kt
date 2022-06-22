package com.projectapp.rxjavaexercise1.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.projectapp.rxjavaexercise1.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(this.requireActivity().application as App)
        )[MainViewModel::class.java]
    }
    private var _binding: MainFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val testList = listOf(
            "https://ismycomputeronfire.com/",
            "https://ismycomputeronfire.com/",
            "http://www.ismycomputeron.com/",

            "https://fdsfsd", // bad url request

            "https://google.com",
            "https://yandex.by",
            "https://ismycomputeronfire.com/",
            "https://ismycomputeronfire.com/",
            "http://www.ismycomputeron.com/",
            "https://google.com",
            "https://yandex.by",
            "https://ismycomputeronfire.com/",
            "https://ismycomputeronfire.com/",
            "http://www.ismycomputeron.com/",
            "https://google.com",
            "https://yandex.by",
            )

        setupResultStateListener()

        binding.button.setOnClickListener {
            viewModel.getContent(urlList = testList)
        }
    }

    private fun setupResultStateListener(){
        viewModel.urlsAnswerList.observe(this.viewLifecycleOwner){
            binding.tvResult.text = it.last()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
