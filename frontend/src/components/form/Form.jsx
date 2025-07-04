import { useState } from "react";

const Form = ({ name }) => {
    const [formData, setFormData] = useState({
        gender : "Male",
        classroom : "1",
    });

    const handleChange = (e) => {
        const { name, value, type, files } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: type === "file" ? files[0] : value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const data = new FormData();

        for (let key in formData) {
            data.append(key, formData[key]);
        }

        try {
            const res = await fetch("http://localhost:8080/api/student/add", {
                method: "POST",
                credentials: "include",
                body: data,
            });

            if(res.ok){
                const result = await res.json();
                console.log("Student added:", result);
                alert("Student added successfully!");
            } else{
                console.log("failed to add student");
            }
        } catch (err) {
            console.error("Upload failed:", err);
            alert("Failed to add student");
        }
    };

    return (
        <div>
            <header>
                <h1>Add {name}</h1>
            </header>
            <main>
                <form onSubmit={handleSubmit} encType="multipart/form-data">
                    <label>First Name:</label>
                    <input type="text" name="first_name" onChange={handleChange} required />

                    <label>Last Name:</label>
                    <input type="text" name="last_name" onChange={handleChange} required />

                    <label>Father Name:</label>
                    <input type="text" name="father_name" required onChange={handleChange} />

                    <label>Email:</label>
                    <input type="email" name="email" onChange={handleChange} required />

                    <label>Date of Birth:</label>
                    <input type="date" name="dob" required onChange={handleChange} />

                    <label>Mobile No:</label>
                    <input type="text" name="mobile_no" required onChange={handleChange} />

                    <label>Gender:</label>
                    <select name="gender" required onChange={handleChange}>
                        <option value="Male">Male</option>
                        <option value="Female">Female</option>
                        <option value="Other">Other</option>
                    </select>

                    <label>District:</label>
                    <input type="text" name="district" required onChange={handleChange} />

                    <label>City:</label>
                    <input type="text" name="city" required onChange={handleChange} />

                    <label>State:</label>
                    <input type="text" name="state" required onChange={handleChange} />

                    <label>Nationality:</label>
                    <input type="text" name="nationality" required onChange={handleChange} />

                    <label>Classroom:</label>
                    <select name="classroom" required onChange={handleChange}>
                        {[...Array(12)].map((_, i) => (
                            <option key={i} value={i + 1}>{i + 1}</option>
                        ))}
                    </select>

                    <label>Photo:</label>
                    <input type="file" name="photo" onChange={handleChange} />

                    <button type="submit">Submit</button>
                </form>
            </main>
        </div>
    );
};

export default Form;
