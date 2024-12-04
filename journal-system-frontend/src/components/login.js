import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../App.css'; // Import App.css to access the styles

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      // Send the login request to the backend
      const response = await axios.post(
        'http://localhost:8080/api/login', // Adjusted the endpoint
        { username, password },
        {
          withCredentials: true, // Ensure cookies are sent with the request
        }
      );

      if (response.status === 200) {
        const { role, message, username, id} = response.data; // Destructure the response
        localStorage.setItem('role', role);
        localStorage.setItem('username', username); // You can also use sessionStorage here if you prefer
        localStorage.setItem('userId', id);

        setMessage(message); // Display the response message

        // Redirect based on the user's role
        if (role === 'PATIENT') {
          navigate('/patient-details');
        } else if (role === 'DOCTOR') {
          navigate('/staff-dashboard');
        } else if (role === 'STAFF') {
          navigate('/staff-dashboard');
        } else {
          navigate('/'); // Fallback in case of an unknown role
        }
      }
    } catch (error) {
      // Handle errors in the login process
      if (error.response) {
        setMessage(
          error.response.status === 401
            ? 'Invalid credentials'
            : `Unexpected error: ${error.response.status}`
        );
      } else {
        setMessage('An error occurred. Please check the network or server status.');
      }
    }
  };

  const navigateToRegister = () => {
    navigate('/register'); // Navigate to the registration page if the user doesn't have an account
  };

  return (
    <div className="box">
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <div className="inputBox">
          <input
            type="text"
            name="username"
            required
            value={username}
            onChange={(e) => setUsername(e.target.value)} // Handle username change
          />
          <label htmlFor="username">Username</label>
        </div>
        <div className="inputBox">
          <input
            type="password"
            name="password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)} // Handle password change
          />
          <label htmlFor="password">Password</label>
        </div>
        <input type="submit" name="submit" value="Submit" />
      </form>
      {message && <p className="errorMessage">{message}</p>} {/* Display error or success message */}
      <p className="registerPrompt">
        Don't have an account?{' '}
        <span className="registerLink" onClick={navigateToRegister}>
          Register here
        </span>
      </p>
    </div>
  );
};
export default Login;