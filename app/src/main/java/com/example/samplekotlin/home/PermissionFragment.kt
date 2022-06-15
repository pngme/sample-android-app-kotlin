package com.example.samplekotlin.home

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
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
import com.example.samplekotlin.R
import com.example.samplekotlin.model.User
import com.pngme.sdk.library.PngmeSdk
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

        val pngmeChecked = wasPngmeChecked()
        usePngmeCheckBox.isChecked = pngmeChecked
        usePngmeCheckBox.isEnabled = !pngmeChecked

        continueButton.setOnClickListener {
            // save state of checkBox
            if (usePngmeCheckBox.isChecked) {
                setPngmeAsChecked()
                if (!smsPermissionGranted() && smsNeverPermanentlyIgnored()) {
                    context?.let {
                        PngmeSdk.resetPermissionFlow(it)
                    }
                }
                val mainActivity = (activity as MainActivity)
                getUser()?.let { user ->
                    PngmeSdk.go(
                        mainActivity,
                        BuildConfig.PNGME_SDK_TOKEN,
                        user.firstName,
                        user.lastName,
                        user.email,
                        user.phoneNumber,
                        "",
                        false,
                        MainActivity.COMPANY_NAME
                    ) {
                        navigateToLoadApplication()
                    }
                }
            } else {
                navigateToLoadApplication()
            }
        }
    }

    private fun smsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(),
            android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
    }

    private fun smsNeverPermanentlyIgnored(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              (activity as MainActivity).shouldShowRequestPermissionRationale(android.Manifest.permission.READ_SMS)
        } else {
            true
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
            BuildConfig.SHARED_PREF_NAME,
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