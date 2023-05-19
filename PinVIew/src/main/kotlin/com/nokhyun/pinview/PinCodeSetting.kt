package com.nokhyun.pinview

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

internal object PinCodeSetting {
    private var masterKey: MasterKey? = null
    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context, fileName: String) {
        createMasterKey(context)
        createSharedPreferences(context, fileName)
    }

    private fun createMasterKey(context: Context) {
        masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private fun createSharedPreferences(context: Context, fileName: String) {
        masterKey?.also { mk ->
            sharedPreferences = EncryptedSharedPreferences.create(
                context,
                fileName,
                mk,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    fun comparePinCode(key: String, inputValue: String): Boolean {
        return sharedPreferences?.getString(key, null) == inputValue
    }

    fun savePinCode(key: String, value: String): Boolean {
        val keys = sharedPreferences?.all?.keys
        if (!keys.isNullOrEmpty() && !keys.contains(key)) throw StorageExcessException("Only one PinCode can be stored.")

        return sharedPreferences?.let {
            it.edit { putString(key, value) }
            true
        } ?: false
    }

    fun clearPinCode() {
        sharedPreferences?.edit {
            clear()
        }
    }

    fun pinState(key: String): Boolean {
        return sharedPreferences?.getString(key, null) != null
    }

    private class StorageExcessException(message: String) : Exception(message)
}