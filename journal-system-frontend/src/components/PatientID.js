import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../App.css';
import { useParams, useNavigate } from 'react-router-dom';

const PatientDetails = () => {
  const { patientId } = useParams(); // Extract patientId from the route parameter
  const [patientDetails, setPatientDetails] = useState(null);
  const [conditions, setConditions] = useState([]); // State to store conditions for the patient
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [showAddConditionForm, setShowAddConditionForm] = useState(false);
  const [newCondition, setNewCondition] = useState({ diagnosis: '', status: 'ACTIVE' });
  const [editCondition, setEditCondition] = useState(null); // State to hold the condition being edited
  const navigate = useNavigate(); // Use navigate for routing

  // Fetch patient details on component mount
  useEffect(() => {
    const fetchPatientDetails = async () => {
      setLoading(true);
      try {
        const response = await axios.get(`http://localhost:8080/api/patient/details/${patientId}`);
        setPatientDetails(response.data);
        setError('');
      } catch (err) {
        setError('Failed to load patient details. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchPatientDetails();
  }, [patientId]);

  // Fetch conditions for the patient
  useEffect(() => {
    const fetchConditions = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/conditions/show/${patientId}`);
        setConditions(response.data);
        setError('');
      } catch (err) {
        setError('Failed to load conditions. Please try again later.');
      }
    };

    fetchConditions();
  }, [patientId]);

  // Handler to navigate to encounters screen
  const handleViewEncounters = () => {
    navigate(`/patient/${patientId}/encounters`);
  };

  // Handler to add a new condition
  const handleAddCondition = async (e) => {
    e.preventDefault();
    try {
      const conditionDTO = {
        patientId: patientId,
        diagnosis: newCondition.diagnosis,
        status: newCondition.status,
      };

      const response = await axios.post('http://localhost:8080/api/conditions/add', conditionDTO);

      if (response.status === 201) {
        const updatedConditions = await axios.get(`http://localhost:8080/api/conditions/show/${patientId}`);
        setConditions(updatedConditions.data);
        setNewCondition({ diagnosis: '', status: 'ACTIVE' });
        setShowAddConditionForm(false);
        setError('');
      }
    } catch (err) {
      console.error("Error adding condition:", err);
      setError('Failed to add condition. Please try again later.');
    }
  };

  // Handler to start editing a condition
  const handleEditCondition = (condition) => {
    setEditCondition(condition); // Set the current condition to be edited
  };

  // Handler to submit updated condition
  const handleUpdateCondition = async (e) => {
    e.preventDefault(); // Prevent default form submission
    try {
      const conditionDTO = {
        diagnosis: editCondition.diagnosis,
        status: editCondition.status,
      };

      await axios.put(`http://localhost:8080/api/conditions/update/${editCondition.id}`, conditionDTO);

      // Fetch updated conditions to reflect the changes
      const updatedConditions = await axios.get(`http://localhost:8080/api/conditions/show/${patientId}`);
      setConditions(updatedConditions.data);
      setEditCondition(null); // Clear edit state
      setError('');
    } catch (err) {
      console.error("Error updating condition:", err);
      setError('Failed to update condition. Please try again later.');
    }
  };

  return (
    <div className="box">
      <h2>Patient Details</h2>

      {loading && <p>Loading...</p>}
      {error && <p className="error">{error}</p>}

      {patientDetails && (
        <div>
          <h3>Patient Information</h3>
          <p><strong>Username:</strong> {patientDetails.username}</p>
          <p><strong>Name:</strong> {patientDetails.name}</p>
          <p><strong>Address:</strong> {patientDetails.address}</p>
          <p><strong>Phone Number:</strong> {patientDetails.phoneNumber}</p>
          <p><strong>Date of Birth:</strong> {patientDetails.dateOfBirth}</p>
          
          {/* Displaying the Conditions */}
          <h3>Conditions</h3>
          {conditions && conditions.length > 0 ? (
            <ul>
              {conditions.map((condition) => (
                <li key={condition.id}>
                  <p><strong>Diagnosis:</strong> {condition.diagnosis}</p>
                  <p><strong>Status:</strong> {condition.status}</p>
                  <button onClick={() => handleEditCondition(condition)} className="edit-condition-btn">Edit</button>
                </li>
              ))}
            </ul>
          ) : (
            <p>No conditions available for this patient.</p>
          )}

          {/* Button to show the add condition form */}
          <button onClick={() => setShowAddConditionForm(!showAddConditionForm)} className="add-condition-btn">
            {showAddConditionForm ? 'Cancel' : 'Add Condition'}
          </button>

          {/* Form to add a new condition */}
          {showAddConditionForm && (
            <form onSubmit={handleAddCondition} className="add-condition-form">
              <h3>Add New Condition</h3>
              <div>
                <label>Diagnosis:</label>
                <input
                  type="text"
                  value={newCondition.diagnosis}
                  onChange={(e) => setNewCondition({ ...newCondition, diagnosis: e.target.value })}
                  required
                />
              </div>
              <div>
                <label>Status:</label>
                <select
                  value={newCondition.status}
                  onChange={(e) => setNewCondition({ ...newCondition, status: e.target.value })}
                >
                  <option value="ACTIVE">Active</option>
                  <option value="RESOLVED">Resolved</option>
                </select>
              </div>
              <button type="submit" className="submit-condition-btn">Add Condition</button>
            </form>
          )}

          {/* Form to edit a condition */}
          {editCondition && (
            <form onSubmit={handleUpdateCondition} className="edit-condition-form">
              <h3>Edit Condition</h3>
              <div>
                <label>Diagnosis:</label>
                <input
                  type="text"
                  value={editCondition.diagnosis}
                  onChange={(e) => setEditCondition({ ...editCondition, diagnosis: e.target.value })}
                  required
                />
              </div>
              <div>
                <label>Status:</label>
                <select
                  value={editCondition.status}
                  onChange={(e) => setEditCondition({ ...editCondition, status: e.target.value })}
                >
                  <option value="ACTIVE">Active</option>
                  <option value="RESOLVED">Resolved</option>
                </select>
              </div>
              <button type="submit" className="submit-edit-btn">Update Condition</button>
            </form>
          )}

          {/* Button to navigate to encounters screen */}
          <button onClick={handleViewEncounters} className="view-encounters-btn">
            View Encounters
          </button>
        </div>
      )}
    </div>
  );
};

export default PatientDetails;
