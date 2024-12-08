package com.example.receitasfrequencia.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.receitasfrequencia.model.User

@Dao
interface Daouser {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    @Query("SELECT * FROM users WHERE email = :email AND password = :hashedPassword")
    suspend fun getUserByEmailAndPassword(email: String, hashedPassword: String): User?

}