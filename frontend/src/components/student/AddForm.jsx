import { useState } from "react";
import '../style/form.css';
import { toast, Toaster } from "react-hot-toast";
import { useStudent } from "../context/StudentContext";
import { useNavigate } from "react-router-dom";

const AddForm = () => {
  const {addStudent , loading, } = useStudent();

  const [formData, setFormData] = useState({
    gender: "Male",
    classroom: "1",
  });

  const navigate = useNavigate();

  // const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value, type, files } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "file" ? files[0] : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const data = new FormData();
    for (let key in formData) {
      data.append(key, formData[key]);
    }

    const result = await addStudent(data);
    if(result){
      toast.success(`${result.data.first_name} added successfully`);
      setTimeout(() => {}, "5000");
      navigate("/student/dashboard");
    } else {
      toast.error(result.message)
    }

    // try {
    //   const res = await fetch("http://localhost:8080/api/student/add", {
    //     method: "POST",
    //     credentials: "include",
    //     body: data,
    //   });

    //   if (res.ok) {
    //     const result = await res.json();
    //     console.log("Student added:", result);
    //     toast.success(`${result.data.first_name} ${result.data.last_name} added successfully!`);
    //   } else {
    //     const error = await res.json();
    //     toast.error("Failed: " + (error.message || "Server error"));
    //   }
    // } catch (err) {
    //   console.error("Upload failed:", err);
    //   toast.error("Upload failed. Check internet or server.");
    //   setLoading(false);
    // }
  };

  return (
    <div className="form-wrapper">
      {/* <Toaster position="top-right" reverseOrder={false} /> */}
      <div className="form-card">
        <h1 className="form-title">Add New Student</h1>

        <form onSubmit={handleSubmit} encType="multipart/form-data" className="form-grid">
          {["first_name", "last_name", "father_name", "email", "dob", "mobile_no", "district", "city", "state", "nationality"].map((field) => (
            <div key={field} className="form-group">
              <label htmlFor={field}>{field.replace(/_/g, ' ')}</label>
              <input type={field === 'dob' ? 'date' : field === 'email' ? 'email' : 'text'} name={field} id={field} onChange={handleChange} required />
            </div>
          ))}

          <div className="form-group">
            <label htmlFor="gender">Gender</label>
            <select name="gender" id="gender" onChange={handleChange} required>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
              <option value="Other">Other</option>
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="classroom">Classroom</label>
            <select name="classroom" id="classroom" onChange={handleChange} required>
              {[...Array(12)].map((_, i) => (
                <option key={i} value={i + 1}>{i + 1}</option>
              ))}
            </select>
          </div>

          <div className="form-group full">
            <label htmlFor="photo">Photo</label>
            <input type="file" name="photo" id="photo" onChange={handleChange} />
          </div>

          <div className="form-group full">
            <button type="submit" disabled={loading}>
              {loading ? <span className="spinner"></span> : "Submit"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export { AddForm };
