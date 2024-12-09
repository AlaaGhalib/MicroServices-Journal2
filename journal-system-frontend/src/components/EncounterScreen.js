import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import '../App.css';

const EncountersScreen = () => {
  const { patientId } = useParams(); // Extract patientId from route params
  const [encounters, setEncounters] = useState([]); // State to store all encounters
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [newEncounterReason, setNewEncounterReason] = useState(''); // State for new encounter reason
  const [newEncounterNotes, setNewEncounterNotes] = useState(''); // State for new encounter notes

  // Fetch all encounters when the component mounts
  useEffect(() => {
    const fetchEncounters = async () => {
      setLoading(true);
      try {
        console.log('Fetching all encounters for patient:', patientId);
        const response = await axios.get(`http://localhost:8080/api/encounters/patient/${patientId}`);
        console.log('Fetched encounters:', response.data);
        setEncounters(Array.isArray(response.data) ? response.data : []); // Ensure response is an array
        setError('');
      } catch (err) {
        console.error('Error fetching encounters:', err);
        setError('Failed to load encounters. Please try again later.');
        setEncounters([]); // Reset encounters in case of an error
      } finally {
        setLoading(false);
      }
    };

    fetchEncounters();
  }, [patientId]); // Run this effect when patientId changes

  // Function to handle adding a new encounter
  const handleAddEncounter = async () => {
    if (!newEncounterReason) {
      alert('Please enter a reason for the new encounter.');
      return;
    }

    setLoading(true);
    try {
      console.log('Adding new encounter for patient:', patientId);
      const response = await axios.post(`http://localhost:8080/api/encounters/add`, {
        patientId: patientId,
        reason: newEncounterReason,
        notes: newEncounterNotes, // Adding notes field here
      });

      if (response.status === 201) {
        // Update the state with the new encounter
        console.log('New encounter added:', response.data);
        setEncounters((prevEncounters) => [...prevEncounters, response.data]);
        setNewEncounterReason(''); // Clear input field for reason
        setNewEncounterNotes(''); // Clear input field for notes
        setError('');
        alert('Encounter added successfully!');
      }
    } catch (error) {
      console.error('Error adding new encounter:', error);
      setError('Failed to add new encounter. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="box">
      <h2>Patient Encounters</h2>

      {loading && <p>Loading...</p>}
      {error && <p className="error">{error}</p>}

      {/* Form to add a new encounter */}
      <div className="add-encounter">
        <h3>Add New Encounter</h3>
        <input
          type="text"
          value={newEncounterReason}
          onChange={(e) => setNewEncounterReason(e.target.value)}
          placeholder="Enter reason for the new encounter"
        />
        <textarea
          value={newEncounterNotes}
          onChange={(e) => setNewEncounterNotes(e.target.value)}
          placeholder="Enter notes for the new encounter"
        />
        <button onClick={handleAddEncounter}>Add Encounter</button>
      </div>

      {/* Display all encounters */}
      <div className="encounters-list">
        <h3>All Encounters</h3>
        {Array.isArray(encounters) && encounters.length > 0 ? (
          <ul>
            {encounters.map((encounter) => (
              <li key={encounter.id} style={{ marginBottom: '10px' }}>
                <p><strong>Date:</strong> {new Date(encounter.dateTime).toLocaleString()}</p>
                <p><strong>Reason:</strong> {encounter.reason}</p>
                <p><strong>Notes:</strong> {encounter.notes}</p>
              </li>
            ))}
          </ul>
        ) : (
          <p>No encounters found for this patient.</p>
        )}
      </div>
    </div>
  );
};

export default EncountersScreen;
