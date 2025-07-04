import { Link, useNavigate } from 'react-router-dom';
import { FiUser, FiLogOut } from 'react-icons/fi';
import { useAdmin } from '../context/AdminContext';
import '../style/navbar.css';
import { useAuth } from '../context/AuthContext';


const Navbar = () => {
  const navigate = useNavigate();
  const { isAuthenticated, logout } = useAuth();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link to="/home" className="nav-logo">EduPortal</Link>
      </div>
      <div className="navbar-right">
        {isAuthenticated ? (
          <>
            <Link to="/home" className="nav-link">Home</Link>
            <Link to="/admin/dashboard" className="nav-icon">
              <FiUser />
            </Link>
            <button onClick={handleLogout} className="logout-btn">
              <FiLogOut /> Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/login" className="nav-link">Login</Link>
            <Link to="/signup" className="nav-link">Signup</Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
