package com.jhmk.stampme.Module.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.jhmk.stampme.Model.MyStamps
import com.jhmk.stampme.Module.GlideApp
import com.jhmk.stampme.R
import kotlinx.android.synthetic.main.layout_item_stamps.view.*

class StampsRecyclerviewAdapter(context: Context, items: MutableList<MyStamps?>, listener: StampsRecyclerviewAdapter.IClickListener?) : RecyclerView.Adapter<StampsRecyclerviewAdapter.ViewHolder>() {
    val TAG = this.javaClass.simpleName

    private var mItems: MutableList<MyStamps?> = items
    private var mListener: IClickListener? = listener
    private val mContext = context
    private lateinit var mGlideOption: RequestOptions

    interface IClickListener {
        fun onItemClick(stamp: MyStamps?)
    }

    @SuppressLint("CheckResult")
    override fun onCreateViewHolder(viewGropu: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGropu.context).inflate(R.layout.layout_item_stamps, viewGropu, false)
        mGlideOption = RequestOptions()
        mGlideOption.transforms(CenterCrop(), CircleCrop())
        return ViewHolder(v)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "##### onBindViewHolder position : $position")
        val item: MyStamps? = mItems.get(position)
        GlideApp
                .with(mContext)
                .load(item!!.stampSourceImageUrl)
                .apply(mGlideOption)
                .into(holder.itemView.img_my_stamps)
        holder.itemView.txt_my_stamps.text = item.stampsSource
        holder.itemView.txt_date_my_Stamps.text = item.stampDate

    }

    override fun getItemCount(): Int {
        return this.mItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            if (mListener != null) {
                itemView.layout_my_stamps.setOnClickListener { v -> mListener!!.onItemClick(mItems[adapterPosition]) }
            }
        }
    }
}