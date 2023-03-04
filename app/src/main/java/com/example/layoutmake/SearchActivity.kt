package com.example.layoutmake

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.layoutmake.adapters.TrackAdapter
import com.example.layoutmake.databinding.ActivitySearchBinding
import com.example.layoutmake.models.Track
import com.example.layoutmake.models.makeTrackList


class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_INPUT = "SEARCH_INPUT"
    }

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchInput: String
    private lateinit var dataSet: ArrayList<Track>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater).also { setContentView(it.root) }

        startRecyclerView()

        val textWatcher = object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                changeClearButtonVisibility(p0)
                searchInput = binding.searchEditText.text.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        with(binding) {
            searchEditText.addTextChangedListener(textWatcher)
            removeInputButton.setOnClickListener {
                clearInput()
                hideKeyboard(it)
            }
            arrowBackButton.setOnClickListener {
                finish()
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInput = savedInstanceState.getString(SEARCH_INPUT).toString()
        binding.searchEditText.setText(searchInput)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_INPUT, searchInput)
    }


    private fun changeClearButtonVisibility(p0: CharSequence?) {
        if (p0.isNullOrEmpty()) binding.removeInputButton.visibility = View.INVISIBLE
        else binding.removeInputButton.visibility = View.VISIBLE
    }

    private fun clearInput() {
        binding.searchEditText.setText("")
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun startRecyclerView(){
        dataSet = makeTrackList()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = TrackAdapter(dataSet)
        binding.recyclerView.setHasFixedSize(true)
    }

}