package com.shriram.reactinterviewapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun goToIntroducation(view: View) {
        startActivity(Intent(applicationContext, IntroducationActivity::class.java))
    }
    fun goToAdvancedConcepts(view: View) {
        startActivity(Intent(applicationContext, AdvancedTopicsActivity::class.java))
    }
    fun goToReactHooks(view: View) {
        startActivity(Intent(applicationContext, ReactHooksActivity::class.java))
    }
    fun goToRoutingWithReactRouter(view: View) {
        startActivity(Intent(applicationContext, RoutingWithReactRouterActivity::class.java))
    }
    fun goToStylingInReact(view: View) {
        startActivity(Intent(applicationContext, StylingInReactActivity::class.java))
    }
    fun goToStateManagement(view: View) {
        startActivity(Intent(applicationContext, StateManagementActivity::class.java))
    }
    fun goToHandlingForms(view: View) {
        startActivity(Intent(applicationContext, HandlingFormsActivity::class.java))
    }
    fun goToWorkingWithAPIs(view: View) {
        startActivity(Intent(applicationContext, WorkingWithAPIsActivity::class.java))
    }
    fun goToPerformanceOptimization(view: View) {
        startActivity(Intent(applicationContext, PerformanceOptimizationActivity::class.java))
    }
    fun goToTestingInReact(view: View) {
        startActivity(Intent(applicationContext, TestingInReactActivity::class.java))
    }
    fun goToReactAndBackendIntegration(view: View) {
        startActivity(Intent(applicationContext, ReactAndBackendIntegrationActivity::class.java))
    }
    fun goToAdvancedTopics(view: View) {
        startActivity(Intent(applicationContext, AdvancedTopicsActivity::class.java))
    }
    fun goToBuildingAProject(view: View) {
        startActivity(Intent(applicationContext, BuildingAProjectActivity::class.java))
    }
    fun goToDeployment(view: View) {
        startActivity(Intent(applicationContext, DeploymentActivity::class.java))
    }
}