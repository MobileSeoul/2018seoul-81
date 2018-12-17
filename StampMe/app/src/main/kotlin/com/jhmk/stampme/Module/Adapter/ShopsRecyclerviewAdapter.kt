package com.jhmk.stampme.Module.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jhmk.stampme.Model.Shops
import com.jhmk.stampme.Module.GlideApp
import com.jhmk.stampme.R
import kotlinx.android.synthetic.main.layout_item_shops.view.*

class ShopsRecyclerviewAdapter(context: Context, items: MutableList<Shops?>, listener: ShopsRecyclerviewAdapter.IClickListener) : RecyclerView.Adapter<ShopsRecyclerviewAdapter.ViewHolder>() {
    val TAG = this.javaClass.simpleName

    private var mItems: MutableList<Shops?> = items
    private var mListener: IClickListener = listener
    private val mContext = context
    private lateinit var mGlideOption : RequestOptions

    interface IClickListener {
        fun onItemClick(id: Int)
        fun onGetStampClick(id : Int)
    }

    @SuppressLint("CheckResult")
    override fun onCreateViewHolder(viewGropu: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGropu.context).inflate(R.layout.layout_item_shops, viewGropu, false)
        mGlideOption = RequestOptions()
        mGlideOption.transforms(CenterCrop(), RoundedCorners(16))
        return ViewHolder(v)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "##### onBindViewHolder position : $position")
        val item: Shops? = mItems.get(position)
        if(mItems.get(position)!!.isFront) {
            holder.itemView.layout_item_shops_front.visibility = View.VISIBLE
            holder.itemView.layout_item_shops_back.visibility = View.GONE
            GlideApp
                    .with(mContext)
                    .load(item?.shopImageUrl)
                    .apply(mGlideOption)
                    .into(holder.itemView.img_item_shops)
            holder.itemView.txt_name_item_shops.text = item?.shopName
            holder.itemView.txt_name_item_shops.isSelected = true
            holder.itemView.txt_subname_item_shops.text = item?.shopAddress
            holder.itemView.txt_subname_item_shops.isSelected = true
            holder.itemView.txt_distance_item_shops.text = item?.shopDistance
        }else{
            holder.itemView.layout_item_shops_front.visibility = View.GONE
            holder.itemView.layout_item_shops_back.visibility = View.VISIBLE
            holder.itemView.txt_subtitle_back_item_shops.text = item?.shopTargetBehavior
        }
    }

    override fun getItemCount(): Int {
        return this.mItems.size
    }

    fun refreshItem(pos : Int){
        Log.d(TAG, "##### refreshITem ##### pos : $pos")
        mItems.get(pos)!!.isFront = !mItems.get(pos)!!.isFront
        notifyItemChanged(pos)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.layout_item_shops_back.setOnClickListener { v -> mListener.onItemClick(adapterPosition)}
            itemView.layout_item_shops_front.setOnClickListener { v -> mListener.onItemClick(adapterPosition)}
            itemView.img_getstamp_back_item_shops.setOnClickListener{v -> mListener.onGetStampClick(adapterPosition)}
        }
    }
}