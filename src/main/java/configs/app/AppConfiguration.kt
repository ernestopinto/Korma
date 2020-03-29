package configs.app

import data.DataContextFactory
import io.javalin.core.JavalinConfig
import io.javalin.core.security.Role
import services.AuthService
import services.ServiceFactory


class AppConfiguration (c: JavalinConfig, serviceFactory: ServiceFactory, dataContextFactory: DataContextFactory) {

    private val authService = serviceFactory.getAuthService(dataContextFactory)
    private val configuration: JavalinConfig = c

    internal enum class MyRole : Role {
        WEB_CONTEXT,
        API,
        USER,
        SECURE,
        ADMIN
    }

    private fun getRoleKey(k: String): List<datamodels.Role> {
        return this.authService.getUserRoleByKey(k)
    }

    private fun getUserRole(ctx: io.javalin.http.Context) : Role? {

        // determine user role based on request
        // typically done by inspecting headers

        val vkey = ctx.header("v-key").toString()
        val roleData = this.getRoleKey(vkey)

        // valid role scale
        if (roleData.isNotEmpty() && roleData[0].role!! <= 5){
            // ok, its a valid Role API -> check key on database
            return when (roleData[0].role){
                0 -> MyRole.ADMIN
                1 -> MyRole.USER // registered user
                5 -> MyRole.SECURE // critical
                else -> MyRole.API // normal web user/org registered for API 3,4
            }
        }

        if (ctx.url().contains("web")){
            return MyRole.WEB_CONTEXT
        }
        return null
    }

    fun getRolesConfig(){
        this.configuration.accessManager { handler, ctx, permittedRoles ->
            val userRole = getUserRole(ctx) // determine user role based on request
            if (permittedRoles.contains(userRole)) {
                handler.handle(ctx)
            } else {
                ctx.status(401).result("Unauthorized")
            }
        }
    }


}