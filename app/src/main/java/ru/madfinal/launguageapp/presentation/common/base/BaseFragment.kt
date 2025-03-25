package ru.madfinal.launguageapp.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding>(private val bindingInflater: (inflater: LayoutInflater, container: ViewGroup?, attachRoot: Boolean) -> VB) :
    Fragment() {

    private var _binding: VB? = null
    val binding: VB
        get() = _binding
            ?: throw IllegalStateException("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun backPressed() {
        requireActivity().onBackPressed()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    protected fun handleError(message: String) {
        showToast(message)
    }

    open fun applyClick() {

    }

}