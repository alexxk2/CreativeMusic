package com.example.layoutmake.data.externals.settings.impl

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.layoutmake.R
import com.example.layoutmake.data.externals.settings.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {

    override fun createBrowserIntent() {
        val urlAgreement = context.getString(R.string.url_agreement)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlAgreement)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(context,intent, null)
    }

    override fun createMessageIntent() {
        val messageTitle = context.getString(R.string.message_title)
        val messageBody = context.getString(R.string.message_body)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_TEXT, messageBody)
        intent.putExtra(Intent.EXTRA_SUBJECT, messageTitle)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.dev_email)))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(context,intent,null)
    }

    override fun createShareIntent() {
        val url = context.getString(R.string.url_android_practicum)
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, url)
        intent.type = "text/plain"
        val chooser = Intent.createChooser(intent, context.getString(R.string.text_for_chooser))
        chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(context,chooser,null)
    }
}