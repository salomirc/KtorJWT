package com.belsoft

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.belsoft.routes.*
import com.belsoft.utils.localPath
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import java.io.File

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


private val algorithm = Algorithm.HMAC256("secret")
fun makeToken(issuer: String, audience: String): String = JWT.create()
    .withSubject("Authentication")
    .withAudience(audience)
    .withIssuer(issuer)
    .sign(algorithm)

private fun makeJwtVerifier(issuer: String, audience: String): JWTVerifier = JWT
    .require(algorithm)
    .withAudience(audience)
    .withIssuer(issuer)
    .build()

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val issuer = environment.config.property("jwt.domain").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val realm = environment.config.property("jwt.realm").getString()

    val token = makeToken(issuer, audience)
    println("token = $token")

    install(Authentication) {
        //BasicAuth
        basic("myBasicAuth") {
            this.realm = realm
            validate {
                if (
                    when {
                        it.name == "test" && it.password == "password" -> true
                        it.name == "salox" && it.password == "redhat" -> true
                        else -> false
                    }
                ) UserIdPrincipal(it.name) else null
            }
        }

        //JWT Auth
        val jwtVerifier = makeJwtVerifier(issuer, audience)
        jwt("myJWTAuth") {
            verifier(jwtVerifier)
            this.realm = realm
            validate { credential ->
                if (credential.payload.audience.contains(audience))
                    JWTPrincipal(credential.payload)
                else
                    null
            }
        }
    }

    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
        }
    }

    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    install(CallLogging)

    routing {
        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }

        //Basic Auth
        authenticate("myBasicAuth") {
            login(token)
        }

        //JWT AUth
        authenticate("myJWTAuth") {
            who()
            json()
            getStaticFilesName()
            getPrivateFilesName()
            getFileAsByteArray()
        }

        root()
        rootPost()
    }
}

