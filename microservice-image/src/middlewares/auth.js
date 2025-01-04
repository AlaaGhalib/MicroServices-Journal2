const jwt = require('jsonwebtoken');

const jwtMiddleware = (req, res, next) => {
    const authHeader = req.headers['authorization'];

    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        console.log("No token provided");
        return res.status(401).json({ error: 'Unauthorized: No token provided' });
    }

    const token = authHeader.split(' ')[1];
    const SECRET_KEY = 'TVlfU1VQRVJfU0VDVVJFX1NFQ1JFVF9LRVlfV0lUSF9BVF9MRUFTVF8yNTZfQklUUw=='; // Base64-encoded key

    console.log("Received Token:", token);

    try {
        const decoded = jwt.verify(token, SECRET_KEY, { algorithms: ['HS256'] }); // Explicit HS256
        console.log("Decoded Token:", decoded);
        req.user = decoded;
        next();
    } catch (err) {
        console.error("JWT Verification Error:", err.message);
        return res.status(403).json({ error: 'Forbidden: Invalid or expired token' });
    }
};

const authenticateDoctor = (req, res, next) => {
    jwtMiddleware(req, res, () => {
        if (req.user && req.user.role === 'DOCTOR') {
            next(); // Fortsätt om rollen är DOCTOR
        } else {
            return res.status(403).json({ error: 'Access denied. Only doctors can edit images.' });
        }
    });
};

module.exports = { jwtMiddleware, authenticateDoctor };
