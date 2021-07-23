package com.mindeurfou.utils

interface DatabaseMapper<ResultRow, DomainModel> {
    fun mapFromEntity(resultRow: ResultRow): DomainModel
}