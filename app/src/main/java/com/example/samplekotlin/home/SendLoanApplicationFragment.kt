package com.example.samplekotlin.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.samplekotlin.R

class SendLoanApplicationFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_loan_application, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeButton = view.findViewById<Button>(R.id.home_button)
        homeButton.setOnClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_sendLoanApplicationFragment_to_homeFragment)
        }
    }
}