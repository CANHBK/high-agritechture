package com.example.administrator.glasshouse.ui.dashboard

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.administrator.glasshouse.R
import com.example.administrator.glasshouse.binding.FragmentDataBindingComponent
import com.example.administrator.glasshouse.databinding.BottomSheetAddGateBinding
import com.example.administrator.glasshouse.databinding.BottomSheetDeleteGateBinding
import com.example.administrator.glasshouse.di.Injectable
import com.example.administrator.glasshouse.util.autoCleared
import com.example.administrator.glasshouse.vo.Gate
import com.example.administrator.glasshouse.vo.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteGateBottomSheet : BottomSheetDialogFragment(), Injectable {

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<BottomSheetDeleteGateBinding>()


    companion object {
        private lateinit var gate: Gate
        private lateinit var dashBoardViewModel: DashBoardViewModel
        fun newInstance(gate: Gate, dashBoardViewModel: DashBoardViewModel): DeleteGateBottomSheet {
            this.gate = gate
            this.dashBoardViewModel = dashBoardViewModel
            val f = DeleteGateBottomSheet()
            return f
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<BottomSheetDeleteGateBinding>(
                inflater,
                R.layout.bottom_sheet_delete_gate,
                container,
                false,
                dataBindingComponent
        )

        binding = dataBinding


//        val btnDelete = view.findViewById<Button>(R.id.btn_delete)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.txtWarningMessage.text =
                    Html.fromHtml("Bạn đang chuẩn bị xóa <span style=\"color: red\">${gate.name}</span>, hành động này không thể quay lại, bạn có chắc chắn?", Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.txtWarningMessage.text =
                    Html.fromHtml("Bạn đang chuẩn bị xóa <span style=\"color: red\">${gate.name}</span>, hành động này không thể quay lại, bạn có chắc chắn?")
        }

//        btnDelete.setOnClickListener {
//            dashBoardViewModel.removeGate(gate.serviceTag)
//        }


        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setLifecycleOwner(viewLifecycleOwner)

        binding.viewModel = dashBoardViewModel

        binding.gate = gate

        handleResultDeleteGate()


    }

    private fun handleResultDeleteGate() {
        dashBoardViewModel.removeGate.observe(viewLifecycleOwner, Observer {
            val status = it.status
            when (status) {
                Status.LOADING -> {
                    binding.loading = true
                    binding.hasError = false
                }
                Status.SUCCESS -> {
                    binding.loading = false
                    binding.hasError = false
//                    dismiss()

                }
                Status.ERROR -> {
                    binding.hasError = true
                    binding.loading = false
                    binding.errorMessage = it.message!!
                }
            }
        })
    }

}