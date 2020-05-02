package controllers.auth

import com.auth0.jwt.interfaces.DecodedJWT
import data.DataContextFactory
import datamodels.User
import io.javalin.http.Context
import javalinjwt.JWTProvider
import javalinjwt.JavalinJWT
import javalinjwt.examples.JWTResponse
import models.responses.Response
import providers.JWTProviderFactory
import services.ServiceFactory
import java.util.*


class AuthController (
        private val ctx: Context,
        private val dataContextFactory: DataContextFactory,
        serviceFactory: ServiceFactory,
        jwtProvider: JWTProviderFactory?
) {

    private var jwtP = jwtProvider

    init {
    }

    fun getAuthToken (): Context {

        // TODO: validate psw

        // TODO: Get User
        // dummy
        val user = User(1,"user2", "User 2", "user2@korma.com")

        val token = this.jwtP?.getJWTProvider()!!.generateToken(user)

        // send the JWT response
        return ctx.json(Response(1, JWTResponse(token)))
    }
}