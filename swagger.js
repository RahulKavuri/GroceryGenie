const swaggerJSDoc = require("swagger-jsdoc");

const options = {
  definition: {
    openapi: "3.0.0",
    info: {
      title: "My API Docs",
      version: "1.0.0",
      description: "Auto-generated API documentation using Swagger",
    },
    servers: [
      {
        url: "http://localhost:5000", // ✅ Correct base path
      },
    ],
    components: {
      schemas: {
        User: {
          type: "object",
          properties: {
            username: { type: "string" },
            email: { type: "string" },
            password: { type: "string" },
          },
        },
        StudentResult: {
          type: "object",
          properties: {
            studentId: { type: "integer" },
            subject: { type: "string" },
            score: { type: "number" },
          },
        },
      },
    },
  },
  apis: ["./routes/*.js"], // Path to your routes with swagger comments
};

const swaggerSpec = swaggerJSDoc(options);
module.exports = swaggerSpec;
