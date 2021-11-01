package database

import com.mindeurfou.database.course.CourseDao
import com.mindeurfou.database.course.CourseDaoImpl
import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.database.hole.HoleTable
import com.mindeurfou.model.course.incoming.PutCourseBody
import com.mindeurfou.model.course.outgoing.Course
import com.mindeurfou.model.course.outgoing.CourseDetails
import com.mindeurfou.service.CourseService
import com.mindeurfou.utils.GBException
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class CourseDaoTest : BaseDaoTest(){

    private val courseDao: CourseDao = CourseDaoImpl()

    @Test
    fun `create and retrieve a course`() {
        transaction {
            createSchema()
            val validCourse = DbInstrumentation.validPostCourseBody()
            val courseId = courseDao.insertCourse(validCourse)
            val courseDetails = courseDao.getCourseById(courseId)

            courseDetails ?: throw IllegalStateException("courseDetails shouldn't be null")

            assertThat(courseDetails).isEqualTo(
                CourseDetails(
                    courseId,
                    validCourse.name,
                    validCourse.numberOfHOles,
                    validCourse.par,
                    0,
                    LocalDate.now(),
                    4,
                    DbInstrumentation.listOfHoles()
                )
            )
        }
    }

    @Test
    fun `get non existing course`() {
        transaction {
            createSchema()
            val course = courseDao.getCourseById(1)
            assertEquals(null, course)
        }
    }

    @Test
    fun updateCourse() {
        transaction {
            createSchema()
            val validPutCourseBody = DbInstrumentation.validPostCourseBody()
            val notPresentId = 1
            assertThrows(GBException::class.java) {
                courseDao.updateCourse(
                    PutCourseBody(
                        notPresentId,
                        validPutCourseBody.name,
                        validPutCourseBody.numberOfHOles,
                        validPutCourseBody.par,
                        10,
                        4,
                        DbInstrumentation.listOfHoles()
                    )
                )
            }

            val courseId = courseDao.insertCourse(validPutCourseBody)

            val updatedCourseDetails = courseDao.updateCourse(
                PutCourseBody(
                    courseId,
                    validPutCourseBody.name,
                    validPutCourseBody.numberOfHOles,
                    validPutCourseBody.par,
                    10,
                    4,
                    DbInstrumentation.listOfHoles()
                )
            )

            assertThat(updatedCourseDetails).isEqualTo(
                CourseDetails(
                    courseId,
                    validPutCourseBody.name,
                    validPutCourseBody.numberOfHOles,
                    validPutCourseBody.par,
                    10,
                    LocalDate.now(),
                    4,
                    DbInstrumentation.listOfHoles()
                )
            )
        }
    }

    @Test
    fun deleteCourse() {
        transaction {
            createSchema()
            var result = courseDao.deleteCourse(1)
            assertEquals(false, result)

            val validPostCourse = DbInstrumentation.validPostCourseBody()
            val courseId = courseDao.insertCourse(validPostCourse)

            result = courseDao.deleteCourse(courseId)
            assertEquals(result, true)
        }
    }

    @Test
    fun getCourseByName() {
        transaction {
            createSchema()
            var course = courseDao.getCourseByName("course not present")
            assertEquals(course, null)

            val validPostCourseBody = DbInstrumentation.validPostCourseBody()
            val courseId = courseDao.insertCourse(validPostCourseBody)
            course = courseDao.getCourseByName(validPostCourseBody.name)

            course ?: throw IllegalStateException("course shouldn't be null")

            assertThat(course).isEqualTo(
                Course(
                    courseId,
                    validPostCourseBody.name,
                    validPostCourseBody.numberOfHOles,
                    validPostCourseBody.par,
                    0,
                    4,
                    LocalDate.now()
                )
            )
        }
    }

    @Test
    fun getCourses() {
        transaction {
            createSchema()

            // TODO finish function and test nullity

            val validCourseBody = DbInstrumentation.validPostCourseBody()

            val courseId0 = courseDao.insertCourse(validCourseBody)
            val courseId1 = courseDao.insertCourse(validCourseBody)

            val courses = courseDao.getCourses(limit = CourseService.GET_COURSES_DEFAULT_LIMIT, offset = CourseService.GET_COURSES_DEFAULT_OFFSET)

            assertEquals(courses.size, 2)
            assertThat(courses[0]).isEqualTo(
                Course(
                    courseId1,
                    validCourseBody.name,
                    validCourseBody.numberOfHOles,
                    validCourseBody.par,
                    0,
                    4,
                    LocalDate.now()
                )
            )
            assertThat(courses[1]).isEqualTo(
                Course(
                    courseId0,
                    validCourseBody.name,
                    validCourseBody.numberOfHOles,
                    validCourseBody.par,
                    0,
                    4,
                    LocalDate.now()
                )
            )
        }
    }



    override fun createSchema() {
        SchemaUtils.create(CourseTable)
        SchemaUtils.create(HoleTable)
    }

    override fun dropSchema() {
        SchemaUtils.drop(CourseTable)
        SchemaUtils.drop(HoleTable)
    }

}