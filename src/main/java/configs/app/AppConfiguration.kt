package configs.app

import com.auth0.jwt.interfaces.DecodedJWT
import data.DataContextFactory
import datamodels.User
import io.javalin.core.JavalinConfig
import io.javalin.core.security.Role
import javalinjwt.JWTProvider
import javalinjwt.JavalinJWT
import providers.JWTProviderFactory
import services.ServiceFactory
import java.util.*


class AppConfiguration (
        c: JavalinConfig,
        serviceFactory: ServiceFactory,
        dataContextFactory: DataContextFactory,
        jwtProvider: JWTProviderFactory?
) {

    private val authService = serviceFactory.getAuthService(dataContextFactory)
    private val configuration: JavalinConfig = c

    private var jwtP: JWTProvider? = null

    internal enum class MyRole : Role {
        WEB_CONTEXT,
        API,
        USER,
        FRIEND,
        REGISTERED,
        SECURE,
        ADMIN
    }

    init {
        // TODO: always auth is needed?
        this.jwtP = jwtProvider?.getJWTProvider()
    }

    private fun getAppRoleKey(k: String): List<datamodels.Origin> {
        return this.authService.getAppRoleByKey(k)
    }

    private fun getRoleById(id: Int): List<datamodels.Role> {
        return this.authService.getUserRoleById(id)
    }

    private fun getContextRole(ctx: io.javalin.http.Context): Role? {

        val vkey = ctx.header("origin").toString()
        val roleData = this.getAppRoleKey(vkey)

        if (ctx.url().contains("web")) {
            // open web route
            return MyRole.WEB_CONTEXT
        }

        if (roleData.isNotEmpty() && roleData[0].role!! > 5) {

            println("role app ->" + roleData[0].role)

            // Open Routes
            return when (roleData[0].role) {
                6 -> MyRole.API // open api calls
                else -> MyRole.WEB_CONTEXT // normal web app
            }

        } else if (roleData.isEmpty()) {

            ctx.status(401).result("Unauthorized")

        } else {
            // inspect token
            val decodedJWT: Optional<DecodedJWT> = JavalinJWT.getTokenFromHeader(ctx)
                    .flatMap(jwtP!!::validateToken)

            if (!decodedJWT.isPresent) {

                ctx.status(401).result("Missing or invalid token")

            } else {

                val user = User(
                        decodedJWT.get().getClaim("id").asInt(),
                        decodedJWT.get().getClaim("name").asString(),
                        decodedJWT.get().getClaim("username").asString(),
                        decodedJWT.get().getClaim("email").asString()
                )

                println("user -> $user")

                val roles = this.getRoleById(user.id)

                // valid role scale
                if (roles.isNotEmpty() && roles[0].role!! <= 5) {

                    // ok, its a valid Role API -> check key on database

                    println("user role -> " + roles[0].role)

                    //
                    return when (roles[0].role as Int) {
                        0 -> MyRole.ADMIN
                        1 -> MyRole.USER // registered user
                        2 -> MyRole.FRIEND // registered and level 1 or paying friend
                        5 -> MyRole.SECURE // critical or special ops
                        else -> MyRole.REGISTERED // normal web user/org registered
                    }
                }
            }
        }

        return null
    }

    fun getRolesConfig(){
        this.configuration.accessManager { handler, ctx, permittedRoles ->
            val userRole = getContextRole(ctx) // determine user role based on request
            if (permittedRoles.contains(userRole)) {
                handler.handle(ctx)
            } else {
                ctx.status(401).result("Unauthorized")
            }
        }
    }


}