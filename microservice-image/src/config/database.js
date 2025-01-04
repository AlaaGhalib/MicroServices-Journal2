const { Sequelize } = require('sequelize');
const Image = require('../bo/models/image');

const sequelize = new Sequelize('imageservice', 'root', 'Aprilapril23.', {
    host: 'localhost',
    dialect: 'mysql',
});

sequelize.sync({ alter: true })
    .then(() => console.log('Database synchronized'))
    .catch((error) => console.error('Error synchronizing the database:', error));

module.exports = sequelize;