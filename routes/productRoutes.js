const express = require("express");
const router = express.Router();
const productController = require("../controllers/productController");

// Route to add a new product
router.post("/products", productController.addProduct);


router.get("/products", productController.getProducts)

// ✅ Route to get all products or search by name using query param
router.get("/products/search", productController.searchProducts);

module.exports = router;
