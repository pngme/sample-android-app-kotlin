package com.example.samplekotlin.home

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.navigation.Navigation
import com.example.samplekotlin.BuildConfig
import com.example.samplekotlin.Constants
import com.example.samplekotlin.R
import com.example.samplekotlin.model.User
import com.pngme.sdk.library.PngmeSdk
import com.pngme.sdk.library.views.PngmeDialogStyle
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class PermissionFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_permission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val continueButton = view.findViewById<Button>(R.id.next_button)
        val usePngmeCheckBox = view.findViewById<CheckBox>(R.id.use_pngme_checkbox)
        val customStyleCheckbox = view.findViewById<CheckBox>(
            R.id.use_pngme_custom_style_checkbox
        )
        val pngmeChecked = wasPngmeChecked()
        usePngmeCheckBox.isChecked = pngmeChecked
        usePngmeCheckBox.isEnabled = !pngmeChecked

        continueButton.setOnClickListener {
            // save state of checkBox
            if (usePngmeCheckBox.isChecked) {
                setPngmeAsChecked()
                val mainActivity = (activity as MainActivity)
                getUser()?.let { user ->
                    // build a simple custom style example
                    val customStyle = PngmeDialogStyle(
                        primaryColor          = ContextCompat.getColor(requireContext(), R.color.purple_500),
                        backgroundColor       = ContextCompat.getColor(requireContext(), R.color.white),
                        textColor             = ContextCompat.getColor(requireContext(), R.color.black),
                        buttonBackgroundColor = ContextCompat.getColor(requireContext(), R.color.teal_200),
                        buttonTextColor       = ContextCompat.getColor(requireContext(), R.color.white),
                        titleTextSize         = 22f,
                        bodyTextSize          = 16f,
                        buttonTextSize        = 18f,
                        customButtonText      = "Let’s Go!"
                    )

                    // choose overload depending on whether custom-style box is checked
                    if (customStyleCheckbox.isChecked) {
                        PngmeSdk.goWithCustomDialog(
                            activity        = mainActivity,
                            clientKey      = BuildConfig.PNGME_SDK_TOKEN,
                            firstName      = user.firstName,
                            lastName       = user.lastName,
                            email          = user.email,
                            phoneNumber    = user.phoneNumber,
                            externalId     = user.externalId,
                            companyName    = MainActivity.COMPANY_NAME,
                            hasAcceptedTerms = true,
                            onComplete     = { navigateToLoadApplication() },
                            dialogStyle    = customStyle
                        )
                    } else {
                        PngmeSdk.goWithCustomDialog(
                            activity        = mainActivity,
                            clientKey       = BuildConfig.PNGME_SDK_TOKEN,
                            firstName       = user.firstName,
                            lastName        = user.lastName,
                            email           = user.email,
                            phoneNumber     = user.phoneNumber,
                            externalId      = user.externalId,
                            companyName     = MainActivity.COMPANY_NAME,
                            hasAcceptedTerms= true,
                            onComplete      = { navigateToLoadApplication() }
                        )
                    }
                }
            } else {
                navigateToLoadApplication()
            }
        }
    }

    private fun setPngmeAsChecked() {
        getSharedPreference()?.edit {
            putBoolean("pngmeChecked", true)
            apply()
        }
    }

    private fun wasPngmeChecked(): Boolean =
        getSharedPreference()?.getBoolean("pngmeChecked", false) ?: false

    private fun getUser(): User? {
        val userJson = getSharedPreference()?.getString("userInfo", null) ?: return null
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<User> = moshi.adapter(User::class.java)
        return jsonAdapter.fromJson(userJson)
    }

    private fun getSharedPreference(): SharedPreferences? {
        return activity?.getSharedPreferences(
            Constants.SHARED_PREF_NAME,
            MODE_PRIVATE
        )
    }

    private fun navigateToLoadApplication() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_permissionFragment_to_loanApplicationFragment)
        }
    }
}