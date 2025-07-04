
import './App.css'

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Login, Signup, Home } from "./components/index.js"
// dashboard
import { StudentDashboard, TeacherDashBoard, AdminDashBoard } from "./components/dashboard/dashboardIndex.js"

import { AuthProvider, StudentProvider, TeacherProvider, AdminProvider } from './components/context/contextIndex.js';
import { AddStudentForm, UpdateStudentForm, AboutStudent } from "./components/student/studentIndex.js"
import { AddTeacherForm, UpdateTeacherForm, AboutTeacher } from "./components/teacher/teacherIndex.js";
import { Toaster } from 'react-hot-toast';
import Layout from './components/common/Layout.jsx';

function App() {
  return (
    <Router>
      <AuthProvider>
        <AdminProvider>
          <TeacherProvider>
            <StudentProvider>
              <Toaster
                position='top-right'
                reverseOrder={false}
                toastOptions={{
                  success: {
                    style: {
                      background: '#4caf50',
                      color: 'white',
                    },
                  },
                  error: {
                    style: {
                      background: '#f44336',
                      color: 'white',
                    },
                  },
                }}
              />
              <Routes>
                {/* Public Routes */}
                <Route path='/login' element={<Login />} />
                <Route path='/signup' element={<Signup />} />

                {/* Routes with Navbar */}
                <Route element={<Layout />}>
                  <Route path='/' element={<Home />} />
                  <Route path='/home' element={<Home />} />

                  {/* Dashboard */}
                  <Route path='/student/dashboard' element={<StudentDashboard />} />
                  <Route path='/teacher/dashboard' element={<TeacherDashBoard />} />
                  <Route path='/admin/dashboard' element={<AdminDashBoard />} />

                  {/* Student Routes */}
                  <Route path='/student/add' element={<AddStudentForm />} />
                  <Route path='/student/update/:id' element={<UpdateStudentForm />} />
                  <Route path='/student/profile/:id' element={<AboutStudent />} />

                  {/* Teacher Routes */}
                  <Route path='/teacher/add' element={<AddTeacherForm />} />
                  <Route path='/teacher/update/:id' element={<UpdateTeacherForm />} />
                  <Route path='/teacher/profile/:id' element={<AboutTeacher />} />
                </Route>
              </Routes>
            </StudentProvider>
          </TeacherProvider>
        </AdminProvider>
      </AuthProvider>
    </Router>
  );
}

export default App;

