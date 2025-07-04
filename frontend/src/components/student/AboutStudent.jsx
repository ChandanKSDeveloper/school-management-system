import '../style/about.css';
import { useEffect, useState } from 'react';
import { useNavigate, Link, useParams } from 'react-router-dom';
import { useStudent } from '../context/StudentContext';
import toast from 'react-hot-toast';
import { FaEdit, FaUser } from 'react-icons/fa';

const AboutStudent = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { getStudentById } = useStudent();
  const [student, setStudent] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchStudent = async () => {
      try {
        setLoading(true);
        const response = await getStudentById(id);

        if (response?.data) {
          setStudent(response.data);
        } else {
          setError("Student not found");
          toast.error("Student with this id not found");
          navigate('/student/dashboard', { replace: true });
        }
      } catch (err) {
        setError("Something went wrong");
        toast.error("Failed to fetch student details");
        console.error("Fetch student error:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchStudent();
  }, [id, getStudentById, navigate]);

  if (loading) {
    return <div className="about-wrapper">Loading student details...</div>;
  }

  if (error || !student) {
    return <div className="about-wrapper">{error || "Student not found"}</div>;

  }


  return (
    <div className="about-wrapper">
      <div className="about-card">
        <div className="about-left">
          {teacher.photo ? (
            <img 
              src={student.photo} 
              alt="student" 
              className="about-photo"
              onError={(e) => {
                e.target.onerror = null;
                e.target.style.display = 'none';
              }}
            />
          ) : (
            <div className="user-icon-wrapper">
              <FaUser className="user-icon" />
            </div>
          )}
        </div>
        <div className="about-right">
          <h2>{student.first_name} {student.last_name}</h2>
          <p><strong>ID:</strong> {student.id}</p>
          <p><strong>Classroom:</strong> {student.classroom}</p>
          <p><strong>Date of Birth:</strong> {student.dob ? new Date(student.dob).toLocaleDateString() : 'N/A'}</p>
          <p><strong>Father Name:</strong> {student.father_name || 'N/A'}</p>
          <p><strong>Email:</strong> {student.email || 'N/A'}</p>
          <p><strong>Mobile No:</strong> {student.mobile_no || 'N/A'}</p>
          <p><strong>Gender:</strong> {student.gender || 'N/A'}</p>
          <p><strong>District:</strong> {student.district || 'N/A'}</p>
          <p><strong>City:</strong> {student.city || 'N/A'}</p>
          <p><strong>State:</strong> {student.state || 'N/A'}</p>
          <p><strong>Nationality:</strong> {student.nationality || 'N/A'}</p>
        </div>
        <FaEdit
          className="edit-icon"
          onClick={() => navigate(`/student/update/${student.id}`)}
          title="Edit Student"
        />
      </div>
    </div>
  )
};



export default AboutStudent;
