package com.jhmk.stampme.Module.DataBase

import com.google.firebase.database.FirebaseDatabase

object DataBaseReference {
    val mUsersDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    val mStampsDatabaseReference = FirebaseDatabase.getInstance().getReference("Stamps")
    val mShopsDatabaseReference = FirebaseDatabase.getInstance().getReference("Shops")
}