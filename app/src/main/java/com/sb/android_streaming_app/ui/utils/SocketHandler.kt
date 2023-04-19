package com.sb.android_streaming_app.ui.utils

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import kotlin.reflect.KProperty0

/**
 * Created by Zep S. on 11/04/2023.
 */
object SocketHandler {

    var mSocket: Socket? = null

    @Synchronized
    fun setSocket(domain: String) {
        try {
            // "http://10.0.2.2:3000" is the network your Android emulator must use to join the localhost network on your computer
            // "http://localhost:3000/" will not work
            // If you want to use your physical phone you could use your ip address plus :3000
            // This will allow your Android Emulator and physical device at your home to connect to the server
            Log.d("2ndScreenAPP", domain)
            mSocket = IO.socket(domain)
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket? {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket?.connect()
        // This makes sure the user is in the room with the right client
        SocketHandler.mSocket?.emit("join-room", "userId", "Connected to ${android.os.Build.MODEL}")
    }

    @Synchronized
    fun closeConnection() {
        mSocket?.emit("disconnect_alert", "userId", "Disconnected from ${android.os.Build.MODEL}")
        mSocket?.disconnect()
    }
}

