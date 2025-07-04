import '../style/dashboard.css';
import { useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useTeacher } from '../context/TeacherContext';
import { FaUser } from 'react-icons/fa';
import toast from 'react-hot-toast';

const TeacherDashBoard = () => {
    const navigate = useNavigate();
    const { teachers, fetchTeachers, deleteteacher } = useTeacher();

    useEffect(() => {
        fetchTeachers();
    }, []);

    const handleDelete = async (e, id) => {
        e.stopPropagation();
        const confirmDelete = window.confirm("Are you sure you want to delete this Teacher record?");
        if (confirmDelete) {
            const success = await deleteteacher(id);
            if (success) {
                toast.success("teacher deleted successfully");
            } else {
                toast.error("Failed to delete teacher");
            }
        }
    }
    const teacherArray = teachers?.data || [];
    const formatDate = (dateStr) => {
        if (!dateStr) return "";
        const date = new Date(dateStr);
        return `${String(date.getDate()).padStart(2, "0")}-${String(date.getMonth() + 1).padStart(2, "0")}-${date.getFullYear()}`;
    };

    return (
        <div className="dashboard-wrapper">
            <div className="dashboard-header">
                <h1 className="dashboard-title">teacher Dashboard</h1>
                <Link to="/teacher/add">
                    <button className="add-button">+ Add teacher</button>
                </Link>
            </div>

            <div className="table-container">
                <table className="teacher-table">
                    <thead>
                        <tr>
                            <th style={{ textAlign: 'left', marginRight: "2.5rem" }}>
                                ID
                            </th>
                            <th style={{ textAlign: 'left', marginRight: "2.9rem" }}>
                                PHOTO
                            </th>
                            <th style={{ textAlign: 'left', marginRight: "2.5rem" }}>
                                NAME
                            </th>
                            <th style={{ textAlign: 'left', marginRight: "2.5rem" }}>
                                ACTIONS
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        {teacherArray.map((teacher) => (

                            <tr key={teacher.id} className='clickable-row' onClick={() => navigate(`/teacher/profile/${teacher.id}`)}>
                                <td>{teacher.id}</td>
                                <td>
                                    {
                                        teacher.photo ? (<img
                                            src={teacher.photo}
                                            alt="teacher"
                                            width="75"
                                            height="75"
                                            style={{ objectFit: "cover", borderRadius: "50%" }}
                                        />) :

                                        (<FaUser size={40} style={{ margin: '17.5px' }} />)
                                    }
                                </td>
                                <td>{teacher.first_name} {teacher.last_name}</td>
                                <td onClick={(e) => e.stopPropagation()}>
                                    <Link to={`/teacher/update/${teacher.id}`}><button className='edit-button'>Edit</button></Link>
                                    <button onClick={(e) => handleDelete(e, teacher.id)} className="delete-button">Delete</button>
                                </td>

                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {/* Make whole row clickable via CSS */}
            <style>
                {`
          .clickable-row {
            cursor: pointer;
          }
          .clickable-row:hover {
            background-color: #f0f0f0;
          }

          tr{
            display: flex;
            justify-content: space-between;
          }
        `}
            </style>

            {/* Row click navigation */}
        </div>
    );
};

export default TeacherDashBoard;
