package com.ctacek.yandexschool.doitnow.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.appComponent
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.databinding.FragmentLoginBinding
import com.ctacek.yandexschool.doitnow.utils.Constants.REQUEST_LOGIN_SDK_CODE
import com.ctacek.yandexschool.doitnow.utils.Constants.TOKEN_API
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import javax.inject.Inject


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sdk: YandexAuthSdk
    @Inject
    lateinit var sharedPreferences: SharedPreferencesAppSettings
    private val viewModel: MainViewModel by activityViewModels { requireContext().appComponent.findViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.injectLoginFragment(this)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        sdk = YandexAuthSdk(requireContext(), YandexAuthOptions(requireContext(), true))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
        val intent = sdk.createLoginIntent(loginOptionsBuilder.build())

        binding.yandexAuthButton.setOnClickListener {
            startActivityForResult(intent, REQUEST_LOGIN_SDK_CODE)
        }

        binding.logInButton.setOnClickListener {
            if (sharedPreferences.getCurrentToken() != TOKEN_API) {
                viewModel.deleteAll()
                sharedPreferences.setCurrentToken("Bearer $TOKEN_API")
                sharedPreferences.putRevisionId(0)
            }
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_LOGIN_SDK_CODE) {
            try {
                val yandexAuthToken = sdk.extractToken(resultCode, data)
                if (yandexAuthToken != null) {
                    val curToken = yandexAuthToken.value
                    if (curToken != sharedPreferences.getCurrentToken()) {
                        viewModel.deleteAll()
                        sharedPreferences.setCurrentToken("OAuth ${yandexAuthToken.value}")
                        sharedPreferences.putRevisionId(0)
                    }
                    sharedPreferences.setCurrentToken("OAuth ${yandexAuthToken.value}")
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
            } catch (e: YandexAuthException) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}