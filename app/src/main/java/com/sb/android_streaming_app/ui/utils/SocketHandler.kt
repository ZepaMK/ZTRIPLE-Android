package com.sb.android_streaming_app.ui.utils

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import kotlin.reflect.KProperty0

/**
 *    Title: Socket.io Connection on Android Kotlin to Node.js Server
 *    Author: Thushen Mohanarajah
 *    Date: 11/08/2021
 *    Code version: 1.0
 *    Availability: https://medium.com/@thushenarriyam/socket-io-connection-on-android-kotlin-to-node-js-server-71b218c160c9
 */
object SocketHandler {

    var mSocket: Socket? = null

    @Synchronized
    fun setSocket(domain: String) {
        try {
            // initialize Socket
            mSocket = IO.socket(domain)
        } catch (e: URISyntaxException) { }
    }

    @Synchronized
    fun getSocket(): Socket? {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket?.connect()
        // This makes sure the user is in the room with the right client
        mSocket?.emit("join-room", "userId", "Connected to ${android.os.Build.MODEL}")
    }

    @Synchronized
    fun closeConnection() {
        // This makes sure a disconnection alert is shown on the smart TV application
        mSocket?.emit("disconnect_alert", "userId", "Disconnected from ${android.os.Build.MODEL}")
        mSocket?.disconnect()
    }
}
