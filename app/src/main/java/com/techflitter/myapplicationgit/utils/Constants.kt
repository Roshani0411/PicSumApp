package com.techflitter.myapplicationgit.utils

import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.techflitter.myapplicationgit.R

object Constants {
    const val URL = "https://picsum.photos/"

    fun loadMediaInGlide(
        imageUrl: String?,
        placeHolder: Int = R.color.transparent,
        imageView: ImageView? = null,
        progressBar: ProgressBar? = null,
        isSetImage: Boolean = false,
        pos:Int = 0
    ) {

        Log.d(
            "loadImagesinGlide",
            "loadMediainGlide 00000000000000000000 isSetImage=$isSetImage - pos=$pos"
        )
        if (imageView == null) {
            return
        }
        val mContext = imageView.context

        if (progressBar != null) {
            progressBar.visibility = View.VISIBLE
        }

        val counts = 0

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(placeHolder)
            .error(placeHolder)

        Glide.with(mContext.applicationContext)
            .setDefaultRequestOptions(requestOptions)
            .load(imageUrl)
            .dontTransform()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean,
                ): Boolean {
                    Log.d(
                        "loadImagesinGlide",
                        "0 loadMediainGlide onLoadFailed GlideException = ${e!!.localizedMessage}"
                    )
                    try {
                        if (progressBar != null) {
                            Log.d(
                                "loadImagesinGlide",
                                "loadMediainGlide 3 onLoadFailed - progressBar.visibility = View.GONE"
                            )
                            progressBar.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        Log.d(
                            "loadImagesinGlide",
                            " loadMediainGlide 1 Exception = ${e.localizedMessage}"
                        )
                    }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean,
                ): Boolean {
                    imageView.clearAnimation()
                    Log.d(
                        "loadImagesinGlide",
                        "2 loadMediainGlide onResourceReady isFirstResource = $isFirstResource - counts = $counts"
                    )
                    try {
                        if (progressBar != null) {
                            Log.d(
                                "loadImagesinGlide",
                                "loadMediainGlide 3 onResourceReady - progressBar.visibility = View.GONE"
                            )
                            progressBar.visibility = View.GONE
                        }
                    } catch (e: Exception) {
                        Log.d(
                            "loadImagesinGlide",
                            "loadMediainGlide 3 Exception = ${e.localizedMessage}"
                        )
                    }

                    if(isSetImage){
                        // MANAGE FIX HEIGHT FOR MIN HEIGHT REQUIREMENT
                        if (resource?.intrinsicHeight!! >= resource.intrinsicWidth) {
                            Log.d(
                                "loadImagesinGlide",
                                "loadMediainGlide 00000000000000000000113 onResourceReady: else if"
                            )
//                            imageView.minimumHeight = context.resources?.getDimension(R.dimen._280sdp)!!.toInt()
                            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                            val viewWidth: Float = imageView.width.toFloat()
                            val viewHeight: Float = imageView.height.toFloat()
                            val drawableWidth = resource.intrinsicWidth
                            val drawableHeight = resource.intrinsicHeight
                            val widthScale = viewWidth / drawableWidth
                            val heightScale = viewHeight / drawableHeight
                            val scale = widthScale.coerceAtLeast(heightScale)
                            val baseMatrix = Matrix()
                            baseMatrix.reset()
                            baseMatrix.postScale(scale, scale)
                            imageView.imageMatrix = baseMatrix
                            imageView.scaleType = ImageView.ScaleType.MATRIX
                            imageView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                            imageView.requestLayout()

                        }
                        else if(resource.intrinsicHeight <= mContext.resources?.getDimension(com.intuit.sdp.R.dimen._180sdp)!!){
                            Log.d(
                                "loadImagesinGlide",
                                "loadMediainGlide 00000000000000000000114 onResourceReady: else if resources?.getDimension=${mContext.resources?.getDimension(
                                    com.intuit.sdp.R.dimen._180sdp)!!}"
                            )

                            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                            imageView.layoutParams.height = mContext!!.resources.getDimensionPixelOffset(
                                com.intuit.sdp.R.dimen._180sdp)
                        }
                        Log.d(
                            "loadImagesinGlide",
                            "loadMediainGlide 0000000000000000000011111111111 - pos=$pos ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
                        )
                    }
                    return false
                }
            })
            .into(imageView)

    }
    public fun isOnline(context: Context?): Boolean {

        var isOnline = false
        try {
            if (context != null) {
                val mgr =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (mgr != null) {
                    val mobileInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    val wifiInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    if (wifiInfo != null && wifiInfo.isAvailable && wifiInfo.isAvailable && wifiInfo.isConnected) {
                        val wifiManager =
                            context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                        val wifiInfoStatus = wifiManager.connectionInfo
                        val supState = wifiInfoStatus.supplicantState
                        if (supState.toString()
                                .equals("COMPLETED", ignoreCase = true) || supState.toString()
                                .equals("ASSOCIATED", ignoreCase = true)
                        ) {
                            // WiFi is connected
                            isOnline = true
                        }
                    }
                    if (mobileInfo != null && mobileInfo.isAvailable && mobileInfo.isConnected) {
                        // Mobile Network is connected
                        isOnline = true
                    }
                }
            }
        } catch (ex: java.lang.Exception) {

        }
        return isOnline
    }
    lateinit var timer: CountDownTimer
    fun msgDialog(
        context: AppCompatActivity,
        msg: String,
        msgTitle: String = "",
        dialogType: String? = "SUCCESS",
    ) {
        try {
            val dialogMsg = MessageDialog.getInstance()
            val bundle = Bundle()
            bundle.putString("okTxt", "OK")
            bundle.putString("tvMsgText", msg)
            bundle.putString("msgTitle", msgTitle)
            bundle.putString("dialogType", "" + dialogType)

            dialogMsg.arguments = bundle

            if (dialogMsg.isAdded) {
                return
            }

            dialogMsg.show(context.supportFragmentManager, "")
            timer = object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    if (timer != null) {
                        timer.cancel()
                    }
                    //setStatusBar(ContextCompat.getColor(baseContext!!, statusbarOriginalColor))
                    try {
                        if (dialogMsg != null) {
                            if (dialogMsg.isVisible) {
                                dialogMsg!!.dismiss()
                            }
                        }
                    } catch (e: java.lang.Exception) {

                    }
                }
            }.start()
            dialogMsg.setListener(object : MessageDialog.OnClick {
                override fun set(ok: Boolean) {
                    try {
                        if (timer != null) {
                            timer!!.cancel()
                        }
                        if (dialogMsg != null) {
                            if (dialogMsg.isVisible) {
                                dialogMsg.dismiss()
                            }
                        }
                    } catch (e: java.lang.Exception) {

                    }
                    //setStatusBar(ContextCompat.getColor(baseContext!!, statusbarOriginalColor))
                }
            })
        } catch (e: java.lang.Exception) {
            Log.d("msgDialog", "Exception = ${e.localizedMessage}")
        }

    }


}