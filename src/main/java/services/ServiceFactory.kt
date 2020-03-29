package services

import data.DataContextFactory

class ServiceFactory {

    fun getSqliteService(DataContext: DataContextFactory?): SqliteService {
        return SqliteService(DataContext!!)
    }

    fun getAuthService(DataContext: DataContextFactory?): AuthService {
        return AuthService(DataContext!!)
    }

    fun getUsersService(DataContext: DataContextFactory?): UserService {
        return UserService(DataContext!!)
    }

}