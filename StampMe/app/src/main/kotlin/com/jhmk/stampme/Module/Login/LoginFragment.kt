package com.jhmk.stampme.Module.Login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jhmk.stampme.Model.User
import com.jhmk.stampme.R
import kotlinx.android.synthetic.main.layout_fragment_login.*

class LoginFragment : Fragment(), View.OnClickListener {
    private val TAG = this.javaClass.simpleName
    private lateinit var mPresenter: Login.presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "##### onCreateView #####")
        mPresenter = LoginPresenter()
        return inflater.inflate(R.layout.layout_fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "##### onActivityCreated #####")
        initializeUI()
    }

    fun initializeUI() {
        Log.d(TAG, "##### initializeUI #####")
        btn_login.setOnClickListener(this)
        btn_register.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        Log.d(TAG, "##### onItemClick #####")
        when (v.id) {
            btn_login.id -> mPresenter.requestLogin(User(edtxt_login_id.text.toString(), edtxt_login_pw.text.toString()))
            btn_register.id -> mPresenter.requestRegister()
        }
    }
}