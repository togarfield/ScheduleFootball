package com.example.togar.schedulefootball.utils

import kotlinx.coroutines.Dispatchers

open class CoroutineContextProvider {
    open val main: kotlin.coroutines.CoroutineContext by lazy { Dispatchers.Main }
    open val io by lazy { Dispatchers.IO }
}