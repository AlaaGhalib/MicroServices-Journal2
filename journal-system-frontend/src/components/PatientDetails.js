import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './PatientDetails.css'; // Assuming you're using some basic CSS for styling
import { useNavigate } from 'react-router-dom'; // Import useNavigate from react-router-dom

const PatientDetails = () => {
  const [patientDetails, setPatientDetails] = useState(null); // State to store patient details
  const [error, setError] = useState(null); // State for handling errors
  const [loading, setLoading] = useState(false); // State for loading indicator
  const navigate = useNavigate(); // Initialize useNavigate hook

  useEffect(() => {
    const username = localStorage.getItem('username'); // Retrieve username from localStorage

    if (username) {
      fetchPatientDetails(username); // Fetch patient details if username exists
    } else {
      setError('User not logged in.');
    }
  }, []); // Empty dependency array ensures this effect runs only once after the component mounts

  // Function to fetch patient details from the backend
  const fetchPatientDetails = async (username) => {
    setLoading(true); // Start loading

    try {
      const response = await axios.get('http://localhost:8080/api/patient/details', {
        headers: {
          Username: username, // Add the username to the request headers
        },
      });

      // Update the state with the patient details
      setPatientDetails(response.data);
      setError(null); // Clear any previous error messages
    } catch (err) {
      const errorMessage = err.response?.data?.message || 'An error occurred.'; // Safely extract the message
      setError(errorMessage); // Only set the error message, not the whole object
      setPatientDetails(null); // Clear patient details if there's an error
    } finally {
      setLoading(false); // Stop loading after request finishes
    }
  };

  const handleMessageClick = () => {
    navigate('/message');
  };

  const handleViewMessageClick = () => {
    navigate('/view-message'); // Assuming you want to navigate to a "View Messages" page
  };

  return (
    <div className="box"> {/* Apply the 'box' class here to inherit the styles */}
      <h2>Patient Details</h2>

      {/* Display loading state */}
      {loading && <p>Loading...</p>}

      {/* Display error message */}
      {error && <p className="error">{error}</p>}

      {/* Display patient details */}
      {patientDetails && (
        <div>
          <h3>Patient Information</h3>
          <p><strong>Username:</strong> {patientDetails.username}</p>
          <p><strong>Name:</strong> {patientDetails.name}</p>
          <p><strong>Address:</strong> {patientDetails.address}</p>
          <p><strong>Phone Number:</strong> {patientDetails.phoneNumber}</p>
          <p><strong>Date of Birth:</strong> {patientDetails.dateOfBirth}</p>
        </div>
      )}

      {/* Buttons for sending and viewing messages */}
      <div className="buttons-container2">
        <button className="message-btn2" onClick={handleMessageClick}>
          Send Message!
        </button>
        
        <button className="message-btn2" onClick={handleViewMessageClick}>
          View Message!
        </button>
      </div>
    </div>
  );
};

export default PatientDetails;