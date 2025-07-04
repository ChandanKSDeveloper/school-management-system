import { useState, useEffect } from "react";
import '../style/form.css';
import { toast, Toaster } from "react-hot-toast";
import { useTeacher } from "../context/TeacherContext";
import { useParams, useNavigate } from 'react-router-dom';

const UpdateTeacherForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { teachers, updateTeacher } = useTeacher();

  const teacher = teachers?.data?.find((t) => t.id.toString() === id);

  // Format the date for the input field
  const formatDateForInput = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return '';
    return date.toISOString().split('T')[0];
  };

  const [formData, setFormData] = useState({
    gender: teacher?.gender || "Male",
    subject: teacher?.subject || "Math", // Default subject
    ...teacher,
    dob: formatDateForInput(teacher?.dob)
  });

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!teacher) {
      toast.error("Teacher not found");
      navigate('/teacher/dashboard');
    }
  }, [teacher, navigate]);

  const handleChange = (e) => {
    const { name, value, type, files } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "file" ? files[0] : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    const data = new FormData();
    for (let key in formData) {
      if (formData[key] !== null && formData[key] !== undefined) {
        data.append(key, formData[key]);
      }
    }

    const result = await updateTeacher(id, data);
    if (result?.data) {
      toast.success(`${result.data.first_name}'s record updated successfully`);
      navigate("/teacher/dashboard");
    } else {
      toast.error(result?.message || "Failed to update teacher");
    }
    setLoading(false);
  };

  // Common subjects - you can customize this list
  const subjects = [
    "Math", "Science", "English", "History", 
    "Geography", "Physics", "Chemistry", "Biology",
    "Computer Science", "Art", "Music", "Physical Education"
  ];

  return (
    <div className="form-wrapper">
      <Toaster position="top-right" reverseOrder={false} />
      <div className="form-card">
        <h1 className="form-title">Update Teacher</h1>

        <form onSubmit={handleSubmit} encType="multipart/form-data" className="form-grid">
          {["first_name", "last_name", "email", "dob", "mobile_no", "district", "city", "state", "nationality"].map((field) => (
            <div key={field} className="form-group">
              <label htmlFor={field}>{field.replace(/_/g, ' ')}</label>
              <input
                type={field === 'dob' ? 'date' : field === 'email' ? 'email' : 'text'}
                name={field}
                id={field}
                value={formData[field] || ""}
                onChange={handleChange}
              />
            </div>
          ))}

          <div className="form-group">
            <label htmlFor="gender">Gender</label>
            <select name="gender" id="gender" value={formData.gender} onChange={handleChange}>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
              <option value="Other">Other</option>
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="subject">Subject</label>
            <select name="subject" id="subject" value={formData.subject} onChange={handleChange}>
              {subjects.map((subject) => (
                <option key={subject} value={subject}>{subject}</option>
              ))}
            </select>
          </div>

          <div className="form-group full">
            <label htmlFor="photo">New Photo (optional)</label>
            <input type="file" name="photo" id="photo" onChange={handleChange} />
          </div>

          <div className="form-group full">
            <button type="submit" disabled={loading}>
              {loading ? <span className="spinner"></span> : "Update"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default UpdateTeacherForm;