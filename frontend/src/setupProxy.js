const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = (app) => {
	app.use(
		"/ws",
		createProxyMiddleware({ target: "http://localhost:8620", ws: true })
	);
};