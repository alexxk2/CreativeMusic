package com.example.layoutmake

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.layoutmake.adapters.TrackAdapter
import com.example.layoutmake.databinding.ActivitySearchBinding
import com.example.layoutmake.models.Track
import com.example.layoutmake.sources.ITunesSearchApi
import com.example.layoutmake.sources.entities.ResponseEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_INPUT = "SEARCH_INPUT"
    }

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchInput: String
    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var tracks = mutableListOf<Track>()
    private val searchingService = retrofit.create(ITunesSearchApi::class.java)
    private val trackAdapter = TrackAdapter(tracks)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater).also { setContentView(it.root) }

        startRecyclerView()

        with(binding) {
            searchEditText.setOnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (view.text.isNotEmpty()) {
                        searchInput = view.text.toString()
                        startSearch()
                    }
                    true
                }
                false

            }

            removeInputButton.setOnClickListener {
                clearInput()
                hideKeyboard(it)
                tracks.clear()
                trackAdapter.notifyDataSetChanged()
            }

            arrowBackButton.setOnClickListener {
                finish()
            }

            updateButton.setOnClickListener {
                startSearch()
            }

            searchEditText.addTextChangedListener {
                changeClearButtonVisibility(it)
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

    private fun changeClearButtonVisibility(input: Editable?) {
        binding.removeInputButton.visibility = if (input.isNullOrEmpty()) View.INVISIBLE
        else View.VISIBLE
    }

    private fun clearInput() {
        binding.searchEditText.setText("")
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun startRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = trackAdapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun clearErrors() {

        with(binding) {
            errorTextMessage.visibility = View.INVISIBLE
            errorNotFoundImage.visibility = View.INVISIBLE
            errorSomethingWrongImage.visibility = View.INVISIBLE
            updateButton.visibility = View.INVISIBLE
        }
    }

    private fun startSearch() {

        with(binding)
        {
            searchingService
                .findSong(searchInput)
                .enqueue(object : Callback<ResponseEntity> {
                    override fun onResponse(
                        call: Call<ResponseEntity>,
                        response: Response<ResponseEntity>
                    ) {
                        if (response.code() == 200) {
                            clearErrors()
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                trackAdapter.notifyDataSetChanged()
                            }
                        } else {
                            showServerError()
                        }

                        if (tracks.isEmpty()) {
                            showNotFoundError()
                        }
                    }

                    override fun onFailure(call: Call<ResponseEntity>, t: Throwable) {
                        showServerError()
                    }
                })
        }
    }

    private fun showServerError() {
        with(binding) {
            clearErrors()
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            errorSomethingWrongImage.visibility = View.VISIBLE
            errorTextMessage.visibility = View.VISIBLE
            errorTextMessage.text =
                getString(R.string.error_message_something_went_wrong)
            updateButton.visibility = View.VISIBLE
        }
    }

    private fun showNotFoundError() {
        with(binding) {
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            errorNotFoundImage.visibility = View.VISIBLE
            errorTextMessage.visibility = View.VISIBLE
            errorTextMessage.text =
                getString(R.string.error_message_not_found)
        }
    }
}