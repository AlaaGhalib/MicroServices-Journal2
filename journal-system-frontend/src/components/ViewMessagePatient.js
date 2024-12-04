import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ViewMessagePatient = () => {
  const [messages, setMessages] = useState([]);
  const [error, setError] = useState('');
  useEffect(() => {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      setError('User is not logged in.');
      return;
    }

    const fetchMessages = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/messages/recipient', {
          headers: {
            userId: userId, 
          },
        });
        setMessages(response.data); 
      } catch (err) {
        setError('Failed to fetch messages.');
      }
    };
    fetchMessages();
  }, []); // Empty dependency array ensures this effect runs once when the component mounts

  return (
    <div className="box">
      <h2>View Messages</h2>
      {error && <p className="error">{error}</p>}
      <div>
        {messages.length > 0 ? (
          messages.map((message, index) => (
            <div key={index}>
              <p><strong>Sender:</strong> {message.senderName}</p>
              <p><strong>Message:</strong> {message.content}</p>
              <p><strong>Date:</strong> {message.date}</p>
              <hr />
            </div>
          ))
        ) : (
          <p>No messages found.</p>
        )}
      </div>
    </div>
  );
};

export default ViewMessagePatient;