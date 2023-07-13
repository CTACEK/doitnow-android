package com.ctacek.yandexschool.doitnow.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.appComponent
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.databinding.FragmentSettingsBinding
import com.ctacek.yandexschool.doitnow.utils.notificationmanager.NotificationScheduler
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferencesAppSettings

    @Inject
    lateinit var notificationScheduler: NotificationScheduler

    private val themeOptions: Array<String> by lazy {
        resources.getStringArray(R.array.theme_options)
    }
    private val themeLabels: Array<String> by lazy {
        resources.getStringArray(R.array.theme_labels)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        requireContext().appComponent.injectSettingsFragment(this)
        prepareSwitchStatus()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun prepareSwitchStatus() {
        val status = sharedPreferences.getNotificationStatus()
        binding.switchAllowNotification.isChecked = status ?: false
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            themeOptionsValue.text = sharedPreferences.getThemeMode()

            switchAllowNotification.setOnCheckedChangeListener { _, status ->
                if (status) {
                    sharedPreferences.putNotificationStatus(true)
                } else {
                    sharedPreferences.putNotificationStatus(false)
                    notificationScheduler.cancelAll()
                }
            }

            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            themeOption.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(
                    ContextThemeWrapper(
                        context, R.style.AlertDialogCustom
                    )
                )

                val currentTheme = sharedPreferences.getThemeMode()

                val selectedIndex = themeOptions.indexOf(currentTheme)
                builder.apply {
                    setTitle(getString(R.string.choose_theme_title))

                    setSingleChoiceItems(themeLabels, selectedIndex) { dialog, which ->
                        val selectedTheme = themeOptions[which]
                        sharedPreferences.putThemeMode(selectedTheme)
                        themeOptionsValue.text = selectedTheme
                        dialog.dismiss()
                    }
                }
                builder.show().create()
            }

            logoutButton.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(
                    ContextThemeWrapper(
                        context, R.style.AlertDialogCustom
                    )
                )
                builder.apply {
                    val title = context.getString(R.string.you_want_get_out)
                    setMessage(title)
                    setPositiveButton(context.getString(R.string.logout_button)) { _, _ ->
                        findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
                    }
                }
                builder.show().create()
            }

        }


    }
}
