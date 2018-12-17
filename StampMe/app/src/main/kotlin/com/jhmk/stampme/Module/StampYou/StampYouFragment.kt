package com.jhmk.stampme.Module.StampYou

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jhmk.stampme.Model.ConstVariables
import com.jhmk.stampme.Model.EventBusObject
import com.jhmk.stampme.Model.Shops
import com.jhmk.stampme.Model.User
import com.jhmk.stampme.Module.GlideApp
import com.jhmk.stampme.R
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_fragment_my_store.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import srjhlab.com.myownbarcode.Dialog.BarcodeDialog

class StampYouFragment : Fragment(), StampYou.view, View.OnClickListener {
    private val TAG = this.javaClass.simpleName
    private lateinit var mPresenter: StampYouPresenter
    private lateinit var mCurrentUser: User
    private lateinit var mCurrentStore: Shops

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "##### onCreateVIew #####")
        mCurrentUser = arguments?.getSerializable("UserItem") as User
        return inflater.inflate(R.layout.layout_fragment_my_store, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onActivityCreated #####")
        super.onActivityCreated(savedInstanceState)
        mPresenter = StampYouPresenter(this)
        EventBus.getDefault().register(this)
        initializeUi()
    }

    override fun onDestroyView() {
        Log.d(TAG, "##### onDestroyView #####")
        super.onDestroyView()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    fun initializeUi() {
        Log.d(TAG, "##### initializeUi #####")
        mPresenter.requestGetMyStore(mCurrentUser)
        txt_title_toolbar.text = resources.getString(R.string.string_title_my_store)
        img_back_toolbar.visibility = View.INVISIBLE
        img_give_stamp_storeinfo.setOnClickListener(this)
        txt_logout_storeinfo.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        Log.d(TAG, "##### onClick #####")
        when (v.id) {
            img_give_stamp_storeinfo.id -> mPresenter.requestSetBarcodeScan(mCurrentStore, activity as Activity)
            txt_logout_storeinfo.id -> {
                activity!!.alert(resources.getString(R.string.alert_logout_title)) {
                    yesButton { mPresenter.requestLogout(mCurrentUser) }
                    noButton { }
                }.show()
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun onResultMyStore(result: Boolean, msg: Int, store: Shops?) {
        Log.d(TAG, "##### onResultMyStore ##### storename : ${store!!.shopName}")
        mCurrentStore = store
        val glideOption = RequestOptions()
        glideOption.transforms(CenterCrop(), RoundedCorners(16))

        GlideApp
                .with(activity as Context)
                .load(store.shopImageUrl)
                .apply(glideOption)
                .into(img_store_storeinfo)
        txt_name_storeinfo.text = store!!.shopName
        txt_distance_storeinfo.text = store!!.shopDistance
        txt_address_storeinfo.text = store!!.shopAddress
        txt_phonenumber_storeinfo.text = store!!.shopPhoneNumber
        txt_body_guideline_storeinfo.text = store!!.shopTargetBehavior
    }

    @Subscribe
    fun onEvent(obj: EventBusObject) {
        Log.d(TAG, "##### onEvent #####")
        when (obj.key) {
            ConstVariables.EVENTBUS_SUCCESS_SUBMIT -> {
                BarcodeDialog()
                        .setType(ConstVariables.DIALOG_TYPE_SUBMIT_SUCCESS)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
            ConstVariables.EVENTBUS_FAILED_SUBMIT -> {
                BarcodeDialog()
                        .setType(ConstVariables.DIALOG_TYPE_SUBMIT_FAILED)
                        .show(fragmentManager, this.javaClass.simpleName)
            }
        }
    }
}
