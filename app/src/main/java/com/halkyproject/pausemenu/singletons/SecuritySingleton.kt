package com.halkyproject.pausemenu.singletons

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SecuritySingleton private constructor() {
    companion object {
        private var _instance: SecuritySingleton? = null
        fun getInstance(): SecuritySingleton {
            if (_instance == null) {
                _instance = SecuritySingleton()
            }
            return _instance as SecuritySingleton
        }
    }

    fun getHash(s: String): String? {
        return try {
            val bigInt = BigInteger(1, MessageDigest.getInstance("MD5").digest((s + s.length).toByteArray()))
            val hashtext = StringBuilder(bigInt.toString(16))
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length < 32) {
                hashtext.insert(0, "0")
            }
            hashtext.toString()
        } catch (e: NoSuchAlgorithmException) {
            null
        }
    }

}