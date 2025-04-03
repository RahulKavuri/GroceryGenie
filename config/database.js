require("dotenv").config();
const { Sequelize } = require("sequelize");

const sequelize = new Sequelize(
  process.env.MYSQLDATABASE || process.env.DB_NAME, // Railway uses MYSQLDATABASE
  process.env.MYSQLUSER || process.env.DB_USER,     // Railway uses MYSQLUSER
  process.env.MYSQLPASSWORD || process.env.DB_PASSWORD, {
    host: process.env.MYSQLHOST || process.env.DB_HOST,
    port: process.env.MYSQLPORT || process.env.DB_PORT || 3306,
    dialect: 'mysql',
    dialectOptions: {
      ssl: { // ← Required for Railway MySQL
        require: true,
        rejectUnauthorized: false
      }
    },
    logging: process.env.NODE_ENV === 'development' ? console.log : false
  }
);

// Test connection
sequelize.authenticate()
  .then(() => console.log("✅ Database connected"))
  .catch((err) => {
    console.error("❌ Database connection failed:", err);
    process.exit(1); // Crash app if DB connection fails
  });

module.exports = sequelize;