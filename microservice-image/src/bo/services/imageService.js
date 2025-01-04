const path = require('path'); // Importera path-modulen
const fs = require('fs');     // Importera fs-modulen
const sharp = require('sharp');
const { Op } = require('sequelize');
const Image = require('../models/image');

// Spara metadata i databasen
exports.saveImageMetadata = async (imageData) => {
    try {
        return await Image.create(imageData);
    } catch (error) {
        throw new Error('Failed to save image metadata: ' + error.message);
    }
};



// Redigera en bild och spara metadata för den redigerade bilden
exports.editImage = async (req, res) => {
    try {
        console.log('Received Request Body:', req.body);

        const { fileName, drawnImageBase64 } = req.body;

        if (!fileName || !drawnImageBase64) {
            console.error('Missing fileName or drawnImageBase64');
            return res.status(400).json({ error: 'File name and image data are required' });
        }

        // Filväg för redigerad bild
        const editedFileName = `edited-${fileName}`;
        const editedFilePath = path.join(__dirname, '../../uploads', editedFileName);

        // Spara Base64-bilden
        const base64Data = drawnImageBase64.replace(/^data:image\/png;base64,/, '');
        fs.writeFileSync(editedFilePath, Buffer.from(base64Data, 'base64'));

        console.log('Image saved to:', editedFilePath);

        // Hitta originalbilden
        const originalImage = await Image.findOne({ where: { fileName } });
        if (!originalImage) {
            console.error('Original image not found');
            return res.status(404).json({ error: 'Original image not found' });
        }

        // Spara redigerad bild i databasen
        const editedImage = await Image.create({
            originalName: `Edited: ${originalImage.originalName}`,
            fileName: editedFileName,
            path: editedFilePath,
            size: fs.statSync(editedFilePath).size,
            uploadedAt: new Date(),
        });

        console.log('Edited image saved successfully:', editedImage);

        return res.status(200).json(editedImage); // Rätt statuskod och respons
    } catch (error) {
        console.error('Error editing image:', error.message);
        return res.status(500).json({ error: 'Failed to edit image', details: error.message });
    }
};



// Hämta alla bilder
exports.getAllImages = async () => {
    try {
        return await Image.findAll();
    } catch (error) {
        throw new Error('Failed to fetch images: ' + error.message);
    }
};

// Hämta metadata för en bild
exports.getImageById = async (id) => {
    try {
        const image = await Image.findByPk(id);
        if (!image) {
            throw new Error('Image not found');
        }
        return image;
    } catch (error) {
        throw new Error('Failed to fetch image: ' + error.message);
    }
};

// Sök bilder
exports.searchImages = async ({ name, date }) => {
    const where = {};
    if (name) where.originalName = { [Op.like]: `%${name}%` };
    if (date) where.createdAt = { [Op.gte]: new Date(date) };

    try {
        return await Image.findAll({ where });
    } catch (error) {
        throw new Error('Failed to search images: ' + error.message);
    }
};


// Ladda ner en bild
exports.downloadImage = async (id) => {
    try {
        const image = await Image.findByPk(id);
        if (!image) {
            throw new Error('Image not found');
        }

        const filePath = path.resolve(image.path);
        return filePath;
    } catch (error) {
        throw new Error('Failed to download image: ' + error.message);
    }
}
