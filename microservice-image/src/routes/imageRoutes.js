const express = require('express');
const router = express.Router();
const imageController = require('../controller/imageController');

// Routes
router.post('/upload', imageController.uploadImage);
router.put('/edit', imageController.editImage);
router.get('/all', imageController.getAllImages);
router.get('/search', imageController.searchImages);
router.get('/:id', imageController.getImageById); // Hämta metadata
router.get('/download/:id', imageController.downloadImage); // Ladda ner en bild

module.exports = router;
