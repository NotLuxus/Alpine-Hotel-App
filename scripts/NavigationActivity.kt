package com.example.hotel_03.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.hotel_03.R
import com.example.hotel_03.application.MyApplication
import com.example.hotel_03.databinding.ActivityNavigationBinding
import com.example.hotel_03.viewmodels.FaqViewModel
import com.example.hotel_03.viewmodels.ProfiloViewModel

//Activity che contiene e gestisce tutte le funzionalità dell'app

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding
    private lateinit var navController: NavController

    private val viewModelFaq: FaqViewModel by viewModels<FaqViewModel>()
    private val viewModelProfilo: ProfiloViewModel by viewModels<ProfiloViewModel>(){
        ProfiloViewModel.factory(
            (applicationContext as MyApplication).sharedPreferences
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inizializza il View Binding
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configura la menù bar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        //Richedono al db dati che non vengono salvati in locale ma che servono durante l'utilizzo dell'app
        viewModelProfilo.getUserPaymentMethod()
        viewModelFaq.getFaq()

        }

    }
