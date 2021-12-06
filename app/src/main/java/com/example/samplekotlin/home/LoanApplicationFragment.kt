package com.example.samplekotlin.home

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.navigation.Navigation
import com.example.samplekotlin.R

class LoanApplicationFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loan_application, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loanAmountText = view.findViewById<EditText>(R.id.loan_amount_text)
        val loanAmountSeekBar = view.findViewById<SeekBar>(R.id.loan_amount_seek_bar)
        val nextButton = view.findViewById<Button>(R.id.next_button)
        val formatValue = String.format(getString(R.string.loan_amount_format), 0)
        loanAmountText.setText(formatValue)
        loanAmountSeekBar.max = 50000
        loanAmountSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(view: SeekBar?, progress: Int, fromUser: Boolean) {
                val format = String.format(getString(R.string.loan_amount_format), progress)
                loanAmountText.setText(format)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        nextButton.setOnClickListener {
            navigateToSendLoadApplication()
        }
    }

    private fun navigateToSendLoadApplication() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_loanApplicationFragment_to_sendLoanApplicationFragment)
        }
    }

}