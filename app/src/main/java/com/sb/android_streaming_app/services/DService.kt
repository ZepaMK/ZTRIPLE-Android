package com.sb.android_streaming_app.services

import android.util.Log
import com.connectsdk.core.AppInfo
import com.connectsdk.core.Util
import com.connectsdk.service.DIALService
import com.connectsdk.service.capability.Launcher.AppLaunchListener
import com.connectsdk.service.capability.listeners.ResponseListener
import com.connectsdk.service.command.ServiceCommand
import com.connectsdk.service.command.ServiceCommand.ServiceCommandProcessor
import com.connectsdk.service.command.ServiceCommandError
import com.connectsdk.service.command.ServiceSubscription
import com.connectsdk.service.command.URLServiceSubscription
import com.connectsdk.service.config.ServiceDescription
import com.connectsdk.service.sessions.LaunchSession
import com.connectsdk.service.sessions.LaunchSession.LaunchSessionType

class DService : ServiceCommandProcessor {

    fun getLaunchCommand(
        appInfo: AppInfo,
        params: Any?,
        service: DIALService,
    ): ServiceCommand<ResponseListener<Any>> {
        val command = ServiceCommand<ResponseListener<Any>>(this,
            requestURL(appInfo.name, service.serviceDescription), params, object : ResponseListener<Any?> {
                override fun onError(error: ServiceCommandError) {
                    Log.d("DID NOT WORK", "Did not made connection")
                }

                override fun onSuccess(`object`: Any?) {
                    val launchSession = LaunchSession.launchSessionForAppId(appInfo.id)
                    launchSession.appName = appInfo.name
                    launchSession.sessionId = `object` as String?
                    launchSession.service = service
                    launchSession.sessionType = LaunchSessionType.App
                    Log.d("DID WORK", requestURL(appInfo.name, service.serviceDescription).toString() + params.toString())
                }
            })
        return command
    }

    override fun unsubscribe(subscription: URLServiceSubscription<*>?) {
        TODO("Not yet implemented")
    }

    override fun unsubscribe(subscription: ServiceSubscription<*>?) {
        TODO("Not yet implemented")
    }

    override fun sendCommand(command: ServiceCommand<*>?) {
        TODO("Not yet implemented")
    }

    private fun requestURL(appName: String, serviceDescription: ServiceDescription): String? {
        val applicationURL: String = (serviceDescription.applicationURL)
            ?: throw IllegalStateException("DIAL service application URL not available")
        val sb = StringBuilder()
        sb.append(applicationURL)
        if (!applicationURL.endsWith("/")) sb.append("/")
        sb.append(appName)
        return sb.toString()
    }
}