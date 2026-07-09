package com.biglitecode.familyhub.util

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import android.widget.Toast

/**
 * Sends pending-task reminder SMS messages.
 * Requires [android.Manifest.permission.SEND_SMS] at runtime before calling.
 */
object SmsHelper {

    fun sendReminderSms(
        context: Context,
        phoneNumber: String,
        taskTitle: String
    ): Boolean {
        val message =
            "FamilyHub Reminder: '$taskTitle' is still pending. Please complete it soon!"
        return try {
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                context.getSystemService(SmsManager::class.java)
            } else {
                @Suppress("DEPRECATION")
                SmsManager.getDefault()
            }
            if (smsManager == null) {
                Toast.makeText(context, "SMS service unavailable", Toast.LENGTH_SHORT).show()
                return false
            }
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            true
        } catch (_: SecurityException) {
            Toast.makeText(
                context,
                "SMS permission is required to send reminders",
                Toast.LENGTH_SHORT
            ).show()
            false
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Could not send SMS: ${e.message ?: "unknown error"}",
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }
}
