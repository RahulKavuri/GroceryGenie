require("dotenv").config();
const { Sequelize } = require("sequelize");

// Configuration for both local and Railway
const sequelize = new Sequelize(
  process.env.MYSQLDATABASE || process.env.DB_NAME, // Fallback to local DB_NAME
  process.env.MYSQLUSER || process.env.DB_USER,     // Fallback to local DB_USER
  process.env.MYSQLPASSWORD || process.env.DB_PASSWORD, {
    host: process.env.MYSQLHOST || process.env.DB_HOST,
    port: process.env.MYSQLPORT || process.env.DB_PORT || 3306,
    dialect: 'mysql',
    dialectOptions: {
      ssl: process.env.NODE_ENV === 'production' ? { // SSL only for production
        require: true,
        rejectUnauthorized: false
      } : null
    },
    logging: process.env.NODE_ENV === 'development' ? console.log : false,
    pool: {
      max: 5,
      min: 0,
      acquire: 30000,
      idle: 10000
    }
  }
);

// Test connection
sequelize.authenticate()
  .then(() => console.log(`✅ Connected to ${process.env.NODE_ENV} database`))
  .catch(err => {
    console.error("❌ Connection failed:", err.message);
    process.exit(1);
  });

module.exports = sequelize;