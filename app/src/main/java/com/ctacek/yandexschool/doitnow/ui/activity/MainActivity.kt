package com.ctacek.yandexschool.doitnow.ui.activity

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.utils.localeLazy
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk

class MainActivity : AppCompatActivity() {

    private val sharedPreferencesAppSettings: SharedPreferencesAppSettings by localeLazy()

    private lateinit var register: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = getRootNavController()
        prepareRootNavController(isSignedIn(), navController)
    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        return navHost.navController
    }

    private fun prepareRootNavController(isSignedIn: Boolean, navController: NavController) {
        val graph = navController.navInflater.inflate(getMainNavigationGraphId())
        graph.setStartDestination(
            if (isSignedIn) {
                getMainDestination()
            } else {
                getSignInDestination()
            }
        )
        navController.graph = graph
    }

    private fun getMainNavigationGraphId(): Int = R.navigation.main_graph

    private fun getMainDestination(): Int = R.id.mainFragment

    private fun getSignInDestination(): Int = R.id.loginFragment

    private fun isSignedIn(): Boolean {
        val currentUser = sharedPreferencesAppSettings.getCurrentToken()
        if (currentUser != null) return true
        return false
    }
}