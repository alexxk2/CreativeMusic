package com.example.layoutmake

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.layoutmake.adapters.SearchHistoryAdapter
import com.example.layoutmake.adapters.TrackAdapter
import com.example.layoutmake.databinding.ActivitySearchBinding
import com.example.layoutmake.models.SearchHistory
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
    private lateinit var sharedPref: SharedPreferences

    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var tracks = mutableListOf<Track>()
    private val searchingService = retrofit.create(ITunesSearchApi::class.java)
    private val trackAdapter = TrackAdapter(tracks)
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (key == HISTORY_LIST){
            startHistoryRecyclerView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater).also { setContentView(it.root) }
        sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)


        startTrackRecyclerView()
        startHistoryRecyclerView()

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

            searchEditText.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    manageHistoryVisibility(s)
                    manageSearchVisibility(s)
                }

                override fun afterTextChanged(s: Editable?) {
                    changeClearButtonVisibility(s)
                }
            })

            searchEditText.setOnFocusChangeListener { _, _ ->
                manageHistoryVisibility(searchEditText.text)
            }

            cleanHistoryButton.setOnClickListener {
                sharedPref.edit()
                    .putString(HISTORY_LIST,null)
                    .apply()

                with(binding){
                    searchHistoryView.visibility = View.GONE
                    cleanHistoryButton.visibility = View.GONE
                }

            }
        }


         sharedPref.registerOnSharedPreferenceChangeListener (listener)
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
        binding.removeInputButton.visibility = if (input.isNullOrEmpty()) View.GONE
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

    private fun startTrackRecyclerView() {
        with(binding){
            recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            recyclerView.adapter = trackAdapter
            recyclerView.setHasFixedSize(true)
        }
    }

    private fun startHistoryRecyclerView() {
        val sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPref)
        with(binding) {
            historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            historyRecyclerView.adapter = SearchHistoryAdapter(searchHistory.historyList())
            historyRecyclerView.setHasFixedSize(true)
        }
    }

    private fun clearErrors() {

        with(binding) {
            errorTextMessage.visibility = View.GONE
            errorNotFoundImage.visibility = View.GONE
            errorSomethingWrongImage.visibility = View.GONE
            updateButton.visibility = View.GONE
        }
    }

    private fun manageHistoryVisibility(s: CharSequence?){

        with(binding){
            val trackHistory = sharedPref.getString(HISTORY_LIST,null)
            val historyCondition = searchEditText.hasFocus() && s?.isEmpty() == true && trackHistory!=null
            searchHistoryView.visibility = if (historyCondition) {
                clearErrors()
                View.VISIBLE
            } else View.GONE
            cleanHistoryButton.visibility = if (historyCondition) View.VISIBLE else View.GONE
        }
    }

    private fun manageSearchVisibility(s: CharSequence?){
        with(binding){
            val searchCondition = searchEditText.hasFocus() && s?.isEmpty() == true
            if (searchCondition){
                hideKeyboard(constraintLayout)
                tracks.clear()
                trackAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun startSearch() {

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