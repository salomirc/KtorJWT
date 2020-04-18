package com.belsoft

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.*

fun main(args: Array<String>) {
    embeddedServer(Netty, applicationEngineEnvironment {
        this.log = LoggerFactory.getLogger("ktor.application")
        this.module {
            install(LetsEncrypt) {
                email = "ciprian.salomir@gmail.com"
                setProduction()
                addDomainSet("belsoft.cf")
            }
            routing {
                get("/") {
                    call.respondText("HELLO!")
                }
            }
        }

        connector {
            this.port = 8889
            this.host = "0.0.0.0"
        }
        sslConnector(LetsEncryptCerts.keyStore, LetsEncryptCerts.alias, { charArrayOf() }, { charArrayOf() }) {
            this.port = 8890
            this.host = "0.0.0.0"
        }
    }) {
        // Netty config
    }.start(wait = true)
}