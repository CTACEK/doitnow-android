package com.ctacek.yandexschool.doitnow.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.appComponent
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.databinding.FragmentLoginBinding
import com.ctacek.yandexschool.doitnow.utils.Constants.REQUEST_LOGIN_SDK_CODE
import com.ctacek.yandexschool.doitnow.utils.Constants.TOKEN_API
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk
import javax.inject.Inject


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private lateinit var register: ActivityResultLauncher<Intent>

    @Inject
    lateinit var sharedPreferences: SharedPreferencesAppSettings

    @Inject
    lateinit var yandexSdk : YandexAuthSdk

    private val viewModel: LoginViewModel by viewModels { requireContext().appComponent.findViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.injectLoginFragment(this)
        binding = FragmentLoginBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == REQUEST_LOGIN_SDK_CODE) {
                    val data: Intent? = result.data
                    if (data != null) {
                        try {
                            val yandexAuthToken = yandexSdk.extractToken(result.resultCode, data)
                            if (yandexAuthToken != null) {
                                val curToken = yandexAuthToken.value
                                if (curToken != sharedPreferences.getCurrentToken()) {
                                    viewModel.deleteCurrentItems()
                                    sharedPreferences.setCurrentToken("OAuth ${yandexAuthToken.value}")
                                    sharedPreferences.putRevisionId(0)
                                }
                                sharedPreferences.setCurrentToken("OAuth ${yandexAuthToken.value}")
                                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                            }
                        } catch (e: YandexAuthException) {
                            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        binding.yandexAuthButton.setOnClickListener {
            register.launch(yandexSdk.createLoginIntent(YandexAuthLoginOptions.Builder().build()))
        }

        binding.logInButton.setOnClickListener {
            if (sharedPreferences.getCurrentToken() != TOKEN_API) {
                viewModel.deleteCurrentItems()
                sharedPreferences.setCurrentToken("Bearer $TOKEN_API")
                sharedPreferences.putRevisionId(0)
            }
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
    }
}
