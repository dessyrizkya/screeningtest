package com.dessy.screeningtest.ui.guest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dessy.screeningtest.R
import com.dessy.screeningtest.databinding.ActivityGuestBinding
import com.dessy.screeningtest.model.GuestEntity
import com.dessy.screeningtest.model.UserEntity
import com.dessy.screeningtest.ui.event.EventAdapter
import com.dessy.screeningtest.util.Extra

class GuestActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityGuestBinding
    private lateinit var viewModel: GuestViewModel
    private lateinit var adapter: GuestAdapter
    private val listGuest = ArrayList<GuestEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.getParcelableExtra<UserEntity>(Extra.EXTRA_USER)

        binding = ActivityGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(GuestViewModel::class.java)

        viewModel.getGuests().observe(this, Observer {
            showLoading(true)

            for (item in it) {
                listGuest.add(GuestEntity(
                    item.birthdate.toString(), item.name.toString(), item.id.toString().toInt()))
            }

            adapter = GuestAdapter(listGuest, extras!!)

            with(binding) {
                rvGuest.layoutManager = GridLayoutManager(this@GuestActivity, 2, LinearLayoutManager.VERTICAL, false)
                rvGuest.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            showLoading(false)
        })

        binding.swipeRefresh.setOnRefreshListener(this)
    }

    private fun showLoading(load: Boolean) {
        if (load) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onRefresh() {
        recreate()
    }


}