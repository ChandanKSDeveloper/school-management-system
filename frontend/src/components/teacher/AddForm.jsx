import { useState } from "react";
import '../common/style.css';
import { toast, Toaster } from "react-hot-toast";
import { useTeacher } from "../context/TeacherContext";


const AddForm = () => {
  const {addTeacher , loading} = useTeacher();

  const [formData, setFormData] = useState({
    gender: "Male",
  });


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

    const result = await addTeacher(data);
    if(result){
      toast.success(`${result.data.first_name} added successfully`);
    }
  };

  return (
    <div className="form-wrapper">
      {/* <Toaster position="top-right" reverseOrder={false} /> */}
      <div className="form-card">
        <h1 className="form-title">Add New Teacher</h1>

        <form onSubmit={handleSubmit} encType="multipart/form-data" className="form-grid">
          {["first_name", "last_name", "email", "dob", "mobile_no", "district", "city", "state", "nationality", "subject"].map((field) => (
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
