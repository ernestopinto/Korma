package services

import datamodels.Role
import kotliquery.Row
import kotliquery.Session
import kotliquery.queryOf
import data.DataContextFactory as DataContextFactoryOnSqLite

class AuthService (data: DataContextFactoryOnSqLite) {

    private var sessionContext: Session? = null

    private val convertRole: (Row) -> Role = { row ->
        Role(
                row.int("id"),
                row.int("user"),
                row.stringOrNull("key"),
                row.int("role")
        )
    }

    init {
        this.sessionContext = data.getKotlinQueryContextForSqlite("jdbc:sqlite:example.db")
    }

    fun getUserRoleByKey(key: String): List<Role> {
        return this.sessionContext!!.run(
                queryOf("""select * from roles where key = :key""", mapOf("key" to key))
                        .map(convertRole).asList
        )
    }

    fun getUserRoleById(id: Int): List<Role> {
        return this.sessionContext!!.run(
                queryOf("""select * from roles where id = :id""", mapOf("id" to id))
                        .map(convertRole).asList
        )
    }

    fun getAllRoles(): List<Role> {
        return this.sessionContext!!.run(
                queryOf("select * from roles").map(convertRole).asList
        )
    }


}