package com.jhmk.stampme.Module.NearbyShops

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jhmk.stampme.Model.Shops
import com.jhmk.stampme.Model.User
import com.jhmk.stampme.Module.Adapter.ShopsRecyclerviewAdapter
import com.jhmk.stampme.R
import kotlinx.android.synthetic.main.layout_fragment_nearby_shops.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import srjhlab.com.myownbarcode.Dialog.BarcodeDialog

class NearbyShopsFragment : Fragment(), NearbyShops.view, ShopsRecyclerviewAdapter.IClickListener {
    private val TAG = this.javaClass.simpleName
    private lateinit var mPresenter: NearbyShopsPresenter
    private lateinit var mCurrentUser: User
    private lateinit var mAdapter: ShopsRecyclerviewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "##### onCreateVIew #####")
        mPresenter = NearbyShopsPresenter(this)
        mCurrentUser = arguments?.getSerializable("UserItem") as User
        return inflater.inflate(R.layout.layout_fragment_nearby_shops, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onActivityCreated #####")
        super.onActivityCreated(savedInstanceState)
        initializeUi()
    }

    fun initializeUi() {
        Log.d(TAG, "##### initializeUi #####")
        txt_title_toolbar.text = resources.getString(R.string.string_title_nearby_shops)
        img_back_toolbar.visibility = View.INVISIBLE
        mPresenter.requestAroundShopList(activity as Context)
    }


    override fun onItemClick(pos: Int) {
        Log.d(TAG, "##### onItemClick #####")
        mAdapter.refreshItem(pos)
    }

    override fun onGetStampClick(id: Int) {
        Log.d(TAG, "onGetStampClick")
        BarcodeDialog()
                .setUser(mCurrentUser)
                .show(fragmentManager, this.javaClass.simpleName)
    }

    override fun onResultAroundShopList(list: MutableList<Shops?>) {
        Log.d(TAG, "##### onResultAroundShopList #####")
        mAdapter = ShopsRecyclerviewAdapter(activity as Context, list, this)
        list_nearby_shops.adapter = mAdapter
        list_nearby_shops.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        list_nearby_shops.setHasFixedSize(true)
        for ((index, value) in list.withIndex()) {
            Log.d(TAG, "targetBehavior : ${value?.shopTargetBehavior} + targetImageUrl : ${value?.shopImageUrl}")
        }
    }
}
