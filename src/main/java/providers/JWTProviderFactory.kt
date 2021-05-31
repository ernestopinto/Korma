package providers

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import datamodels.User
import javalinjwt.JWTGenerator
import javalinjwt.JWTProvider


class JWTProviderFactory {

    private var algorithm: Algorithm? = null
    private var verifier: JWTVerifier? = null
    private var jwtProvider: JWTProvider? = null
    private var generator: JWTGenerator<User>? = null

    init {
        this.algorithm = Algorithm.HMAC256("pUj50diXlHFqDIpk2TBIWho71hvt8Gsz")
        this.verifier = JWT.require(algorithm).build()
        this.generator = JWTGenerator<User> { user: User, alg: Algorithm? ->
            val token: JWTCreator.Builder = JWT.create()
                    .withClaim("id", user.id)
                    .withClaim("name", user.name)
                    .withClaim("username", user.username)
                    .withClaim("email", user.email)
            token.sign(alg)
        }
    }

    fun generate(user: User, alg: Algorithm): String? {
        val token: JWTCreator.Builder = JWT.create()
                .withClaim("name", user.name)
                .withClaim("role", user.username)
        return token.sign(alg)
    }

    fun getJWTProvider (): JWTProvider{
        return JWTProvider(algorithm, generator, verifier)
    }

}