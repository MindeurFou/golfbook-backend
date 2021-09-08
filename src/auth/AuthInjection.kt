package com.mindeurfou.auth

import com.auth0.jwt.interfaces.JWTVerifier
import org.koin.dsl.module

object AuthInjection {
    val koinAuthModule = module {
        single { JWTConfig.verifier }
    }
}