package com.example.tpingsoftware.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import com.example.tpingsoftware.R
import com.example.tpingsoftware.databinding.ActivityHomeBinding
import com.example.tpingsoftware.ui.view.adapters.TabsFragmentAdapter
import com.example.tpingsoftware.ui.viewModels.HomeVIewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.system.exitProcess


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel: HomeVIewModel by viewModel()

    var viewBottomSheet: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btnBurger -> {
                setBottomSheet()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView(){

        setSupportActionBar(binding.toolbarHome)

        viewBottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null, false)

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

    private fun setBottomSheet() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        viewBottomSheet?.let { bottomSheet ->

            if (bottomSheet.parent != null) {
                (bottomSheet.parent as ViewGroup).removeView(bottomSheet)
            }

            dialog.setContentView(bottomSheet)
        }
        dialog.show()

        viewBottomSheet?.findViewById<ImageView>(R.id.ivClose)?.setOnClickListener {
            dialog.dismiss()
        }

        viewBottomSheet?.findViewById<TextView>(R.id.tvSignOut)?.setOnClickListener {
            viewModel.SignOut()
        }

        viewBottomSheet?.findViewById<TextView>(R.id.tvProfile)?.setOnClickListener {
            viewModel.goToEditProfile()
        }

    }

}