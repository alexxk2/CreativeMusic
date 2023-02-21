package com.example.layoutmake

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.layoutmake.SearchActivity.Companion.SEARCH_INPUT
import com.example.layoutmake.databinding.ActivitySearchBinding


class SearchActivity : AppCompatActivity() {

    companion object{
        const val SEARCH_INPUT = "SEARCH_INPUT"
    }
    lateinit var binding: ActivitySearchBinding
    lateinit var searchInput: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val textWatcher = object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) binding.removeInputButton.visibility = View.VISIBLE
                searchInput = binding.searchEditText.text.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.isNullOrEmpty()) binding.removeInputButton.visibility = View.INVISIBLE
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
        binding.searchEditText.setText(savedInstanceState.getString(SEARCH_INPUT))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_INPUT,searchInput)
    }


    private fun clearInput() {
        binding.searchEditText.setText("")
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}