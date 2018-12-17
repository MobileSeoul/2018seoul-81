package srjhlab.com.myownbarcode.Dialog

import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.jhmk.stampme.Model.ConstVariables
import com.jhmk.stampme.Model.User
import com.jhmk.stampme.Module.Dialog.Barcode
import com.jhmk.stampme.Module.Dialog.BarcodeDialogPresenter
import com.jhmk.stampme.R
import kotlinx.android.synthetic.main.layout_barcode_dialog.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BarcodeDialog : android.support.v4.app.DialogFragment(), Barcode.view {
    private val TAG = this.javaClass.simpleName
    private lateinit var mPresenter : BarcodeDialogPresenter
    private lateinit var mUser: User
    private var mDialogType = ConstVariables.DIALOG_TYPE_BARCODE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "##### onCreateView #####")
        dialog.window.attributes.windowAnimations = R.style.SelectDialogAnimation
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.setCanceledOnTouchOutside(true)
        return inflater.inflate(R.layout.layout_barcode_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onActivityCreated #####")
        super.onActivityCreated(savedInstanceState)
        mPresenter = BarcodeDialogPresenter(this)
        initializeUi()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        Log.d(TAG, "##### onDismiss #####")
        super.onDismiss(dialog)
    }

    fun setUser(user: User): BarcodeDialog {
        Log.d(TAG, "##### setUser #####")
        this.mUser = user
        return this
    }

    fun setType(type : Int) : BarcodeDialog{
        Log.d(TAG, "##### setType #####")
        mDialogType = type

        return this
    }

    private fun initializeUi() {
        Log.d(TAG, "##### initializeUi #####")
        if(mDialogType == ConstVariables.DIALOG_TYPE_BARCODE) {
            mPresenter.requestMakeBarcode(mUser)
        }else if(mDialogType == ConstVariables.DIALOG_TYPE_SUBMIT_SUCCESS){
            img_barcode.background = activity!!.resources.getDrawable(R.drawable.img_stamp)
        }else if(mDialogType == ConstVariables.DIALOG_TYPE_SUBMIT_FAILED){
            img_barcode.background = activity!!.resources.getDrawable(R.drawable.img_nostamp)
        }
    }

    override fun onResultMakeBarcode(user: User, bitmap: Bitmap?) {
        Log.d(TAG, "##### onResultMakeBarcode #####")
        img_barcode.setImageBitmap(bitmap)
    }
}