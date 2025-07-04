import '../style/dashboard.css';
import { useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useStudent } from '../context/StudentContext';
import toast from 'react-hot-toast';

const StudentDashboard = () => {
  const navigate = useNavigate();
  const { students, fetchStudents, deleteStudent } = useStudent();

  useEffect(() => {
    fetchStudents();
  }, []);

  const handleDelete = async (e, id) => {
    e.stopPropagation();
    const confirmDelete = window.confirm("Are you sure you want to delete this student record?");
    if (confirmDelete) {
      const success = await deleteStudent(id);
      if (success) {
        toast.success("Student deleted successfully");
      } else {
        toast.error("Failed to delete student");
      }
    }
  }

  // Assuming you already fixed fetchStudents to store students array directly (not inside `.data`)
  const studentArray = students?.data || [];

  const headers = studentArray.length > 0 ? Object.keys(studentArray[0]) : [];

  // Remove keys you don't want as columns
  // const visibleHeaders = headers.filter(key => key !== "imageId"); // example: removing imageId if exists
  const formatDate = (dateStr) => {
    if (!dateStr) return "";
    const date = new Date(dateStr);
    return `${String(date.getDate()).padStart(2, "0")}-${String(date.getMonth() + 1).padStart(2, "0")}-${date.getFullYear()}`;
  };

  return (
    <div className="dashboard-wrapper">
      <div className="dashboard-header">
        <h1 className="dashboard-title">Student Dashboard</h1>
        <Link to="/student/add">
          <button className="add-button">+ Add Student</button>
        </Link>
      </div>

      <div className="table-container">
        <table className="student-table">
          <thead>
            <tr>
              <th style={{ textAlign: 'left', marginRight : "2.5rem" }}>
                ID
              </th>
              <th style={{ textAlign: 'left' , marginRight : "2.9rem"}}>
                PHOTO
              </th>
              <th style={{ textAlign: 'left' , marginRight : "2.5rem"}}>
                NAME
              </th>
              <th style={{ textAlign: 'left', marginRight : "2.5rem" }}>
                ACTIONS
              </th>
            </tr>
          </thead>
          <tbody>
            {studentArray.map((student) => (

              <tr key={student.id} className='clickable-row' onClick={() => navigate(`/student/profile/${student.id}`)}>
                <td>{student.id}</td>
                <td><img src={student.photo} alt="student" width="75" height="75" style={{ objectFit: "cover", borderRadius: "50%" }} /></td>
                <td>{student.first_name} {student.last_name}</td>
                <td onClick={(e) => e.stopPropagation()}> 
                  <Link to={`/student/update/${student.id}`}><button className='edit-button'>Edit</button></Link>
                  <button onClick={(e) => handleDelete(e, student.id)} className="delete-button">Delete</button>
                </td>

              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Make whole row clickable via CSS */}
      <style>
        {`
          .clickable-row {
            cursor: pointer;
          }
          .clickable-row:hover {
            background-color: #f0f0f0;
          }

          tr{
            display: flex;
            justify-content: space-between;
          }
        `}
      </style>

      {/* Row click navigation */}
    </div>
  );
};

export default StudentDashboard;
