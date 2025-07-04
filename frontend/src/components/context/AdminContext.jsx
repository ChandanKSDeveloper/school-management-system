import { createContext, useContext, useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { toast } from 'react-hot-toast';
import { useAuth } from "./AuthContext";

const AdminContext = createContext({
     admin: null,
     loading: true,
     error: null,
     isAuthenticated: false,
});

export default function AdminProvider({ children }) {

     const BASE_URL = "http://localhost:8080/api/admin";

     const [admin, setAdmin] = useState(null);
     const [loading, setLoading] = useState(true);
     const [error, setError] = useState(null);
     const { login } = useAuth();

     const changePassword = async (formData) => {
          setLoading(true);
          try {
               const response = await fetch(`${BASE_URL}/change-password`, {
                    method: "POST",
                    credentials: "include",
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(formData)

               });

               const result = await response.json();
               if (response.ok) {
                    toast.success(result.message || "Password changed successfully");
                      return { success: true, message: result.message };
                    // setPasswords({ oldPassword: '', newPassword: '' });
               } else {
                    toast.error(result.message || "Password change failed");
                    return { success: false, message: result.message };
               }

               
          } catch (error) {

               console.error("password change failed:", error);
               toast.error(error.message || "Password change failed");
               return { success: false, message: result.message };
          } finally {
               setLoading(false)
          }
     }



     const updateProfileImage = async (image) => {
          setLoading(true);
          try {
               const res = await fetch(`${BASE_URL}/update-profile-img`, {
                    method: "POST",
                    credentials: "include",
                    body: image
               });

               const result = await res.json();
               if (!res.ok) {
                    throw new Error(result.message);
                    // return null;
               }

               setAdmin((prev) => ({...prev, profile_image: result.secure_url}))
               await getAdminDetails();
               return result;
          } catch (error) {
               toast.error(error.message || "Add student failed");
               console.error("Add student failed:", error);
               return null
          } finally {
               setLoading(false)
          }
     }

     const getAdminDetails = async () => {
          if (login === null) return;

          const res = await fetch(`${BASE_URL}/admin-detail`, {
               credentials: "include"
          })

          const result = await res.json();
          console.log(result);
          if (!res.ok) {
               // throw new Error(result.message);
               return null;
          }

          setAdmin(result);
          return result;
     }


     useEffect(() => {
          getAdminDetails();
     }, []);

     return (
          <AdminContext.Provider
               value={{
                    admin,
                    getAdminDetails,
                    changePassword,
                    updateProfileImage,
               }}
          >
               {children}
          </AdminContext.Provider>
     )


}

export const useAdmin = () => useContext(AdminContext);