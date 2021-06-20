package com.mindeurfou

interface DatabaseMapper<ResultRow, DomainModel> {
    fun mapFromEntity(resultRow: ResultRow): DomainModel
}