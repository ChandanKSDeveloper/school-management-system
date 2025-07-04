import { createContext, useContext, useState, useEffect } from "react";
const TeacherContext = createContext();

export const TeacherProvider = ({ children }) => {

    const BASE_URL = "http://localhost:8080/api/teacher";

    const [teachers, setTeachers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // fetch all teachers details
    const fetchTeachers = async () => {
        setLoading(true);

        try {
            const response = await fetch(`${BASE_URL}/getTeacher`, {
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error("failed to fetch teachers")
            }

            if (response.ok) {
                const data = await response.json();
                console.log(data);

                setTeachers(data);
                // toast.success("teacher data fetched successfully");
            }
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }

    }

    const addTeacher = async (formData) => {
        setLoading(true);
        try {
            const res = await fetch(`${BASE_URL}/add`, {
                method: "POST",
                credentials: "include",
                body: formData
            });
            const result = await res.json();
            if(!res.ok){
                throw new Error(result.message);
                // return null;
            }

            await fetchTeachers();
            return result;
        } catch (err) {
           toast.error(error.message || "Add student failed");
            console.error("Add student failed:", err);
            return null;
        }
    }

     const updateTeacher = async (id,formData) => {
        setLoading(true);
        try {
            const res = await fetch(`${BASE_URL}/update?id=${id}`, {
                method: "PUT",
                credentials: "include",
                body: formData 
            });

           const result = await res.json();
            if(!res.ok){
                throw new Error(result.message);
                // return null;
            }

            await fetchTeachers();
            return result;
        } catch (error) {
            console.error("Add teacher failed:", error);
            return null
        } finally {
            setLoading(false);
        }
    }

    const getTeacherById = async (id) => {
        try {
            const response = await fetch(`${BASE_URL}/getTeacher/${id}`, {
                credentials : "include",
                
            })

            return response.ok ? await response.json() : null;

        } catch (error) {
            console.error("Add teacher failed:", error);
            return null
        }
    }

    const deleteTeacher = async (id) => {
        try {
            const res = await fetch(`${BASE_URL}/delete?id=${id}`, {
                method: "DELETE",
                credentials: "include",
            });

            if (!res.ok) throw new Error("Delete failed");
            await fetchStudents();
            console.log("Teacher with id : " + id + " has been removed.");
            
            return true;
        } catch (error) {
            console.error("Delete failed:", error);
            return false;
        }
    }

    useEffect(() => {
        fetchTeachers();
    }, []);

    return(
        <TeacherContext.Provider
            value={{
                teachers, 
                loading,
                error,
                fetchTeachers,
                addTeacher,
                getTeacherById,
                updateTeacher,
                deleteTeacher,
            }}
        >
            {children}
        </TeacherContext.Provider>
    )
}

export const useTeacher = () => useContext(TeacherContext);