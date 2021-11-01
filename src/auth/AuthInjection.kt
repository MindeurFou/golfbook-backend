package com.mindeurfou.auth

import org.koin.dsl.module

object AuthInjection {
    val koinAuthModule = module {
        single { JWTConfig.verifier }
    }
}