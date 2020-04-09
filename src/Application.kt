package com.belsoft

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.belsoft.routes.authenticate
import com.belsoft.routes.root
import com.belsoft.routes.rootPost
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.auth.*
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.gson.*
import io.ktor.util.KtorExperimentalAPI

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

        val jwtVerifier = makeJwtVerifier(issuer, audience)
        jwt {
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

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
        }
    }

    routing {
        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }

        install(StatusPages) {
            exception<Throwable> { e ->
                call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
            }
        }

        authenticate("myBasicAuth") {
            get("/login") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hello ${principal.name} the token is : $token")
            }
        }

        authenticate {
            route("/who") {
                handle {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    val subjectString = principal!!.payload.subject.removePrefix("auth0|")
                    call.respondText("Success, $subjectString")
                }
            }

            get("/json") {
                call.respond(mapOf("hello" to "world"))
            }
        }

        root()
        rootPost()
    }
}

