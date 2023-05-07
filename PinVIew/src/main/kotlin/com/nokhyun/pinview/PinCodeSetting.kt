package com.nokhyun.pinview

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

internal object PinCodeSetting {
    private var masterKey: MasterKey? = null
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

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
            ).also { sp ->
                editor = sp.edit()
            }
        }
    }

    fun comparePinCode(key: String, inputValue: String): Boolean {
        return sharedPreferences?.getString(key, null) == inputValue
    }

    fun savePinCode(key: String, value: String): Boolean {
        return editor?.run {
            putString(key, value)
            commit()
        } ?: false
    }

    fun clearPinCode() {
        editor?.clear()
    }
}