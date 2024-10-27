package com.adyen.android.assignment.ui

import androidx.activity.ComponentActivity
import com.adyen.android.assignment.PlanetsRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val planetsRepository: PlanetsRepository by inject<PlanetsRepository>()

    override fun onStart() {
        //todo remove this test code
        super.onStart()
        GlobalScope.launch {
            planetsRepository.getImagePlanets()
        }
    }
}