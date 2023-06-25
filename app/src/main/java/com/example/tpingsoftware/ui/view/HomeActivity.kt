package com.example.tpingsoftware.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.tpingsoftware.R
import com.example.tpingsoftware.databinding.ActivityHomeBinding
import com.example.tpingsoftware.ui.viewModels.HomeVIewModel
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.system.exitProcess


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel: HomeVIewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSignOut.setOnClickListener {
            viewModel.SignOut()
        }

        initView()
    }

    private fun initView(){
        val tabsAdapter = TabsFragmentAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        tabsAdapter.addItem(ServiceFragment(), "Servicios")
        tabsAdapter.addItem(MyServiceFragment(), "Mis Servicios")
        binding.viewPager.adapter = tabsAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
        exitProcess(0)

    }

}