
import './App.css'

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Login, Signup, Home } from "./components/index.js"
// dashboard
import { StudentDashboard, TeacherDashBoard } from "./components/dashboard/dashboardIndex.js"

import { AuthProvider, StudentProvider, TeacherProvider } from './components/context/contextIndex.js';
import { AddStudentForm, UpdateStudentForm, AboutStudent } from "./components/student/studentIndex.js"
import { AddTeacherForm, UpdateTeacherForm, AboutTeacher } from "./components/teacher/teacherIndex.js"
function App() {



  return (

    <Router>
      <AuthProvider>
        <TeacherProvider>

          <StudentProvider>

            {/* <Toaster /> */}
            <Routes>

              <Route path='/login' element={<Login />} />
              <Route path='/home' element={<Home />} />
              <Route path='/' element={<Home />} />
              <Route path='/signup' element={<Signup />} />

              {/* dashboard */}
              <Route path='/student/dashboard' element={<StudentDashboard />} />
              <Route path='/teacher/dashboard' element={<TeacherDashBoard />} />

              {/* student routes */}
              <Route path='/student/add' element={<AddStudentForm />} />
              <Route path="/student/update/:id" element={<UpdateStudentForm />} />
              <Route path='/student/profile/:id' element={<AboutStudent />} />


              {/* Teacher routes */}
              <Route path='/teacher/add' element={<AddTeacherForm />} />
              <Route path="/teacher/update/:id" element={<UpdateTeacherForm />} />
              <Route path='/teacher/profile/:id' element={<AboutTeacher />} />

            </Routes>
          </StudentProvider>
        </TeacherProvider>
      </AuthProvider>
    </Router>

  )
}

export default App
