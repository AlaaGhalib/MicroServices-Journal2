import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './StaffDashboard.css';

const Message = () => {
  const [practitioners, setPractitioners] = useState([]);
  const [error, setError] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedPractitioner, setSelectedPractitioner] = useState(null);
  const [message, setMessage] = useState('');

  useEffect(() => {
    const fetchPractitioners = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/practitioners');
        setPractitioners(response.data);
        setError('');
      } catch (err) {
        setError('Failed to load practitioners. Please try again later.');
      }
    };

    fetchPractitioners();
  }, []); // The hook will always run on mount

  const handleMessageClick = (practitioner) => {
    setSelectedPractitioner(practitioner);
    setIsModalOpen(true); 
  };

  const handleSendMessage = async () => {
    try {
      const senderId = localStorage.getItem('userId');  
      await axios.post(
        `http://localhost:8080/api/practitioners/${selectedPractitioner.id}/message?senderId=${senderId}`,
        { messageContent: message }
      );

      alert('Message sent successfully!');
      setIsModalOpen(false); 
      setMessage(''); 
    } catch (err) {
      alert('Failed to send message. Please try again.');
    }
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setMessage('');
  };

  return (
    <div className="dashboard-container">
      <h1>Practitioners</h1>
      {error && <p className="error">{error}</p>}
      <div className="patient-list">
        {practitioners.length > 0 ? (
          practitioners.map((practitioner) => (
            <div key={practitioner.id} className="patient-item">
              {/* Display name and role */}
              <div className="practitioner-info">
                <span className="patient-name">{practitioner.name}</span>
                <span className="patient-role">{practitioner.role}</span>
              </div>

              {/* Message button */}
              <button
                className="message-button"
                onClick={() => handleMessageClick(practitioner)}
              >
                Message
              </button>
            </div>
          ))
        ) : (
          <p>No practitioners found.</p>
        )}
      </div>

      {/* Modal for sending a message */}
      {isModalOpen && (
        <div className="modal">
          <div className="modal-content">
            <h2>Send Message to {selectedPractitioner.name}</h2>
            <textarea
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              placeholder="Type your message here..."
              rows="5"
            />
            <div className="modal-actions">
              <button className="send-button" onClick={handleSendMessage}>
                Send
              </button>
              <button className="cancel-button" onClick={closeModal}>
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Message;