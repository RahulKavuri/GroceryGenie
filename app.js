require("dotenv").config();
const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");
const sequelize = require("./config/database");
const userRoutes = require("./routes/userRoutes");
const productRoutes = require("./routes/productRoutes");

// Swagger
const swaggerUi = require("swagger-ui-express");
const swaggerSpec = require("./swagger");

const app = express();

app.use(cors());
app.use(bodyParser.json());

// Mount Routes
app.use("/api", userRoutes);

app.use("/api", productRoutes);

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

    app.listen(PORT, () => {
      console.log(`✅ Server running on port ${PORT}`);
    });
    

  } catch (error) {
    console.error("❌ Server startup failed:", error);
  }
}

startServer();
