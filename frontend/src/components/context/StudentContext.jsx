import { createContext, useContext, useState, useEffect } from "react";
const StudentContext = createContext();

export const StudentProvider = ({ children }) => {

    const BASE_URL = "http://localhost:8080/api/student";

    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // fetch all students details
    const fetchStudents = async () => {
        setLoading(true);

        try {
            const response = await fetch(`${BASE_URL}/getStudent`, {
                credentials: 'include',
            });

            if (!response.ok) {
                throw new Error("failed to fetch students")
            }


            const data = await response.json();
            console.log(data);

            setStudents(data);
            // toast.success("student data fetched successfully");

        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    }

    const addStudent = async (formData) => {
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

            await fetchStudents();
            return result;
        } catch (err) {
             toast.error(error.message || "Add student failed");
            console.error("Add student failed:", err);
            return null
        }
    }

    const updateStudent = async (id, formData) => {
        console.log("id : ", id);
        console.log("formData : ", formData);
        
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

            await fetchStudents();
            return result;
        } catch (error) {
            console.error("Add student failed:", error);
            return null
        } finally {
            setLoading(false);
        }
    }

    const getStudentById = async (id) => {
        try {
            const response = await fetch(`${BASE_URL}/getStudent/${id}`, {
                credentials: "include",
            })

            return response.ok ? await response.json() : null;

        } catch (error) {
            console.error("Add student failed:", error);
            return null;
        }
    }

    const deleteStudent = async (id) => {
        try {
            const res = await fetch(`${BASE_URL}/delete?id=${id}`, {
                method: "DELETE",
                credentials: "include",
            });

            if (!res.ok) throw new Error("Delete failed");
            await fetchStudents();
            console.log("Student with id : " + id + " has been removed.");
            
            return true;
        } catch (error) {
            console.error("Delete failed:", error);
            return false;
        }
    }

    useEffect(() => {
        fetchStudents();
    }, []);

    return (
        <StudentContext.Provider
            value={{
                students,
                loading,
                error,
                fetchStudents,
                addStudent,
                getStudentById,
                updateStudent,
                deleteStudent,
            }}
        >
            {children}
        </StudentContext.Provider>
    )
}

export const useStudent = () => useContext(StudentContext);