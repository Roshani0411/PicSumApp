package com.techflitter.myapplicationgit.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.techflitter.myapplicationgit.databinding.ItemDataBinding
import com.techflitter.myapplicationgit.model.PicSumModelItem
import com.techflitter.myapplicationgit.utils.Constants
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class PicSumDataAdapter(
    context1: Context,
    var followerList: MutableList<PicSumModelItem>,
) :
    ListAdapter<PicSumModelItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        /** Mandatory implementation inorder to use "ListAdapter" - new JetPack component" **/
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<PicSumModelItem>() {
                override fun areItemsTheSame(
                    oldItem: PicSumModelItem,
                    newItem: PicSumModelItem,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: PicSumModelItem,
                    newItem: PicSumModelItem,
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }

    var mContext: Context? = null


    init {
        mContext = context1
    }

    fun refreshList(list: ArrayList<PicSumModelItem>) {
        followerList.clear()
        notifyDataSetChanged()
        followerList.addAll(list)
        notifyItemRangeChanged(0, list.size)
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context

        val view = ItemDataBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    inner class ViewHolder(private val binding: ItemDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @OptIn(DelicateCoroutinesApi::class)
        @SuppressLint("SetTextI18n")
        fun bind(pos: Int, item: PicSumModelItem) {

            binding.txtName.text = "Name : ${item.author}"
            /**
             * comment loadMediaInGlide function to view downloaded image*/
            Constants.loadMediaInGlide(
                item.download_url,
                imageView = binding.image,
                isSetImage = true
            )
            binding.root.setOnClickListener {
                Constants.msgDialog(
                    mContext as AppCompatActivity,
                    item.author
                )
            }
            val urlImage = URL(item.download_url)
            /** async task to get / download bitmap from url
             * perform operation in io thread */
            val result: Deferred<Bitmap?> = GlobalScope.async {
                urlImage.toBitmap()
            }

            GlobalScope.launch(Dispatchers.IO) {
                // get the downloaded bitmap
                val bitmap: Bitmap? = result.await()

                // if downloaded then saved it to internal storage
                bitmap?.apply {

                        // get saved bitmap internal storage uri
                        val savedUri: Uri? = saveToInternalStorage(mContext!!)

                    /**  display saved bitmap to image view from internal storage
                     * uncomment below portion for see downloaded image in to list
                        */
                   /* GlobalScope.launch(Dispatchers.Main) {
                        Constants.loadMediaInGlide(
                            savedUri.toString(),
                            imageView = binding.image,
                            isSetImage = true
                        )
                    }*/
                    // show bitmap saved uri in text view
                }


            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = holder as ViewHolder
        item.bind(position, followerList[position])
    }


    override fun getItemCount(): Int {
        return followerList.size
    }

    // extension function to save an image to internal storage
    fun Bitmap.saveToInternalStorage(context: Context): Uri? {
        // get the context wrapper instance
        val wrapper = ContextWrapper(context)

        // initializing a new file
        // bellow line return a directory in internal storage
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)

        // create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        return try {
            // get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // compress bitmap
            compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // flush the stream
            stream.flush()

            // close stream
            stream.close()

            // return the saved image uri
            Uri.parse(file.absolutePath)
        } catch (e: IOException) { // catch the exception
            e.printStackTrace()
            null
        }


    }

    // extension function to get / download bitmap from url
    fun URL.toBitmap(): Bitmap? {
        return try {
            BitmapFactory.decodeStream(openStream())
        } catch (e: IOException) {
            null
        }
    }
}
