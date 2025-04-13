const User = require("../models/userModel");

const bcrypt = require("bcryptjs");
 // Make sure you're using bcrypt

 exports.loginUser = async (req, res) => {
  const { email, password } = req.body;

  console.log("🟡 Incoming login request");
  console.log("🟠 Raw body:", req.body);  // See exactly what Android sends
  console.log("Email:", email);
  console.log("Password:", password);

  if (!email || !password) {
    console.warn("⚠️ Missing email or password in request body");
    return res.status(400).json({ success: false, message: "Missing credentials" });
  }

  try {
    const user = await User.findOne({ where: { email } });
    console.log("🔵 Found user:", user);

    if (!user) {
      return res.status(400).json({ success: false, message: "Invalid email" });
    }

    const isMatch = await bcrypt.compare(password, user.password);
    console.log("🔵 Password match:", isMatch);

    if (!isMatch) {
      return res.status(400).json({ success: false, message: "Invalid password" });
    }

    return res.status(200).json({ success: true, message: "Login successful", user });

  } catch (err) {
    console.error("❌ Login error:", err);
    return res.status(500).json({ success: false, message: "Server error" });
  }
};




exports.createUser = async (req, res) => {
  try {
    const { username, email, password } = req.body;
    console.log("Creating user:", username, email);

    // 🔐 Hash the password before storing
    const hashedPassword = await bcrypt.hash(password, 10);

    const newUser = await User.create({ username, email, password: hashedPassword });

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

    if (password) {
      // 🔐 Hash new password before saving
      user.password = await bcrypt.hash(password, 10);
    }

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
