const { DataTypes } = require("sequelize");
const sequelize = require("../config/database");

const User = sequelize.define("User", {
  username: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  email: {
    type: DataTypes.STRING,
    allowNull: false,
    unique: true,
    primaryKey: true,  // Email as primary key
  },
  password: {
    type: DataTypes.STRING,
    allowNull: false,
  },
}, {
  freezeTableName: true, // Prevents table name pluralization
  timestamps: false
});

module.exports = User;
