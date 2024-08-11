package com.example.palindromeapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.palindromeapp.databinding.ActivityThirdBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding
    private lateinit var userAdapter: UserAdapter
    private var currentPage = 1
    private var totalPages = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter{
            user -> onUserSelected(user)
        }
        binding.userRecyclerView.adapter = userAdapter
        loadUsers(1)

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadUsers(1)
        }

        binding.userRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    val nextPage = currentPage + 1
                    if (nextPage <= totalPages) {
                        loadUsers(nextPage)
                    }
                }
            }
        })
    }

    private fun showEmptyState(show: Boolean) {
        binding.emptyStateTextView.visibility = if (show) View.VISIBLE else View.GONE
        binding.userRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun loadUsers(page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.apiService.getUsers(page, 10)
                withContext(Dispatchers.Main) {
                    if (response.data.isEmpty()) {
                        showEmptyState(true)
                    } else {
                        showEmptyState(false)
                        userAdapter.setUsers(response.data)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ThirdActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onUserSelected(user: User) {
        val intent = Intent()
        intent.putExtra("selectedUser", "${user.first_name} ${user.last_name}")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}