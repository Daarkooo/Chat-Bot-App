package com.example.chatbotapp

import android.graphics.Bitmap
import com.example.chatbotapp.data.Chat

data class ChatState(
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt: String = "",
    val bitmap: Bitmap? = null
)
