package com.littlebencreations.robotcontroller

import android.util.Log
import com.beust.klaxon.JsonArray
import com.beust.klaxon.Klaxon
import com.koushikdutta.async.http.WebSocket
import com.koushikdutta.async.http.server.AsyncHttpServer
import java.lang.Exception

class WebsocketServer {

    interface DriveMessageCallback {
        fun DriveMessageRecieved(wsr: WebsocketResponse)
    }


    class WebsocketResponse (var left: Float, var right: Float)


    val server: AsyncHttpServer = AsyncHttpServer()
    val sockets: ArrayList<WebSocket> = ArrayList()
    var driveMessageCallback: DriveMessageCallback? = null

    init {
        server.get(
            "/"
        ) { _, response ->
            response.send("Hello!!!")
            }


        server.websocket("/live") {
            // onConnected
                webSocket, _ ->
            run {
                sockets.add(webSocket)
                webSocket.setClosedCallback { ex: Exception? ->
                    run {
                        try {
                            if (ex != null) {
                                Log.e("WebSocket", "An error occurred", ex)
                            }
                        } finally {
                            sockets.remove(webSocket)
                        }
                    }
                }

                webSocket.setStringCallback { s: String? -> run {
                    if (s != null) {
                        this.messageRecieved(s)
                    }
                }
                }
            }
        }


    }

    private fun messageRecieved(s: String) {
        Log.d("messageRecieved", s)
        val result = Klaxon().parse<WebsocketResponse>(s)
        if (result != null) {
            driveMessageCallback?.DriveMessageRecieved(result)
        }

    }

    fun startServer() {
        server.listen(8080)
    }

    fun stopServer() {
        server.stop()
    }

    fun setOnDriveMessage(driveMessageCallback: DriveMessageCallback) {
        this.driveMessageCallback = driveMessageCallback
    }


}