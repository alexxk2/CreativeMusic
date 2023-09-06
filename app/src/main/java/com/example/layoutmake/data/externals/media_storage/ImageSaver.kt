package com.example.layoutmake.data.externals.media_storage

import android.net.Uri

interface ImageSaver {

    suspend fun saveImageAndReturnPath(uri: Uri): Uri
}