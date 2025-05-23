package app.aaps.pump.diaconn.packet

import app.aaps.core.interfaces.logging.LTag
import dagger.android.HasAndroidInjector
import app.aaps.pump.diaconn.DiaconnG8Pump
import javax.inject.Inject

/**
 * DisplayTimeInquireResponsePacket
 */
class DisplayTimeInquireResponsePacket(injector: HasAndroidInjector) : DiaconnG8Packet(injector) {

    @Inject lateinit var diaconnG8Pump: DiaconnG8Pump

    init {
        msgType = 0x8E.toByte()
        aapsLogger.debug(LTag.PUMPCOMM, "DisplayTimeInquireResponsePacket init")
    }

    override fun handleMessage(data: ByteArray) {
        val result = defect(data)
        if (result != 0) {
            aapsLogger.debug(LTag.PUMPCOMM, "DisplayTimeInquireResponsePacket Got some Error")
            failed = true
            return
        } else failed = false

        val bufferData = prefixDecode(data)
        val result2 = getByteToInt(bufferData)
        if (!isSuccInquireResponseResult(result2)) {
            failed = true
            return
        }

        diaconnG8Pump.lcdOnTimeSec = getByteToInt(bufferData)

        aapsLogger.debug(LTag.PUMPCOMM, "Result --> ${diaconnG8Pump.result}")
        aapsLogger.debug(LTag.PUMPCOMM, "lcdOnTimeSec --> ${diaconnG8Pump.lcdOnTimeSec}")
    }

    override val friendlyName = "PUMP_DISPLAY_TIME_INQUIRE_RESPONSE"
}
