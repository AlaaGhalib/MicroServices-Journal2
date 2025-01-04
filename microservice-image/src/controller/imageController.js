const imageService = require('../bo/services/imageService');
const path = require('path'); // Importera path-modulen
const fs = require('fs');     // Importera fs-modulen
const multer = require('../middlewares/multerconfig');
const Image = require('../bo/models/image'); // Importera Image-modellen
const { authenticateDoctor } = require('../middlewares/auth');

// Ladda upp bild (tillgängligt för alla)
exports.uploadImage = (req, res) => {
    multer.single('image')(req, res, async (err) => {
        if (err) {
            return res.status(400).json({ error: `Multer error: ${err.message}` });
        }

        if (!req.file) {
            return res.status(400).json({ error: 'No file uploaded' });
        }

        try {
            const newImage = await imageService.saveImageMetadata({
                originalName: req.file.originalname,
                fileName: req.file.filename,
                path: req.file.path,
                size: req.file.size,
            });

            res.status(200).json({
                message: 'Image uploaded successfully',
                file: newImage,
            });
        } catch (error) {
            res.status(500).json({ error: `Failed to upload image: ${error.message}` });
        }
    });
};

// Redigera bild (endast för läkare)
exports.editImage = async (req, res) => {
    try {
        const { fileName, drawnImageBase64 } = req.body;

        if (!fileName || !drawnImageBase64) {
            return res.status(400).json({ error: 'File name and image data are required' });
        }

        const uploadsDir = path.join(__dirname, '../../uploads');
        

        const editedFileName = `edited-${Date.now()}-${fileName.replace(/^edited-/, '')}`;
        const editedFilePath = path.join(uploadsDir, editedFileName);

        const base64Data = drawnImageBase64.replace(/^data:image\/\w+;base64,/, '');
        fs.writeFileSync(editedFilePath, Buffer.from(base64Data, 'base64'));

        const originalImage = await Image.findOne({ where: { fileName } });
        if (!originalImage) {
            return res.status(404).json({ error: 'Original image not found' });
        }

        const editedImage = await Image.create({
            originalName: `Edited: ${originalImage.originalName}`,
            fileName: editedFileName,
            path: editedFilePath,
            size: fs.statSync(editedFilePath).size,
        });

        return res.status(200).json({ message: 'Image edited successfully', image: editedImage });
    } catch (error) {
        console.error('Error editing image:', error.message);
        return res.status(500).json({ error: 'An error occurred while editing the image' });
    }
};

// Hämta alla bilder (tillgängligt för alla)
exports.getAllImages = async (req, res) => {
    try {
        const images = await imageService.getAllImages();
        res.status(200).json(images);
    } catch (error) {
        res.status(500).json({ error: `Failed to fetch images: ${error.message}` });
    }
};

// Sök bilder (tillgängligt för alla)
exports.searchImages = async (req, res) => {
    const { name, date } = req.query;

    try {
        const images = await imageService.searchImages({ name, date });
        res.status(200).json(images);
    } catch (error) {
        res.status(500).json({ error: `Failed to search images: ${error.message}` });
    }
};

// Hämta metadata för en bild (tillgängligt för alla)
exports.getImageById = async (req, res) => {
    const { id } = req.params;

    try {
        const image = await imageService.getImageById(id);
        res.status(200).json(image);
    } catch (error) {
        res.status(404).json({ error: `Image not found: ${error.message}` });
    }
};

// Ladda ner en bild (tillgängligt för alla)
exports.downloadImage = async (req, res) => {
    const { id } = req.params;

    try {
        const filePath = await imageService.downloadImage(id);
        res.download(filePath);
    } catch (error) {
        res.status(404).json({ error: `Failed to download image: ${error.message}` });
    }
};
