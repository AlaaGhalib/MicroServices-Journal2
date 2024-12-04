const express = require("express");
const multer = require("multer");
const sharp = require("sharp");
const fs = require("fs");
const path = require("path");

const router = express.Router();

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        const uploadDir = "./uploads";
        if (!fs.existsSync(uploadDir)) {
            fs.mkdirSync(uploadDir, { recursive: true });
        }
        cb(null, uploadDir);
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + path.extname(file.originalname));
    },
});

const upload = multer({ storage });

router.post("/upload", upload.single("image"), (req, res) => {
    if (!req.file) {
        return res.status(400).send("No file uploaded.");
    }
    res.status(200).json({
        message: "Image uploaded successfully",
        filename: req.file.filename,
        path: `/uploads/${req.file.filename}`,
    });
});

router.get("/image/:filename", (req, res) => {
    const imagePath = path.join(__dirname, "../uploads", req.params.filename);
    if (fs.existsSync(imagePath)) {
        res.sendFile(imagePath);
    } else {
        res.status(404).send("Image not found.");
    }
});

router.post("/edit/add-text", async (req, res) => {
    const { filename, text, x, y, fontSize = 24 } = req.body;

    const imagePath = path.join(__dirname, "../uploads", filename);
    const outputPath = path.join(__dirname, "../uploads", `edited-${filename}`);

    if (!fs.existsSync(imagePath)) {
        return res.status(404).send("Image not found.");
    }

    try {
        await sharp(imagePath)
            .composite([
                {
                    input: Buffer.from(`
            <svg width="500" height="500">
              <text x="${x}" y="${y}" font-size="${fontSize}" fill="white">${text}</text>
            </svg>
          `),
                    blend: "over",
                },
            ])
            .toFile(outputPath);

        res.status(200).json({
            message: "Text added successfully",
            path: `/uploads/edited-${filename}`,
        });
    } catch (error) {
        console.error(error);
        res.status(500).send("Failed to edit image.");
    }
});

router.post("/edit/draw", async (req, res) => {
    const { filename, x, y, width, height, color = "red" } = req.body;

    const imagePath = path.join(__dirname, "../uploads", filename);
    const outputPath = path.join(__dirname, "../uploads", `drawn-${filename}`);

    if (!fs.existsSync(imagePath)) {
        return res.status(404).send("Image not found.");
    }

    try {
        await sharp(imagePath)
            .composite([
                {
                    input: Buffer.from(`
            <svg width="500" height="500">
              <rect x="${x}" y="${y}" width="${width}" height="${height}" fill="${color}" />
            </svg>
          `),
                    blend: "over",
                },
            ])
            .toFile(outputPath);

        res.status(200).json({
            message: "Drawing added successfully",
            path: `/uploads/drawn-${filename}`,
        });
    } catch (error) {
        console.error(error);
        res.status(500).send("Failed to draw on image.");
    }
});

router.delete("/image/:filename", (req, res) => {
    const imagePath = path.join(__dirname, "../uploads", req.params.filename);
    if (fs.existsSync(imagePath)) {
        fs.unlinkSync(imagePath);
        res.status(200).send("Image deleted successfully.");
    } else {
        res.status(404).send("Image not found.");
    }
});

module.exports = router;
