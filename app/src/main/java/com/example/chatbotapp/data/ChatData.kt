package com.example.chatbotapp.data

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.ResponseStoppedException
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChatData {

    val api_key = System.getenv("API_KEY")

    suspend fun  getResponse(prompt: String):Chat{
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro", // just texts
            apiKey = api_key
        )
        try {
            val response = withContext(Dispatchers.IO){
                generativeModel.generateContent(prompt)
            }

            return Chat(
                prompt = response.text ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }catch (e: ResponseStoppedException){
            return Chat(
                prompt = e.message ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }
    }

    // in case we a have a bitmap (image)
    suspend fun  getResponse(prompt: String, bitmap: Bitmap):Chat{
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision", // can take images(bitmap)
            apiKey = api_key
        )
        try {

            val inputContent = content {
                image(bitmap)
                text(prompt)
//                image(bitmap)   ^^ we can send multiple images ^^
//                image(bitmap)
//                image(bitmap)
            }

            val response = withContext(Dispatchers.IO){
                generativeModel.generateContent(inputContent)
            }

            return Chat(
                prompt = response.text ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }catch (e: ResponseStoppedException){
            return Chat(
                prompt = e.message ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }
    }
}