package com.imaginato.homeworkmvvm.data.local.base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imaginato.homeworkmvvm.data.local.demo.Demo
import com.imaginato.homeworkmvvm.data.local.demo.DemoDao
import com.imaginato.homeworkmvvm.data.local.login.Login
import com.imaginato.homeworkmvvm.data.local.login.LoginDao

@Database(entities = [Demo::class, Login::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val demoDao: DemoDao
    abstract val loginDao: LoginDao
}