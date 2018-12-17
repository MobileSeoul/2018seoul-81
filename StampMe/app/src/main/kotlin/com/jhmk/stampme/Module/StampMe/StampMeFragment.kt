package com.jhmk.stampme.Module.StampMe

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jhmk.stampme.Animation.SimpleViewFadeAnimation
import com.jhmk.stampme.Model.ConstVariables
import com.jhmk.stampme.Model.MyStamps
import com.jhmk.stampme.Model.User
import com.jhmk.stampme.Module.Adapter.StampsRecyclerviewAdapter
import com.jhmk.stampme.R
import kotlinx.android.synthetic.main.layout_fragment_stampme.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import srjhlab.com.myownbarcode.Dialog.BarcodeDialog
import srjhlab.com.myownbarcode.Dialog.StampInfoDialog

class StampMeFragment : Fragment(), StampMe.view, StampsRecyclerviewAdapter.IClickListener, View.OnClickListener {
    private val TAG = this.javaClass.simpleName
    private lateinit var mCurrentUser: User
    private lateinit var mPresenter: StampMe.presenter
    private lateinit var mAdapter: StampsRecyclerviewAdapter

    /*
    * 개선해보자..
    * */
    private var mCafeList: MutableList<MyStamps?> = ArrayList()
    private var mRestrauntList: MutableList<MyStamps?> = ArrayList()
    private var mStoreList: MutableList<MyStamps?> = ArrayList()
    private var mMartList: MutableList<MyStamps?> = ArrayList()
    private var mPublicList: MutableList<MyStamps?> = ArrayList()
    private var mEtcList: MutableList<MyStamps?> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "##### onCreateView #####")
        mCurrentUser = arguments?.getSerializable("UserItem") as User
        mPresenter = StampMePresenter(this)
        return inflater.inflate(R.layout.layout_fragment_stampme, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onActivityCreated #####")
        super.onActivityCreated(savedInstanceState)
        initializeUi()
    }

    fun initializeUi() {
        Log.d(TAG, "##### initializeUi ##### userId : ${mCurrentUser.userId}")
        txt_title_toolbar.text = resources.getString(R.string.string_title_my_stamps)
        txt_logout.setOnClickListener(this)
        img_info_barcode.setOnClickListener(this)
        img_back_toolbar.visibility = View.INVISIBLE
        img_edit_toolbar.visibility = View.GONE
        img_edit_toolbar.setOnClickListener(this)
        img_stamp_cafe_stampme.setOnClickListener(this)
        img_stamp_restaurant_stampme.setOnClickListener(this)
        img_stamp_store_stampme.setOnClickListener(this)
        img_stamp_mart_stampme.setOnClickListener(this)
        img_stamp_public_stampme.setOnClickListener(this)
        img_stamp_etc_stampme.setOnClickListener(this)
        layout_detail_category_back.setOnClickListener(this)
        mPresenter.requestMakeBarcode(mCurrentUser)
        mPresenter.requestGetMyStamp(mCurrentUser)
    }

    override fun onClick(v: View) {
        Log.d(TAG, "##### onItemClick #####")
        when (v.id) {
            img_edit_toolbar.id -> mPresenter.requestSettings(mCurrentUser)
            img_info_barcode.id -> {
                BarcodeDialog()
                        .setUser(mCurrentUser)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            layout_detail_category_back.id -> SimpleViewFadeAnimation().startAnimation(layout_detail_category_back, layout_detail_category_front)
            img_stamp_cafe_stampme.id
                , img_stamp_restaurant_stampme.id
                , img_stamp_store_stampme.id
                , img_stamp_mart_stampme.id
                , img_stamp_public_stampme.id
                , img_stamp_etc_stampme.id -> setDetailCategory(v.id)
            txt_logout.id -> {
                activity!!.alert(resources.getString(R.string.alert_logout_title)) {
                    yesButton { mPresenter.requestLogout(mCurrentUser) }
                    noButton { }
                }.show()
            }
        }
    }

    private fun setDetailCategory(id: Int) {
        Log.d(TAG, "##### setDetailCategory #####")
        var list: MutableList<MyStamps?> = ArrayList()
        when (id) {
            img_stamp_cafe_stampme.id -> {
                txt_category_name.text = ConstVariables.SHOP_TYPE_CAFE
                list = mCafeList
            }
            img_stamp_restaurant_stampme.id -> {
                txt_category_name.text = ConstVariables.SHOP_TYPE_RESTRAUNT
                list = mRestrauntList
            }
            img_stamp_store_stampme.id -> {
                txt_category_name.text = ConstVariables.SHOP_TYPE_STORE
                list = mStoreList
            }
            img_stamp_mart_stampme.id -> {
                txt_category_name.text = ConstVariables.SHOP_TYPE_MART
                list = mMartList
            }
            img_stamp_public_stampme.id -> {
                txt_category_name.text = ConstVariables.SHOP_TYPE_PUBLIC
                list = mPublicList
            }
            img_stamp_etc_stampme.id -> {
                txt_category_name.text = ConstVariables.SHOP_TYPE_ETC
                list = mEtcList
            }

        }
        if (list.size < 1) {
            txt_category_load_fail.visibility = View.VISIBLE
        } else {
            txt_category_load_fail.visibility = View.GONE
        }
        list_category_detail.adapter = StampsRecyclerviewAdapter(activity as Context, list, null)
        list_category_detail.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        list_category_detail.setHasFixedSize(true)
        SimpleViewFadeAnimation().startAnimation(layout_detail_category_front, layout_detail_category_back)
    }

    override fun onItemClick(stamp: MyStamps?) {
        Log.d(TAG, "##### onItemClick #####")
        if (stamp == null) {
            return
        }
        StampInfoDialog()
                .setItem(stamp)
                .show(fragmentManager, this.javaClass.simpleName)
    }

    override fun onResultMakeBarcode(user: User, bitmap: Bitmap?) {
        Log.d(TAG, "##### onResultMakeBarcode ##### user.id : ${user.userId}")
        if (bitmap != null) {
            img_info_barcode.setImageBitmap(bitmap)
        }
        txt_info_name.setText(user.userName)
    }

    override fun onResultGetMyStamp(resultList: MutableList<MyStamps?>?) {
        Log.d(TAG, "##### onResultGetMyStamp #####")
        if (resultList != null) {
            resultList.reverse()
            mPresenter.requestSeperateMyStamp(resultList)
            Log.d(TAG, "##### onResultGetMyStamp list size : ${resultList.size}#####")
            if (activity != null) {
                mAdapter = StampsRecyclerviewAdapter(activity as Context, resultList, this)
            }
            list_latest_stamps.adapter = mAdapter
            list_latest_stamps.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            list_latest_stamps.setHasFixedSize(true)
        }
    }

    override fun onResultSeperateMyStamp(cafeList: MutableList<MyStamps?>, restrauntList: MutableList<MyStamps?>, storeList: MutableList<MyStamps?>, martList: MutableList<MyStamps?>, publicList: MutableList<MyStamps?>, etcList: MutableList<MyStamps?>) {
        Log.d(TAG, "##### onResultSeperateMyStamp ##### \ncafeList ${cafeList.size} \nrestrauntList ${restrauntList.size} \nstoreList ${storeList.size} \nmartList ${martList.size} \npublicList ${publicList.size} \netcList ${etcList.size}")
        mCafeList = cafeList
        mRestrauntList = restrauntList
        mStoreList = storeList
        mMartList = martList
        mPublicList = publicList
        mEtcList = etcList

        if (cafeList.size in 1..2) {
            img_stamp_cafe_stampme.setBackgroundResource(R.drawable.ic_stamp_2)
        } else if (cafeList.size >= 3) {
            img_stamp_cafe_stampme.setBackgroundResource(R.drawable.ic_stamp_3)
        }

        if (restrauntList.size in 1..2) {
            img_stamp_restaurant_stampme.setBackgroundResource(R.drawable.ic_stamp_2)
        } else if (restrauntList.size >= 3) {
            img_stamp_restaurant_stampme.setBackgroundResource(R.drawable.ic_stamp_3)
        }

        if (storeList.size in 1..2) {
            img_stamp_store_stampme.setBackgroundResource(R.drawable.ic_stamp_2)
        } else if (storeList.size >= 3) {
            img_stamp_store_stampme.setBackgroundResource(R.drawable.ic_stamp_3)
        }

        if (martList.size in 1..2) {
            img_stamp_mart_stampme.setBackgroundResource(R.drawable.ic_stamp_2)
        } else if (martList.size >= 3) {
            img_stamp_mart_stampme.setBackgroundResource(R.drawable.ic_stamp_3)
        }

        if (publicList.size in 1..2) {
            img_stamp_public_stampme.setBackgroundResource(R.drawable.ic_stamp_2)
        } else if (publicList.size >= 3) {
            img_stamp_public_stampme.setBackgroundResource(R.drawable.ic_stamp_3)
        }

        if (etcList.size in 1..2) {
            img_stamp_etc_stampme.setBackgroundResource(R.drawable.ic_stamp_2)
        } else if (etcList.size >= 3) {
            img_stamp_etc_stampme.setBackgroundResource(R.drawable.ic_stamp_3)
        }
    }
}