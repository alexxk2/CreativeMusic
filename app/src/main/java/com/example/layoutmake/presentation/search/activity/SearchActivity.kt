package com.example.layoutmake.presentation.search.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.layoutmake.R
import com.example.layoutmake.presentation.search.adapter.TrackAdapter
import com.example.layoutmake.databinding.ActivitySearchBinding
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.player.activity.PlayerActivity
import com.example.layoutmake.presentation.search.SearchingState
import com.example.layoutmake.presentation.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModel()

    private var searchInput = ""
    private var tracks = mutableListOf<Track>()

    private lateinit var  trackAdapter: TrackAdapter
    private var isClickAllowed = true
    private lateinit var  searchHistoryAdapter: TrackAdapter

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { viewModel.startSearch(searchInput)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater).also { setContentView(it.root) }



        viewModel.trackList.observe(this){newTrackList ->
            tracks.clear()
            tracks.addAll(newTrackList)
        }

        viewModel.searchingState.observe(this) { state ->
            when (state) {
                SearchingState.Loading -> showProgressBar()

                SearchingState.Done -> showContent()

                SearchingState.Error -> showServerError()

                SearchingState.NoResults -> showNotFoundError()
            }
        }

        viewModel.isHistoryChanged.observe(this){_ ->
                startHistoryRecyclerView()
        }

        startTrackRecyclerView()
        startHistoryRecyclerView()

        with(binding) {

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
                viewModel.startSearch(searchInput)
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
                    startSearchDebounce(s)
                }

                override fun afterTextChanged(s: Editable?) {
                    changeClearButtonVisibility(s)
                }
            })

            searchEditText.setOnFocusChangeListener { _, _ ->
                manageHistoryVisibility(searchEditText.text)
            }

            cleanHistoryButton.setOnClickListener {
                viewModel.clearSearchHistory()

                with(binding){
                    searchHistoryView.visibility = View.GONE
                    cleanHistoryButton.visibility = View.GONE
                }
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
        trackAdapter = TrackAdapter(this,tracks,object : TrackAdapter.TrackActionListener{
            override fun onClickTrack(track: Track) {
                manageClickAction(track)
            }
        })
        with(binding){
            recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            recyclerView.adapter = trackAdapter
            recyclerView.setHasFixedSize(true)
        }
    }

    private fun startHistoryRecyclerView() {

        searchHistoryAdapter = TrackAdapter(this,viewModel.getSearchHistory(),object : TrackAdapter.TrackActionListener{
            override fun onClickTrack(track: Track) {
                manageClickAction(track)
            }
        })

        with(binding) {
            historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            historyRecyclerView.adapter = searchHistoryAdapter
            historyRecyclerView.setHasFixedSize(true)
        }
    }

    private fun clearErrors() {
        with(binding) {
            errorTextMessage.visibility = View.GONE
            errorNotFoundImage.visibility = View.INVISIBLE
            errorSomethingWrongImage.visibility = View.GONE
            updateButton.visibility = View.GONE
        }
    }

    private fun manageHistoryVisibility(s: CharSequence?){

        with(binding){
            val historyCondition = searchEditText.hasFocus() && s?.isEmpty() == true && viewModel.doesHistoryExist()
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

    private fun startSearchDebounce(s:CharSequence?){
        if (!s.isNullOrEmpty()) {
            showProgressBar()
            searchInput = s.toString()
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun showProgressBar(){
        clearErrors()
        binding.recyclerView.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.recyclerView.visibility = View.VISIBLE
    }


    private fun showServerError() {
        with(binding) {
            clearErrors()
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            progressBar.visibility = View.INVISIBLE
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
            progressBar.visibility = View.INVISIBLE
            errorNotFoundImage.visibility = View.VISIBLE
            errorTextMessage.visibility = View.VISIBLE
            errorTextMessage.text =
                getString(R.string.error_message_not_found)
        }
    }

    private fun clickDebounce (): Boolean{
        val current = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed=true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun manageClickAction(track: Track){
        if (clickDebounce()){
            viewModel.addTrackToHistory(track)
            val playerIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
            playerIntent.putExtra(TRACK, track)
            startActivity(playerIntent)
        }
    }

    private fun showContent(){
        clearErrors()
        hideProgressBar()
        binding.progressBar.visibility = View.INVISIBLE
        trackAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val SEARCH_INPUT = "SEARCH_INPUT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val TRACK = "track"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}