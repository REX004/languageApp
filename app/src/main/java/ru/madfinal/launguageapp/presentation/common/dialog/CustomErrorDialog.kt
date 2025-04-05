package ru.madfinal.launguageapp.presentation.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import ru.madfinal.launguageapp.databinding.DialogErrorBinding

class CustomErrorDialog(private val context: Context) : Dialog(context) {

    private val binding: DialogErrorBinding by lazy { DialogErrorBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        applyClick()
    }


    private fun applyClick() {
        binding.apply {
            backBt.setOnClickListener {
                this@CustomErrorDialog.dismiss()
            }
            okBt.setOnClickListener {
                this@CustomErrorDialog.dismiss()
            }
        }
    }
}