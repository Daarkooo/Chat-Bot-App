package com.example.chatbotapp

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbotapp.data.Chat
import com.example.chatbotapp.data.ChatData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    fun onEvent(event: ChatUiEvent){
        when(event){
            is ChatUiEvent.SendPrompt -> {
                if(event.prompt.isNotEmpty()){
                    addPrompt(event.prompt, event.bitmap)

                    if (event.bitmap != null){
                        getReponseWithImage(event.prompt, event.bitmap)
                    } else{
                        getReponse(event.prompt)
                    }
                }

            }

            is ChatUiEvent.UpdatePrompt -> { // edit the text
                _chatState.update {
                    it.copy(prompt = event.newPrompt)
                }
            }
        }
    }

    private fun addPrompt(prompt: String, bitmap: Bitmap?){ // ? -> may be not available
        _chatState.update {
            it.copy(
                chatList = it.chatList.toMutableList().apply {
                    add(0, Chat(prompt, bitmap, true))
                },
                prompt = "",
                bitmap = null
            )
        }
    }

    private fun getReponse(prompt: String){
        viewModelScope.launch {
            val chat = ChatData.getResponse(prompt)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat)
                    },
                )
            }
        }
    }

    private fun getReponseWithImage(prompt: String, bitmap: Bitmap){
        viewModelScope.launch {
            val chat = ChatData.getResponseWithImage(prompt, bitmap)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat)
                    },
                )
            }
        }
    }
}