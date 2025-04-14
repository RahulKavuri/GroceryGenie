const express = require("express");
const router = express.Router();
const productController = require("../controllers/productController");

// Route to add a new product
router.post("api/products", productController.addProduct);


router.get("api/products", productController.getProducts)

// ✅ Route to get all products or search by name using query param
router.get("api/products/search", productController.searchProducts);

module.exports = router;
