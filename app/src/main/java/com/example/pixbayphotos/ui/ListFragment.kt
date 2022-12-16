package com.example.pixbayphotos.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.pixbayphotos.R
import com.example.pixbayphotos.databinding.FragmentListBinding
import com.example.pixbayphotos.models.Image
import com.example.pixbayphotos.ui.viewmodel.ImageFinderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ImageFinderViewModel by viewModels()
    private val adapter: ImagesAdapter by lazy { ImagesAdapter(::onClick) }

    private fun onClick(image: Image) {
        showConfirmationDialog(image)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }


    private fun showConfirmationDialog(image: Image) = AlertDialog.Builder(requireContext())
        .setTitle(getString(R.string.string_confirmation))
        .setMessage(getString(R.string.string_confirmation_message))
        .setPositiveButton(
            getString(R.string.string_yes)
        ) { _, _ ->
            run {
                val action = ListFragmentDirections.actionListFragmentToDetailsFragment(image)
                findNavController().navigate(action)
            }
        }
        .create()
        .show()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            recyclerview.setHasFixedSize(true)
            recyclerview.adapter = adapter.withLoadStateFooter(
                footer = ImagesLoadStateAdapter {adapter.retry()}
            )
        }
        initEditText()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStatePagedData.collectLatest {
                    it?.let { nonNullData ->
                        adapter.submitData(lifecycle, nonNullData)
                    }

                }
            }
        }
    }

    private fun initEditText() {

        binding.imagesSearchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (! charSequence.isNullOrEmpty() && charSequence.length > 3) {
                    viewModel.getResults(charSequence.toString())
                }
            }override fun afterTextChanged(p0: Editable?) {}

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun test() {

    }
}