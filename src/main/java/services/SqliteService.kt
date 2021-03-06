package services

import datamodels.User
import kotliquery.Row
import kotliquery.Session
import kotliquery.queryOf
import data.DataContextFactory as DataContextFactoryOnSqLite

class SqliteService(data: DataContextFactoryOnSqLite) {

    private var sessionContext: Session? = null

    private val convertUser: (Row) -> User = { row ->
        User(
                row.int("id"),
                row.stringOrNull("username"),
                row.stringOrNull("name"),
                row.stringOrNull("email")
        )
    }

    init {
        this.sessionContext = data.getKotlinQueryContextForSqlite("jdbc:sqlite:example.db")
    }

    fun getAllIds(): List<Int> {
        return this.sessionContext!!.run(
                queryOf("select * from users")
                        .map { row -> row.int("id") }.asList
        )
    }

    fun getAllUsers(): List<User> {
        return this.sessionContext!!.run(
                queryOf("select * from users").map(convertUser).asList
        )
    }

    fun getUserById(id: String): List<User> {
        return this.sessionContext!!.run(
                queryOf("""select * from users where id = :id""", mapOf("id" to id))
                        .map(convertUser).asList
        )
    }

    fun getUserCredentialInterval(c1: String, c2: String): List<User> {
        return this.sessionContext!!.run(
                queryOf("""select * from users where credential >= :c1 AND credential <= :c2""",
                        mapOf("c1" to c1, "c2" to c2))
                        .map(convertUser).asList
        )
    }

}