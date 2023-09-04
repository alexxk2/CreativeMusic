package com.example.layoutmake.data.externals.media_storage.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.layoutmake.app.SHARED_PREFS
import com.example.layoutmake.data.externals.media_storage.ImageSaver
import java.io.File
import java.io.FileOutputStream

class ImageSaverImpl(private val context: Context) : ImageSaver {

    private var coverCount = 0
    private var coverSrc = "-1"
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS, 0)


    override suspend fun saveImageAndReturnPath(uri: Uri): String {

        coverCount = sharedPrefs.getInt(COVERS_COUNT, 0)

        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), COVERS)
        //create catalog if not exist
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        coverSrc = "cover_${coverCount}.jpg"
        val file = File(filePath, coverSrc)
        coverCount++
        putCountInSharedPrefs(coverCount)

        val inputStream = context.contentResolver.openInputStream(uri)
        val outPutStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outPutStream)

        return coverSrc
    }


    private fun putCountInSharedPrefs(count: Int) {
        sharedPrefs.edit()
            .putInt(COVERS_COUNT, count)
            .apply()
    }

    companion object {
        private const val COVERS = "covers"
        private const val COVERS_COUNT = "covers_count"

    }
}