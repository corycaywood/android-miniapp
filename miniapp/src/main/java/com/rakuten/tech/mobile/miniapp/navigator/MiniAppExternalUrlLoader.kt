package com.rakuten.tech.mobile.miniapp.navigator

import android.app.Activity
import android.content.Intent

class MiniAppExternalUrlLoader(
    miniAppId: String,
    private val activity: Activity
) {
    private val miniAppDomain = "mscheme.${miniAppId}"
    private val customScheme = "$miniAppDomain://"
    private val customDomain = "https://$miniAppDomain/"
    private val miniAppUrlSchemes = arrayListOf(customScheme, customDomain)

    fun shouldOverrideUrlLoading(url: String): Boolean {
        if (isMiniAppScheme(url)) {
            finishCallBack.invoke(url)
            return true;
        }

        return false
    }

    private fun isMiniAppScheme(url: String): Boolean {
        miniAppUrlSchemes.forEach { scheme ->
            if (url.startsWith(scheme))
                return true
        }
        return false
    }

    val finishCallBack: (url: String) -> Unit = {
        val returnIntent = Intent().apply { putExtra(returnUrlTag, it) }
        activity.setResult(Activity.RESULT_OK, returnIntent)
        activity.finish()
    }

    companion object {
        const val returnUrlTag = "return_url_tag"
    }
}
