import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './StaffDashboard.css'; // Import the CSS file for styling
import { useNavigate } from 'react-router-dom';

const StaffDashboard = () => {
  const [patients, setPatients] = useState([]);
  const [messages, setMessages] = useState([]); // Store the messages for the user
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchPatients = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/patients');
        setPatients(response.data);
        setError('');
      } catch (err) {
        setError('Failed to load patients. Please try again later.');
      }
    };

    // Fetch messages for the logged-in user
    const fetchMessages = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/messages/recipient', {
          headers: {
            userId: userId,  
          },
        });
        setMessages(response.data);  
      } catch (err) {
        setError('Failed to load messages.');
      }
    };

    fetchPatients();
    fetchMessages();
  }, [userId]);

  const handlePatientClick = (patientId) => {
    navigate(`/patient-details/${patientId}`);
  };

  const handleViewMessageClick = (patientId, e) => {
    e.stopPropagation(); 
    localStorage.setItem("recipientId",patientId);
    navigate(`/view-respond/${patientId}`);
  };

  const hasMessageFromPatient = (patientId) => {
    if (!messages || messages.length === 0) {
      return false; 
    }
    return messages.some((message) => message.sender.id === patientId);  
  };

  return (
    <div className="dashboard-container">
      <h1>Staff Dashboard - List of Patients</h1>
      {error && <p className="error">{error}</p>}
      <div className="patient-list">
        {patients.length > 0 ? (
          patients.map((patient) => (
            <div
              key={patient.id}
              className="patient-item"
              onClick={() => handlePatientClick(patient.id)} 
            >
              <div className="patient-info">
                <span className="patient-name">{patient.username}</span>
                <span className="patient-role">{patient.role}</span>
              </div>

              {/* Conditionally render the "View Message!" button */}
              {hasMessageFromPatient(patient.id) && (
                <button
                  className="message-btn"
                  onClick={(e) => handleViewMessageClick(patient.id, e)}
                >
                  View Message!
                </button>
              )}
            </div>
          ))
        ) : (
          <p>No patients found.</p>
        )}
      </div>
    </div>
  );
};

export default StaffDashboard;