package com.mindeurfou.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.mindeurfou.model.player.outgoing.Player
import java.util.*

object JWTConfig {

    private const val secret = "rickIsBetterThanMorty"
    private const val issuer = "http://0.0.0.0:8080/"
    private const val audience = "http://0.0.0.0:8080/"
    private const val validity: Long = 3600000L * 24L // 24h
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    fun createToken(playerId: Int) = JWT.create()
        .withSubject("authentication")
        .withIssuer(issuer)
        .withClaim("playerId", playerId)
        .withExpiresAt(Date(System.currentTimeMillis() + validity))
        .sign(algorithm)

}