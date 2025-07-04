import { useState } from "react";
import '../style/form.css';
import { toast } from "react-hot-toast";
import { useAuth } from "../context/AuthContext";

const Signup = () => {

    const { signup, loading } = useAuth();

    const [formData, setFormData] = useState({
        username: "",
        email: "",
        password: "",
        confirm_password: "",
        profile_image: null
    });

    const [localLoading, setLocalLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value, type, files } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: type === "file" ? files[0] : value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (formData.password !== formData.confirm_password) {
            toast.error("Passwords do not match");
            return;
        }

        const data = new FormData();
        data.append("username", formData.username);
        data.append("email", formData.email);
        data.append("password", formData.password);
        if (formData.profile_image) {
            data.append("profile_image", formData.profile_image);
        }

        setLocalLoading(true);
        const success = await signup(data);
        setLocalLoading(false);

        if (success) {
            toast.success("Signup successful!");
        } else {
            toast.error("Signup failed. Try again.");
        }

    };

    return (
        <div className="form-wrapper">
            <div className="form-card">
                <h1 className="form-title">Sign Up</h1>

                <form onSubmit={handleSubmit} encType="multipart/form-data" className="form-grid">
                    <div className="form-group full">
                        <label htmlFor="username">Username</label>
                        <input
                            type="text"
                            name="username"
                            id="username"
                            value={formData.username}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group full">
                        <label htmlFor="email">Email</label>
                        <input
                            type="email"
                            name="email"
                            id="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            name="password"
                            id="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="confirm_password">Confirm Password</label>
                        <input
                            type="password"
                            name="confirm_password"
                            id="confirm_password"
                            value={formData.confirm_password}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group full">
                        <label htmlFor="profile_image">Profile Image</label>
                        <input
                            type="file"
                            name="profile_image"
                            id="profile_image"
                            accept="image/*"
                            onChange={handleChange}
                        />
                    </div>

                    <div className="form-group full">
                        <button type="submit" disabled={localLoading || loading}>
                            {localLoading || loading ? <span className="spinner"></span> : "Sign Up"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default Signup;
