import { useNavigate } from "react-router-dom";
import './home.css';
import studentImg from "../../assets/student.jpg"; // Add actual path to image
import teacherImg from "../../assets/teacher.avif"; // Add actual path to image

const Home = () => {
  const navigate = useNavigate();

  return (
    <div className="home-wrapper">
      <h1 className="home-title">Welcome to School Management System</h1>
      <div className="role-container">
        <div className="role-card" onClick={() => navigate("/student/dashboard")}> 
          <img src={studentImg} alt="Student" className="role-image" />
          <h2>Student Panel</h2>
        </div>

        <div className="role-card" onClick={() => navigate("/teacher/dashboard")}> 
          <img src={teacherImg} loading="lazy" alt="Teacher" className="role-image" />
          <h2>Teacher Panel</h2>
        </div>
      </div>
    </div>
  );
};

export default Home;
