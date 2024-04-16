package com.example.esempio.models

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

object AESCrypt {

    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"

    fun encrypt(input: String, key: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(key))
        val encryptedBytes = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(input: String, key: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, generateKey(key))
        val decryptedBytes = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
        return String(decryptedBytes, Charsets.UTF_8)
    }

    private fun generateKey(key: String): SecretKeySpec {
        val keySpec = ByteArray(16)
        val keyBytes = key.toByteArray(Charsets.UTF_8)
        System.arraycopy(keyBytes, 0, keySpec, 0, Math.min(keyBytes.size, keySpec.size))
        return SecretKeySpec(keySpec, ALGORITHM)
    }
}
