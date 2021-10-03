package com.mindeurfou.utils

import io.ktor.application.*

fun ApplicationCall.addCacheHeader() {
    response.headers.append("Cache-Control", "public, max-age=5")
}