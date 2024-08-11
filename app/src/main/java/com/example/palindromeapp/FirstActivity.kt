package com.example.palindromeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.palindromeapp.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkButton.setOnClickListener {
            val input = binding.sentenceInput.text.toString()
            if (isPalindrome(input)) {
                showDialogPalindrome("isPalindrome")
            } else {
                showDialogPalindrome("isNotPalindrome")
            }
        }

        binding.nextButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name", binding.nameInput.text.toString())
            startActivity(intent)
        }
    }

    private fun isPalindrome(sentence: String): Boolean {
        val cleaned = sentence.replace("\\s".toRegex(), "").lowercase()
        return cleaned == cleaned.reversed()
    }

    private fun showDialogPalindrome(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}