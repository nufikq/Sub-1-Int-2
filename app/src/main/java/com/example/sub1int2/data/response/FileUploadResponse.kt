package com.example.sub1int2.data.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)