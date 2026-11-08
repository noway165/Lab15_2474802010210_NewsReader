package com.vlu.newsreader

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonParseException
import com.vlu.newsreader.databinding.ActivityMainBinding
import com.vlu.newsreader.model.NewsItem
import java.io.IOException
import kotlinx.coroutines.launch
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.btnManual.setOnClickListener { load(NewsJsonParser::parseManual) }
        binding.btnGson.setOnClickListener { load(NewsJsonParser::parseGson) }

        // Step 6 only: adb shell am start -n com.vlu.newsreader/.MainActivity -e debug mainthread
        // Throws NetworkOnMainThreadException (or SecurityException in the noInternet build).
        if (intent.getStringExtra("debug") == "mainthread") HttpClient.getBlocking(URL)

        load(NewsJsonParser::parseGson)
    }

    private fun load(parse: (String) -> List<NewsItem>) {
        showLoading()
        lifecycleScope.launch {
            try {
                adapter.submitList(parse(HttpClient.get(URL)))
                binding.progressBar.visibility = View.GONE
                binding.tvError.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            } catch (e: IOException) {
                Log.e(TAG, "network failure", e)
                showError(getString(R.string.err_network))
            } catch (e: JSONException) {
                Log.e(TAG, "manual parse failure", e)
                showError(getString(R.string.err_parse))
            } catch (e: JsonParseException) {
                Log.e(TAG, "gson parse failure", e)
                showError(getString(R.string.err_parse))
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
    }

    private fun showError(msg: String) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.tvError.text = msg
        binding.tvError.visibility = View.VISIBLE
    }

    private companion object {
        const val URL = "https://jsonplaceholder.typicode.com/posts"
        const val TAG = "NewsReader"
    }
}
