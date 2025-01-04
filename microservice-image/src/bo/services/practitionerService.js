const axios = require('axios');

const PRACTITIONER_SERVICE_URL = process.env.PRACTITIONER_SERVICE_URL || 'http://localhost:8083/api/user';

exports.getPractitionerByUsername = async (username) => {
    try {
        const response = await axios.get(`${PRACTITIONER_SERVICE_URL}/by-username/${username}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching practitioner by username:', error.message);
        throw new Error('Failed to fetch practitioner');
    }
};
