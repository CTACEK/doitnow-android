package com.ctacek.yandexschool.doitnow.ui.fragment.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.appComponent
import com.ctacek.yandexschool.doitnow.databinding.FragmentMainBinding


class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }
    private lateinit var binding: FragmentMainBinding
    private var fragmentViewComponent: MainFragmentViewController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMainBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = binding.root
        fragmentViewComponent = MainFragmentViewController(
            requireContext(),
            findNavController(),
            binding,
            viewLifecycleOwner,
            viewModel
        ).apply {
            setUpViews()
        }

        viewModel.loadData()
        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentViewComponent = null
    }

}
