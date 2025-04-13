const User = require("../models/userModel");


exports.loginUser = async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
      return res.status(400).json({ message: 'Email and password required' });
  }

  try {
      const user = await User.findOne({ where: { email } });
      if (!user) {
          return res.status(404).json({ message: 'User not found' });
      }

      const validPassword = await bcrypt.compare(password, user.password);
      if (!validPassword) {
          return res.status(401).json({ message: 'Invalid password' });
      }

      res.status(200).json({
          message: 'Login successful',
          user: {
              id: user.id,
              name: user.name,
              email: user.email
          }
      });
  } catch (err) {
      res.status(500).json({ message: 'Server error', error: err.message });
  }
};


exports.createUser = async (req, res) => {
  try {
    const { username, email, password } = req.body;
    console.log("Creating user:", username, email);

    const newUser = await User.create({ username, email, password });

    console.log("✅ User created successfully!", newUser.toJSON());
    res.status(201).json({ message: "User created successfully", user: newUser });
  } catch (error) {
    console.error("❌ Error inserting user:", error);
    res.status(500).json({ error: error.message });
  }
};




exports.getAllUsers = async (req, res) => {
  try {
    const users = await User.findAll();
    res.json(users);
  } catch (error) {
    res.status(500).json({ error: "Internal Server Error" });
  }
};


// Get user by email
exports.getUserByEmail = async (req, res) => {
  try {
    const user = await User.findOne({ where: { email: req.params.email } });
    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }
    res.json(user);
  } catch (error) {
    res.status(500).json({ error: "Internal Server Error" });
  }
};

// Update user by email
exports.updateUser = async (req, res) => {
  try {
    const { username, password } = req.body;
    const user = await User.findOne({ where: { email: req.params.email } });
    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }
    user.username = username || user.username;
    user.password = password || user.password;
    await user.save();
    res.json({ message: "User updated successfully" });
  } catch (error) {
    res.status(500).json({ error: "Internal Server Error" });
  }
};

// Delete user by email
exports.deleteUserByEmail = async (req, res) => {
  try {
    const user = await User.findOne({ where: { email: req.params.email } });
    if (!user) {
      return res.status(404).json({ error: "User not found" });
    }
    await user.destroy();
    res.json({ message: "User deleted successfully" });
  } catch (error) {
    res.status(500).json({ error: "Internal Server Error" });
  }
};
