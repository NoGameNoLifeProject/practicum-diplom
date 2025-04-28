package ru.practicum.android.diploma.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchVacanciesFragment, R.id.favVacanciesFragment, R.id.creatorsFragment -> {
                    binding.bottomNav.isVisible = true
                    binding.bottomNavDelimiter.isVisible = true
                }

                else -> {
                    binding.bottomNav.isVisible = false
                    binding.bottomNavDelimiter.isVisible = false
                }
            }
        }
    }

}
