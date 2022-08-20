package com.mankart.eshop.core.utils

import android.util.Base64
import android.util.Base64.encodeToString
import com.mankart.eshop.core.BuildConfig
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class SecurityUtils {
    private val secureRandom = SecureRandom()

    fun encryptData(data: String): String{
        val cipher: Cipher = Cipher.getInstance("AES")
        val secretKeySpec = generateSecretKey()
        val iv = secureRandom.generateSeed(16)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)
        val cipherText: ByteArray = cipher.doFinal(data.toByteArray())
        return encodeToString(cipherText, Base64.DEFAULT)
    }

    fun decryptData(encryptedText: String): String{
        val cipherText = Base64.decode(encryptedText, Base64.DEFAULT)
        val cipher: Cipher = Cipher.getInstance("AES")
        val secretKeySpec = generateSecretKey()
        val iv = secureRandom.generateSeed(16)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)
        return String(cipher.doFinal(cipherText))
    }

    private fun generateSecretKey(): SecretKeySpec {
        val actualKey = BuildConfig.SECRET_KEY
        return SecretKeySpec(actualKey.toByteArray(), "AES")
    }
}