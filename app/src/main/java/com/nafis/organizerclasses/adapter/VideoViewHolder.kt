package com.nafis.organizerclasses.adapter

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nafis.organizerclasses.DIffUtilCallBack.VideoItemCallback
import com.nafis.organizerclasses.R
import com.nafis.organizerclasses.databinding.VideoItemBinding
import com.nafis.organizerclasses.model.VideoModel
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.IOException

class VideoViewHolder(val binding: VideoItemBinding, val callback: VideoItemCallback) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: VideoModel) {
        val videoId = extractYouTubeId(item.videoUrl ?: "")
        val apiKey = "AIzaSyC1jpxSu28Q4mTOJZ2SvjeGSF1JD42PU38"
        binding.apply {
            videoTitle.text = item.title
            videoDate.isSelected = true
            videoTime.text = item.date.toString()

            if (videoId != null) {
                // Load YouTube video thumbnail using Glide
                Glide.with(itemView.context)
                    .load("https://img.youtube.com/vi/$videoId/hqdefault.jpg")
                    .into(pdfIcon)

                // Fetch YouTube video duration
                getYouTubeVideoTime(videoId, apiKey) { duration ->
                    videoTime.text = duration
                }
            } else {
                // Fallback thumbnail
                pdfIcon.setImageResource(R.drawable.default_video_thumbnail)
                videoTime.text = "Unknown"
            }

            itemView.setOnClickListener {
                callback.onNoteClick(item, position = position)
            }
        }
    }

    // Extract YouTube video ID from the URL
    fun extractYouTubeId(url: String): String? {
        return when {
            "youtu.be/" in url -> url.substringAfter("youtu.be/").substringBefore("?")
            "v=" in url -> url.substringAfter("v=").substringBefore("&")
            "embed/" in url -> url.substringAfter("embed/").substringBefore("?")
            else -> null
        }
    }

    // Fetch the YouTube video duration
    fun getYouTubeVideoTime(videoId: String, apiKey: String, callback: (String) -> Unit) {
        val url = "https://www.googleapis.com/youtube/v3/videos?part=contentDetails&id=$videoId&key=$apiKey"
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder().url(url).build()

        // Perform the HTTP request
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                // Make sure UI updates are done on the main thread
                callback("Failed to fetch video duration")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { jsonResponse ->
                        val jsonObject = JSONObject(jsonResponse)
                        val items = jsonObject.getJSONArray("items")
                        if (items.length() > 0) {
                            val contentDetails = items.getJSONObject(0).getJSONObject("contentDetails")
                            val duration = contentDetails.getString("duration")
                            val formattedDuration = parseYouTubeDuration(duration)

                            // Update the UI on the main thread
                            Handler(Looper.getMainLooper()).post {
                                callback(formattedDuration)
                            }
                        } else {
                            // Update the UI on the main thread
                            Handler(Looper.getMainLooper()).post {
                                callback("No video found")
                            }
                        }
                    }
                } else {
                    // Update the UI on the main thread
                    Handler(Looper.getMainLooper()).post {
                        callback("Error fetching video details")
                    }
                }
            }
        })
    }


    // Parse YouTube video duration (e.g., PT1H23M45S -> 1:23:45)
    private fun parseYouTubeDuration(duration: String): String {
        // Updated regex to handle missing parts (hours, minutes, or seconds)
        val regex = Regex("PT(\\d+H)?(\\d+M)?(\\d+S)?")
        val matchResult = regex.find(duration)

        return if (matchResult != null) {
            // Extract the hours, minutes, and seconds if available
            val hours = matchResult.groups[1]?.value?.replace("H", "")?.toIntOrNull() ?: 0
            val minutes = matchResult.groups[2]?.value?.replace("M", "")?.toIntOrNull() ?: 0
            val seconds = matchResult.groups[3]?.value?.replace("S", "")?.toIntOrNull() ?: 0

            // Format the time, showing only the necessary parts (e.g., minutes and seconds if hours are 0)
            val formattedTime = when {
                hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, seconds)
                minutes > 0 -> String.format("%d:%02d", minutes, seconds)
                else -> String.format("%02d", seconds)
            }

            formattedTime
        } else {
            "Unknown Duration"
        }
    }

}

