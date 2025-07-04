import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from '../context/AuthContext';
import './style.css'
const Login = () => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const { user, login, error, loading } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        // setError(null);
        if (user) {
            navigate('/home');
        }
    }, [user, navigate]);

    const handleSubmit = async (e) => {
        console.log("logging in...");
        
        e.preventDefault();
        console.log("logging in...  haha");
        try {
            await login({ username, password });
        } catch (err) {
            // Error is already handled in AuthContext
            console.error('Login error:', err);
        }
    };

    return (
        <div className="login-wrapper">
            <div className="login-card">
                <div className="login-header">
                    <h1>Login</h1>
                    <p>Enter your username and password to log in.</p>
                </div>

                <form onSubmit={handleSubmit} className="login-form">
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input
                            type="text"
                            name="username"
                            id="username"
                            placeholder="your_username"
                            required
                            value={username}
                            autoFocus
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            name="password"
                            id="password"
                            placeholder="••••••••"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>

                    <button type="submit"  className="login-button">
                        Login
                    </button>
                </form>
            </div>
        </div>
    )
}

export default Login;