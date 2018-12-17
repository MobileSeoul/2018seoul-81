package com.jhmk.stampme.Main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import com.jhmk.stampme.Model.ConstVariables
import com.jhmk.stampme.Model.EventBusObject
import com.jhmk.stampme.Model.Shops
import com.jhmk.stampme.Model.User
import com.jhmk.stampme.Module.DataBase.PreferencesManager
import com.jhmk.stampme.Module.Login.LoginFragment
import com.jhmk.stampme.Module.StampYou.StampYouFragment
import com.jhmk.stampme.Module.NearbyShops.NearbyShopsFragment
import com.jhmk.stampme.Module.Register.RegisterFragment
import com.jhmk.stampme.Module.Settings.SettingsFragment
import com.jhmk.stampme.Module.StampMe.StampMeFragment
import com.jhmk.stampme.R
import org.greenrobot.eventbus.EventBus
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity(), View.OnClickListener, Main.view {
    private val TAG = this.javaClass.simpleName
    private lateinit var mPresenter: MainPresenter
    private lateinit var mContentView: View
    private lateinit var mCurrentUser: User
    private lateinit var mCurrentStore : Shops

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onCreate #####")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        mPresenter = MainPresenter(this)
        initializeUI()
        loadLoginState()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "##### onDestroy #####")
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    fun initializeUI() {
        Log.d(TAG, "##### initializeUI #####")
        img_stampyou_main_tab.setOnClickListener(this)
        img_home_main_tab.setOnClickListener(this)
        img_stampme_main_tab.setOnClickListener(this)
        mContentView = (layout_main_content as View)
    }

    override fun onClick(v: View) {
        Log.d(TAG, "##### onItemClick #####")
        when (v.id) {
            img_stampyou_main_tab.id -> mPresenter.requestSelectTab(mCurrentUser, ConstVariables.TAB_STAMPYOU)
            img_home_main_tab.id -> mPresenter.requestSelectTab(mCurrentUser, ConstVariables.TAB_HOME)
            img_stampme_main_tab.id -> mPresenter.requestSelectTab(mCurrentUser, ConstVariables.TAB_STAMPME)
        }
    }

    override fun onResultLogin(result: Boolean, msg: Int, user: User) {
        Log.d(TAG, "##### onResultLogin ##### isSuccess : $result")
        toast(resources.getString(msg))
        if (result) {
            mCurrentUser = user
            mPresenter.requestSaveUserInfo(this, mCurrentUser)
            if (mCurrentUser.userType == ConstVariables.USER_TYPE_BUYER) {
                handleFragment(img_home_main_tab.id, mCurrentUser)
            } else if (mCurrentUser.userType == ConstVariables.USER_TYPE_SELLER) {
                handleFragment(img_stampyou_main_tab.id, mCurrentUser)
            }
        } else {
            handleFragment(ConstVariables.SHOW_FRAGMENT_LOGIN, mCurrentUser)
        }
    }


    override fun onResultRegister(result: Boolean, msg: Int, user: User) {
        Log.d(TAG, "##### onResultRegister #####")
        toast(resources.getString(msg))
        if (result) {
            mCurrentUser = user
            mPresenter.requestSaveUserInfo(this, mCurrentUser)
            if (mCurrentUser.userType == ConstVariables.USER_TYPE_BUYER) {
                handleFragment(img_home_main_tab.id, mCurrentUser)
            } else if (mCurrentUser.userType == ConstVariables.USER_TYPE_SELLER) {
                handleFragment(img_stampyou_main_tab.id, mCurrentUser)
            }
        } else {
            handleFragment(ConstVariables.SHOW_FRAGMENT_LOGIN, mCurrentUser)
        }
    }

    override fun onResultLogout(result: Boolean, msg: Int) {
        Log.d(TAG, "##### onResultLogout #####")
        toast(resources.getString(msg))
        if (result) {
            handleFragment(ConstVariables.SHOW_FRAGMENT_LOGIN, null)
        } else {

        }
    }

    override fun onResultSelectTab(result: Boolean, msg: Int, tabId: Int) {
        Log.d(TAG, "##### onResultSelectTab #####")
        var id = -1
        when (tabId) {
            ConstVariables.TAB_STAMPYOU -> {
                if (result) {
                    id = img_stampyou_main_tab.id
                } else {
                    toast(resources.getString(msg))
                    return
                }
            }
            ConstVariables.TAB_HOME -> {
                if (result) {
                    id = img_home_main_tab.id
                } else {
                    toast(resources.getString(msg))
                    return
                }
            }
            ConstVariables.TAB_STAMPME -> {
                if (result) {
                    id = img_stampme_main_tab.id
                } else {
                    toast(resources.getString(msg))
                    return
                }
            }
        }

        try {
            handleFragment(id, mCurrentUser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResultBackPressed(result: Boolean, msg: Int) {
        Log.d(TAG, "##### onResultBackPressed #####")
        if (result) {
            this.finish()
        } else {
            toast(resources.getString(msg))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "##### onActivityResult #####")
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        when (result.formatName) {
            null -> return
            "QR_CODE" -> mPresenter.requestProcessingScanResult(mCurrentStore, result.contents)
            else -> return
        }
    }

    fun loadLoginState() {
        Log.d(TAG, "##### loadLoginState #####")
        val loadedUser = PreferencesManager.loadUserInfo(this)
        if (loadedUser.userId == "") {
            handleFragment(ConstVariables.SHOW_FRAGMENT_LOGIN, null)
        } else {
            mPresenter.requestLogin(User(loadedUser.userId, loadedUser.userPw))
        }
    }

    fun handleFragment(state: Int, user: User?) {
        Log.d(TAG, "##### handleFragment #####")
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        var fr = Fragment()
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

        when (state) {
            ConstVariables.FRAGMENT_BACKSTACK -> {
                fm.popBackStack()
                return
            }

            ConstVariables.SHOW_FRAGMENT_LOGIN -> {
                layout_main_tab.visibility = View.GONE
                fr = LoginFragment()
            }
            ConstVariables.SHOW_FRAGMENT_REGISTER -> {
                layout_main_tab.visibility = View.GONE
                fr = RegisterFragment()
            }
            ConstVariables.SHOW_FRAGMENT_SETTINGS -> {
                fr = SettingsFragment()

                if (user != null) {
                    val bundle = Bundle()
                    bundle.putSerializable("UserItem", user)
                    fr.arguments = bundle
                }
            }
            img_stampyou_main_tab.id -> {
                layout_main_tab.visibility = View.VISIBLE
                img_stampyou_main_tab.isSelected = true
                img_stampme_main_tab.isSelected = false
                img_home_main_tab.isSelected = false

                fr = StampYouFragment()

                if (user != null) {
                    val bundle = Bundle()
                    bundle.putSerializable("UserItem", user)
                    fr.arguments = bundle
                }

            }
            img_home_main_tab.id -> {
                layout_main_tab.visibility = View.VISIBLE
                img_home_main_tab.isSelected = true
                img_stampyou_main_tab.isSelected = false
                img_stampme_main_tab.isSelected = false

                fr = NearbyShopsFragment()

                if (user != null) {
                    val bundle = Bundle()
                    bundle.putSerializable("UserItem", user)
                    fr.arguments = bundle
                }
            }
            img_stampme_main_tab.id -> {
                layout_main_tab.visibility = View.VISIBLE
                img_stampme_main_tab.isSelected = true
                img_stampyou_main_tab.isSelected = false
                img_home_main_tab.isSelected = false

                fr = StampMeFragment()

                if (user != null) {
                    val bundle = Bundle()
                    bundle.putSerializable("UserItem", user)
                    fr.arguments = bundle
                }

            }
        }

        if (fr.isAdded) {
            ft.show(fr)
        } else {
            ft.addToBackStack(null)
            ft.add(mContentView.id, fr, fr.javaClass.simpleName)
        }

        ft.commit()
    }

    @Subscribe
    fun onEvent(obj: EventBusObject) {
        Log.d(TAG, "##### onEVent ##### obj : ${obj.key}")
        when (obj.key) {
            ConstVariables.EVENTBUS_REQUELST_LOGIN -> mPresenter.requestLogin(obj.val1 as User)
            ConstVariables.EVENTBUS_REQUELST_REGISTER -> mPresenter.requestRegister(obj.val1 as User)
            ConstVariables.EVENTBUS_REQUEST_LOGOUT -> mPresenter.requestDeleteUserInfo(this, obj.val1 as User)
            ConstVariables.EVENTBUS_SHOW_REGISTER -> handleFragment(ConstVariables.SHOW_FRAGMENT_REGISTER, null)
            ConstVariables.EVENTBUS_SHOW_LOGIN -> handleFragment(ConstVariables.SHOW_FRAGMENT_LOGIN, null)
            ConstVariables.EVENTBUS_SHOW_SETTINGS -> handleFragment(ConstVariables.SHOW_FRAGMENT_SETTINGS, obj.val1 as User)
            ConstVariables.EVENTBUS_POP_BACKSTACK -> handleFragment(ConstVariables.FRAGMENT_BACKSTACK, null)
            ConstVariables.EVENTBUS_SEND_CURRENT_STORE -> mCurrentStore = obj.val1 as Shops
        }
    }

    override fun onBackPressed() {
        Log.d(TAG, "##### onBackPressed ##### ")
        mPresenter.requestBackPressed()
    }
}
