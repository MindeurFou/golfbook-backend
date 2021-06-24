package com.mindeurfou.database.course.injection

import com.mindeurfou.database.course.CourseDao
import com.mindeurfou.database.course.CourseDaoImpl
import com.mindeurfou.database.course.CourseDbMapper
import com.mindeurfou.database.course.CourseTable
import org.koin.dsl.module

object CourseDaoInjection {
    val courseDaoModule = module {
        single { CourseTable }
        single { CourseDbMapper }
        single <CourseDao>{ CourseDaoImpl() }
    }
}