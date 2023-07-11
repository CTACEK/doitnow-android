package com.ctacek.yandexschool.doitnow.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.appComponent
import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.utils.Constants
import com.ctacek.yandexschool.doitnow.utils.PeriodWorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferencesAppSettings: SharedPreferencesAppSettings


    private lateinit var navController: NavController

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle("navControllerState", navController.saveState())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applicationContext.appComponent.injectMainActivity(this)

        navController = getRootNavController()

        if (savedInstanceState != null) {
            navController.restoreState(savedInstanceState.getBundle("navControllerState"))
        } else {
            prepareRootNavController(isSignedIn(), navController)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        periodicUpdate()
    }

    private fun periodicUpdate() {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val myWorkRequest = PeriodicWorkRequest.Builder(
            PeriodWorkManager::class.java,
            Constants.REPEAT_INTERVAL,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .addTag(Constants.WORK_MANAGER_TAG)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            Constants.WORK_MANAGER_TAG,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            myWorkRequest
        )
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
        if (currentUser != Constants.SHARED_PREFERENCES_NO_TOKEN) return true
        return false
    }
}
