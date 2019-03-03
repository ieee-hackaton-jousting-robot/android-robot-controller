package com.littlebencreations.robotcontroller

import de.lehmann.logic.CobsAlgorithm

class CobsConvert {

    val cobsAlgorithm: CobsAlgorithm = CobsAlgorithm()

    fun ConvertResponseToCobs(r: WebsocketServer.WebsocketResponse): ByteArray {
        return cobsAlgorithm.encode(byteArrayOf(ConvertMotorFloatToInt(r.left), ConvertMotorFloatToInt(r.right)))
    }

    fun ConvertMotorFloatToInt(f: Float): Byte {
        var newf = f
        if (newf > 1) newf = 1F
        if (newf < -1) newf = -1F

        return (newf * 127).toByte()
    }
}