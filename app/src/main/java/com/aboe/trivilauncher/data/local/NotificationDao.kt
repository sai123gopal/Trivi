package com.aboe.trivilauncher.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.aboe.trivilauncher.data.local.entity.NotificationEntity

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Update
    suspend fun updateNotification(notification: NotificationEntity)

    @Transaction
    suspend fun upsertNotification(notification: NotificationEntity) {
        val existingNotification = getNotificationById(notification.id)

        if (existingNotification == null) {
            insertNotification(notification)
        } else {
            val mergedNotification = existingNotification.copy(
                title = notification.title,
                subText = notification.subText,
                text = listOfNotNull(existingNotification.text,
                    if (existingNotification.text != notification.text)
                        notification.text else null
                ).joinToString(" • "),
                bigText = notification.bigText,
                packageName = notification.packageName,
                timestamp = notification.timestamp
            )

            updateNotification(mergedNotification)
        }
    }

    @Query("SELECT * FROM notifications WHERE id = :id")
    suspend fun getNotificationById(id: Long): NotificationEntity?

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    suspend fun getAllNotifications(): List<NotificationEntity>

    @Query("DELETE FROM notifications WHERE timestamp < :timestamp")
    suspend fun deleteNotifications(timestamp: Long)

}