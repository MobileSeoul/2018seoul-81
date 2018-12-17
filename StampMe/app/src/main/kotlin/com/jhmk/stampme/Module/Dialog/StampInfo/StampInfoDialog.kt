package srjhlab.com.myownbarcode.Dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jhmk.stampme.Model.MyStamps
import com.jhmk.stampme.Model.User
import com.jhmk.stampme.Module.Dialog.Barcode
import com.jhmk.stampme.Module.GlideApp
import com.jhmk.stampme.R
import kotlinx.android.synthetic.main.layiout_stampinfo_dialog.*
import kotlinx.android.synthetic.main.layout_barcode_dialog.*

class StampInfoDialog : android.support.v4.app.DialogFragment(), Barcode.view {
    private val TAG = this.javaClass.simpleName
    private lateinit var mItem: MyStamps
    private lateinit var mGlideOption: RequestOptions


    @SuppressLint("CheckResult")
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "##### onCreateView #####")
        dialog.window.attributes.windowAnimations = R.style.SelectDialogAnimation
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        mGlideOption = RequestOptions()
        mGlideOption.transforms(CenterCrop(), RoundedCorners(16))
        dialog.setCanceledOnTouchOutside(true)
        return inflater.inflate(R.layout.layiout_stampinfo_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onActivityCreated #####")
        super.onActivityCreated(savedInstanceState)
        initializeUi()
    }

    fun setItem(item: MyStamps): StampInfoDialog {
        Log.d(TAG, "##### setItem #####")
        this.mItem = item
        return this
    }

    private fun initializeUi() {
        Log.d(TAG, "##### initializeUi #####")
        GlideApp
                .with(activity as Context)
                .load(mItem.stampSourceImageUrl)
                .apply(mGlideOption)
                .into(img_store_storeinfo)
        txt_name_storeinfo.text = mItem.stampsSource
        txt_distance_storeinfo.text = mItem.stampSourceType
        txt_address_storeinfo.text = mItem.stampAddress
        txt_body_guideline_storeinfo.text = mItem.stampReason
    }

    override fun onResultMakeBarcode(user: User, bitmap: Bitmap?) {
        Log.d(TAG, "##### onResultMakeBarcode #####")
        img_barcode.setImageBitmap(bitmap)
    }
}