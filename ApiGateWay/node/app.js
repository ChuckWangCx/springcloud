//node.js搭建Web服务器
var http = require('http');

var PORT = 1234;

var app = http.createServer(function (req, res) {
	var path = _dirname + req.url;
	fs.readFile(path, function (err, data) {
		if (err) {
			res.end();
			return;
		}
		res.write('<h1>Hello</hi>');
		res.end();
	});
});
	

app.listen(PORT, function () {
	console.log('server is running at %d', PORT);
});