package com.ctacek.yandexschool.doitnow.ui.fragment.managetask.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.appComponent
import com.ctacek.yandexschool.doitnow.databinding.FragmentManageTaskBinding


class ManageTaskFragment : Fragment(R.layout.fragment_manage_task) {

    private val viewModel: ManageTaskViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }
    private lateinit var binding: FragmentManageTaskBinding
//    private val args: ManageTaskFragmentArgs by navArgs()
    private var fragmentViewComponent: ManageTaskViewController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentManageTaskBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = binding.root

//        val id = args.newTaskArg

//        if (id != null) {
//            viewModel.getItem(id)
//        } else {
//            viewModel.setItem()
//        }

        fragmentViewComponent = ManageTaskViewController(
            requireContext(),
            findNavController(),
            binding,
            viewLifecycleOwner,
            viewModel
        ).apply {
            setUpViews()
        }
        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentViewComponent = null
    }

}
