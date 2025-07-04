import '../style/adminDashboard.css';
import { useEffect, useState } from 'react';
import { toast } from 'react-hot-toast';
import { FiEdit3 } from 'react-icons/fi';
import { useAdmin } from '../context/AdminContext';
const AdminDashboard = () => {
    const {getAdminDetails, updateProfileImage, changePassword } = useAdmin();
    const [adminInfo, setAdminInfo] = useState(null);
  const [image, setImage] = useState(null);
  const [passwords, setPasswords] = useState({ oldPassword: '', newPassword: '' });



   const fetchAdmin = async () => {
    try {
        const data = await getAdminDetails();
        console.log(data);
        
        setAdminInfo(data);
    } catch (error) {
         toast.error("Failed to load admin info");
    }
   };

  useEffect(() => {
   fetchAdmin();
  }, []);

  const handleImageUpload = async (e) => {
    const file = e.target.files[0];
    if(!file) return;

    const formData = new FormData;
    formData.append("profile_image", file);
    const result= await updateProfileImage(formData);

    if(result?.secure_url) {
        setAdminInfo((prev) => ({...prev, profile_image : result.secure_url}));
    }

    fetchAdmin();

  };

 
  const handlePasswordChange = async (e) => {
      e.preventDefault();

    const response = await changePassword(passwords);
    console.log(response);
    console.log(response.success);
    if(response.success){
        setPasswords({oldPassword : "", newPassword: ""});

      
    }
  };

  return (
    <div className="dashboard-wrapper">
      <h1 className="dashboard-title">Admin Dashboard</h1>

      <div className="admin-profile-card">
        <div className="admin-image-section">
          <img
            src={adminInfo?.profile_image || '/default-profile.png'}
            alt="Admin"
            className="admin-profile-img"
          />
          <label htmlFor="upload-image" className="edit-image-icon">
            <FiEdit3 />
          </label>
          <input
            id="upload-image"
            type="file"
            style={{ display: 'none' }}
            accept="image/*"
            onChange={handleImageUpload}
          />
        </div>

        <div className="admin-details-section">
          <p><strong>Username:</strong> {adminInfo?.username}</p>
          <p><strong>Email:</strong> {adminInfo?.email}</p>
          <p><strong>Role:</strong> Admin</p>
        </div>
      </div>

      <div className="change-password-section">
        <h2>Change Password</h2>
        <form onSubmit={handlePasswordChange} className="password-form">
          <input
            type="password"
            placeholder="Old Password"
            value={passwords.oldPassword}
            onChange={(e) => setPasswords({ ...passwords, oldPassword: e.target.value })}
            required
          />
          <input
            type="password"
            placeholder="New Password"
            value={passwords.newPassword}
            onChange={(e) => setPasswords({ ...passwords, newPassword: e.target.value })}
            required
          />
          <button type="submit">Change Password</button>
        </form>
      </div>
    </div>
  );
};

export default AdminDashboard;
