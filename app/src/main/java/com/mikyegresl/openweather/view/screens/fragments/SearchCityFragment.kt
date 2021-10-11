package com.mikyegresl.openweather.view.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikyegresl.openweather.R
import com.mikyegresl.openweather.view.common.BaseFragment

class SearchCityFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presentationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_city, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchCityFragment()
    }
}