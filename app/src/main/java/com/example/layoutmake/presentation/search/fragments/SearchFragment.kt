package com.example.layoutmake.presentation.search.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.layoutmake.R
import com.example.layoutmake.databinding.FragmentSearchBinding
import com.example.layoutmake.domain.models.Track
import com.example.layoutmake.presentation.search.SearchingState
import com.example.layoutmake.presentation.search.adapter.TrackAdapter
import com.example.layoutmake.presentation.search.view_model.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModel()

    private var searchInput = ""
    private var tracks = mutableListOf<Track>()

    private lateinit var  trackAdapter: TrackAdapter

    private lateinit var  searchHistoryAdapter: TrackAdapter

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.trackList.observe(viewLifecycleOwner){newTrackList ->
            tracks.clear()
            tracks.addAll(newTrackList)
        }

        viewModel.searchingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchingState.Loading -> showProgressBar()

                SearchingState.Done -> showContent()

                SearchingState.Error -> showServerError()

                SearchingState.NoResults -> showNotFoundError()
            }
        }

        viewModel.isHistoryChanged.observe(viewLifecycleOwner){_ ->
            startHistoryRecyclerView()
        }

        startTrackRecyclerView()
        startHistoryRecyclerView()


        with(binding) {

            removeInputButton.setOnClickListener {
                hideKeyboard(it)
                clearRecyclerView()
            }

            updateButton.setOnClickListener {
                viewModel.startSearch(searchInput)
            }

            searchEditText.addTextChangedListener(object: TextWatcher {
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

    private fun changeClearButtonVisibility(input: Editable?) {
        binding.removeInputButton.visibility = if (input.isNullOrEmpty()) View.GONE
        else View.VISIBLE
    }

    private fun clearRecyclerView(){
        clearInput()
        tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun clearInput() {
        binding.searchEditText.setText("")
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun startTrackRecyclerView() {
        trackAdapter = TrackAdapter(requireContext(),tracks,object : TrackAdapter.TrackActionListener{
            override fun onClickTrack(track: Track) {
                manageClickAction(track)
            }
        })
        with(binding){
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = trackAdapter
            recyclerView.setHasFixedSize(true)
        }
    }

    private fun startHistoryRecyclerView() {

        searchHistoryAdapter = TrackAdapter(requireContext(),viewModel.getSearchHistory(),object : TrackAdapter.TrackActionListener{
            override fun onClickTrack(track: Track) {
                manageClickAction(track)
            }
        })

        with(binding) {
            historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
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

            if (historyCondition){
                clearErrors()
                searchHistoryView.visibility = View.VISIBLE
                cleanHistoryButton.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
            else{
                searchHistoryView.visibility = View.GONE
                cleanHistoryButton.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun manageSearchVisibility(s: CharSequence?){
        with(binding){
            val searchCondition = searchEditText.hasFocus() && (s?.isEmpty() == true)
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
            searchJob?.cancel()

            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                viewModel.startSearch(searchInput)
            }
        }
    }

    private fun showProgressBar(){
        clearErrors()
        binding.recyclerView.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        with(binding){
            progressBar.visibility = View.INVISIBLE

            if(!searchEditText.hasFocus() || searchEditText.text.isNullOrEmpty() || searchHistoryView.isVisible){
                recyclerView.visibility = View.GONE
            }
            else recyclerView.visibility = View.VISIBLE
        }

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


    private fun manageClickAction(track: Track){
        if (viewModel.clickDebounce()){
            viewModel.addTrackToHistory(track)
            val action = SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
            findNavController().navigate(action)
        }
    }

    private fun showContent(){
        clearErrors()
        hideProgressBar()
        trackAdapter.notifyDataSetChanged()
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        Log.d("GGG","onDestroyView")
    }

    override fun onResume() {
        super.onResume()
        Log.d("GGG","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("GGG","onPause")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        Log.d("GGG","onViewStateRestored")

    }

    override fun onStart() {
        super.onStart()
        Log.d("GGG","onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d("GGG","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("GGG","onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("GGG","onDetach")
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}