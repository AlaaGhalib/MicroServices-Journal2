import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './Respond.css'; // Import App.css to access the styles

const ViewMessagePatient = () => {
  const [messages, setMessages] = useState([]);
  const [responses, setResponses] = useState({}); // To hold response texts for each message
  const [error, setError] = useState('');

  // Helper function to format LocalDateTime into a readable format
  const formatDateTime = (dateTimeString) => {
    if (!dateTimeString) {
      return 'Invalid date';
    }

    try {
      // Assuming the date format is "yyyy-MM-dd'T'HH:mm:ss" (ISO format)
      const [datePart, timePart] = dateTimeString.split('T');
      const [year, month, day] = datePart.split('-');
      const [hour, minute, second] = timePart.split(':');
      
      // Create a Date object to manipulate the time
      const date = new Date(year, month - 1, day, hour, minute, second);
      
      // Use toLocaleString() to display the date in a readable format
      return date.toLocaleString(); // This will return a human-readable date and time
    } catch (err) {
      console.error('Error formatting date:', err);
      return 'Invalid date format';
    }
  };

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
  }, []);

  const handleResponseChange = (messageId, value) => {
    setResponses((prevResponses) => ({
      ...prevResponses,
      [messageId]: value,
    }));
  };

  const handleResponseSubmit = async (messageId) => {
    const senderId = localStorage.getItem('userId');  // Get the sender ID from localStorage
    const responseContent = responses[messageId];  // Get the response content from the state
  
    // Ensure the senderId, recipientId (messageId), and responseContent are valid
    if (!responseContent || responseContent.trim() === '') {
      alert('Response cannot be empty.');
      return;
    }
  
    if (!senderId) {
      alert('Sender ID is missing.');
      return;
    }
  
    try {
      const recipientId = localStorage.getItem("recipientId");
        await axios.post(
            `http://localhost:8080/api/practitioners/${recipientId}/message?senderId=${senderId}`,
        { responseContent }
      );
  
      alert('Response sent successfully!');
      setResponses((prevResponses) => ({
        ...prevResponses,
        [messageId]: '',  // Clear the response field after successful submission
      }));
    } catch (err) {
      console.error(err);  // Log the error for better debugging
      alert('Failed to send the response.');
    }
  };

  return (
    <div className="box">
      <h2>View Messages</h2>
      {error && <p className="error">{error}</p>}
      <div>
        {messages.length > 0 ? (
          messages.map((message) => (
            <div key={message.id} className="message-box">
              {/* Removed Sender Name */}
              <p><strong>Message:</strong> {message.content}</p>
              <p><strong>Date:</strong> {formatDateTime(message.sentAt)}</p>  {/* Corrected field name */}
  
              {/* Response Form */}
              <div className="response-form">
                <textarea
                  placeholder="Type your response here..."
                  value={responses[message.id] || ''}
                  onChange={(e) => handleResponseChange(message.id, e.target.value)}
                  rows="3"
                />
                <button onClick={() => handleResponseSubmit(message.id)}>Send Response</button>
              </div>
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