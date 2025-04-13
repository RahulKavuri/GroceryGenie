const Product = require('../models/productModel'); // Import Product model
const { Op } = require('sequelize'); // Import Sequelize Op for query operations

// Controller to add a new product
exports.addProduct = async (req, res) => {
  try {
    const { title, quantity, cost, imageUrl } = req.body;
    const newProduct = await Product.create({ title, quantity, cost, imageUrl });
    res.status(201).json({ message: "Product added", product: newProduct });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};


// Controller to get all products
exports.getProducts = async (req, res) => {
  try {
    const products = await Product.findAll();
    res.json(products);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

// Controller to search products by name
exports.searchProducts = async (req, res) => {
  const nameQuery = req.query.query;

  try {
    if (nameQuery) {
      const products = await Product.findAll({
        where: {
          title: {
            [Op.like]: `%${nameQuery}%`, // Case-insensitive search
          },
        },
      });

      if (products.length > 0) {
        res.json(products); // Send matching products
      } else {
        res.status(404).json({ message: 'No products found' }); // No match found
      }
    } else {
      const allProducts = await Product.findAll(); // If no name query, fetch all products
      res.json(allProducts);
    }
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Server error, please try again later' });
  }
};
