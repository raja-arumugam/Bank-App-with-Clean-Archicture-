package com.example.bankapp.data.local.db

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.example.bankapp.common.Constants.DB_NAME
import com.example.bankapp.data.local.dao.AuthDao
import com.example.bankapp.data.local.entity.RegistrationModel
import com.example.bankapp.data.local.entity.TransactionModel
import com.example.bankapp.data.local.entity.UsersModel
import net.sqlcipher.database.SupportFactory
import java.io.File
import java.security.SecureRandom

@Database(
    entities = [RegistrationModel::class, UsersModel::class, TransactionModel::class],
    version = 1
)
abstract class BankDataBase : RoomDatabase() {

    abstract fun authDao(): AuthDao

    companion object {

        @Volatile
        private var instance: BankDataBase? = null

        @RequiresApi(Build.VERSION_CODES.O)
        fun getInstance(context: Context): BankDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun buildDatabase(context: Context): BankDataBase {
            val passphrase = getPassphrase(context)
            val helperFactory = SupportFactory(passphrase)

            return Room.databaseBuilder(
                context.applicationContext,
                BankDataBase::class.java,
                DB_NAME
            )
                .openHelperFactory(helperFactory)
                .fallbackToDestructiveMigration()
                .build()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getPassphrase(context: Context): ByteArray {
            val file = File(context.filesDir, "passphrase.bin")
            val encryptedFile = EncryptedFile.Builder (
                file,
                context.applicationContext,
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            return if (file.exists()) {
                encryptedFile.openFileInput().use { it.readBytes() }
            } else {
                generatePassphrase().also { passphrase ->
                    encryptedFile.openFileOutput().use { it.write(passphrase) }
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun generatePassphrase(): ByteArray {
            val random = SecureRandom.getInstanceStrong()
            val result = ByteArray(32)

            random.nextBytes(result)
            while (result.contains(0)) {
                random.nextBytes(result)
            }

            return result
        }

    }

}