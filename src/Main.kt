package com.belsoft

import io.ktor.application.*
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.features.HttpsRedirect
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.*

/**
 * Entry point of the application: main method that starts an embedded server using Netty,
 * processes the application.conf file, interprets the command line args if available
 * and loads the application modules.
 */
fun main(args: Array<String>) {
    embeddedServer(Netty, applicationEngineEnvironment {
        this.log = LoggerFactory.getLogger("ktor.application")
        this.module {
            install(LetsEncrypt) {
                email = "ciprian.salomir@gmail.com"
                setProduction()
                addDomainSet("belsoft.cf")
            }
            install(DefaultHeaders)
            install(CallLogging)
            install(HttpsRedirect) {
                sslPort = 443
            }
            routing {
                get("/") {
                    call.respondText("HELLO!")
                }
            }
        }

        connector {
            this.port = 8889
            this.host = "34.66.48.12"
        }
        sslConnector(LetsEncryptCerts.keyStore, LetsEncryptCerts.alias, { charArrayOf() }, { charArrayOf() }) {
            this.port = 8890
            this.host = "34.66.48.12"
        }
    }) {
        // Netty config
    }.start(wait = true)
}