require("dotenv").config();
const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");
const sequelize = require("./config/database");
const userRoutes = require("./routes/userRoutes");

// Swagger
const swaggerUi = require("swagger-ui-express");
const swaggerSpec = require("./swagger");

const app = express();

app.use(cors());
app.use(bodyParser.json());

// Mount Routes
app.use("/api", userRoutes);

// ✅ Swagger Docs route
app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerSpec));

// ✅ Homepage route
app.get("/", (req, res) => {
  res.send("Welcome to the API Server. Visit /api-docs for Swagger documentation.");
});

// ✅ Debugging: Check registered routes
console.log(app._router.stack
  .map(layer => layer.route && layer.route.path)
  .filter(path => path)
);

const PORT = process.env.PORT || 5000;

async function startServer() {
  try {
    await sequelize.sync({ alter: true });  
    console.log("✅ Database synced successfully!");

    app.listen(PORT, "0.0.0.0", () => {
      console.log(`✅ Server running on http://192.168.29.37:${PORT}`);
      console.log(`📘 Swagger docs at http://192.168.29.37:${PORT}/api-docs`);
    });

  } catch (error) {
    console.error("❌ Server startup failed:", error);
  }
}

startServer();
