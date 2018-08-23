// node.js网关反向代理
var http = require('http');
var httpProxy = require('http-proxy'); //加载 HTTP Proxy 模块

var PORT = 1234;

// 创建代理服务器对象并监听错误事件
var proxy = httpProxy.createProxyServer();
proxy.on('error', function (err, req, res) {
	res.end(); // 输出空白响应
});

var app = http.createServer(function (req, res) {
	// 执行反向代理
	proxy.web(req, res, {
		target: 'http://localhost:8080' // 目标网址
	});
});

app.listen(PORT, function () {
	console.log('server is running at %d', PORT);
});