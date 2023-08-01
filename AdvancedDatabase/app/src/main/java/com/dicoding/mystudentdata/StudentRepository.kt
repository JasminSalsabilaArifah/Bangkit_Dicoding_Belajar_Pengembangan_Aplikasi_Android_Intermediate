package com.dicoding.mystudentdata

import androidx.lifecycle.LiveData
import com.dicoding.mystudentdata.database.Student
import com.dicoding.mystudentdata.database.StudentAndUniversity
import com.dicoding.mystudentdata.database.StudentDao
import com.dicoding.mystudentdata.database.StudentWithCourse
import com.dicoding.mystudentdata.database.UniversityAndStudent
import com.dicoding.mystudentdata.helper.InitialDataSource

class StudentRepository(private val studentDao: StudentDao) {

    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> = studentDao.getAllStudentWithCourse()
    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> = studentDao.getAllUniversityAndStudent()
    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> = studentDao.getAllStudentAndUniversity()
    fun getAllStudent(): LiveData<List<Student>> = studentDao.getAllStudent()

//    suspend fun insertAllData() {
//        studentDao.insertStudent(InitialDataSource.getStudents())
//        studentDao.insertUniversity(InitialDataSource.getUniversities())
//        studentDao.insertCourse(InitialDataSource.getCourses())
//        studentDao.insertCourseStudentCrossRef(InitialDataSource.getCourseStudentRelation())
//    }
}