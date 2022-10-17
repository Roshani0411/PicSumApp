package com.techflitter.myapplicationgit.model

class PicSumModel : ArrayList<PicSumModelItem>()

data class PicSumModelItem(
    val author: String,
    val download_url: String,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)