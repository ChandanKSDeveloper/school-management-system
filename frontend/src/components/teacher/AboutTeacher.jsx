import '../style/about.css';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useTeacher } from '../context/TeacherContext';
import toast from 'react-hot-toast';
import { FaEdit, FaUser } from 'react-icons/fa';

const AboutTeacher = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { getTeacherById } = useTeacher();
  const [teacher, setTeacher] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTeacher = async () => {
      try {
        setLoading(true);
        const response = await getTeacherById(id);

        if (response?.data) {
          setTeacher(response.data);
        } else {
          setError("teacher not found");
          toast.error("teacher with this id not found");
          navigate('/teacher/dashboard', { replace: true });
        }
      } catch (err) {
        setError("Something went wrong");
        toast.error("Failed to fetch teacher details");
        console.error("Fetch teacher error:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchTeacher();
  }, [id, getTeacherById, navigate]);

  if (loading) {
    return <div className="about-wrapper">Loading teacher details...</div>;
  }

  if (error || !teacher) {
    return <div className="about-wrapper">{error || "teacher not found"}</div>;

  }


  return (
    <div className="about-wrapper">
      <div className="about-card">
        <div className="about-left">
          {teacher.photo ? (
            <img 
              src={teacher.photo} 
              alt="teacher" 
              className="about-photo"
              onError={(e) => {
                e.target.onerror = null;
                e.target.style.display = 'none';
              }}
            />
          ) : (
            <div className="user-icon-wrapper">
              <FaUser className="user-icon" size={75}/>
            </div>
          )}
        </div>
        <div className="about-right">
          <h2>{teacher.first_name} {teacher.last_name}</h2>
          <p><strong>ID:</strong> {teacher.id}</p>
          <p><strong>Classroom:</strong> {teacher.classroom}</p>
          <p><strong>Date of Birth:</strong> {teacher.dob ? new Date(teacher.dob).toLocaleDateString() : 'N/A'}</p>
          <p><strong>Subject:</strong> {teacher.subject || 'N/A'}</p>
          <p><strong>Email:</strong> {teacher.email || 'N/A'}</p>
          <p><strong>Mobile No:</strong> {teacher.mobile_no || 'N/A'}</p>
          <p><strong>Gender:</strong> {teacher.gender || 'N/A'}</p>
          <p><strong>District:</strong> {teacher.district || 'N/A'}</p>
          <p><strong>City:</strong> {teacher.city || 'N/A'}</p>
          <p><strong>State:</strong> {teacher.state || 'N/A'}</p>
          <p><strong>Nationality:</strong> {teacher.nationality || 'N/A'}</p>
        </div>
        <FaEdit
          className="edit-icon"
          onClick={() => navigate(`/teacher/update/${teacher.id}`)}
          title="Edit teacher"
        />
      </div>
    </div>
  )
};



export default AboutTeacher;
