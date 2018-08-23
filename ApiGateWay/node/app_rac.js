//搭建Node.js 集群，因为是node.js单线程，所以线程数跟CPU内核数一致
//只能在单台机器上搭建集群
var http = require('http');
var cluster = require('cluster');
var os = require('os');

var PORT = 1234;
var CPUS = os.cpus().length; //获取 CPU 内核数

if (cluster.isMaster) {
	//当前进程为主进程
	for (var i = 0; i < CPUS; i++) {
		cluster.fork();
	}
} else {
	//当前进程为子进程
	var app = http.createServer(function (req, res) {
		res.writeHead(200, {'Content-Type': 'text/html'});
		res.write('<h1>Hello</h1>');
		res.end();
	});
	app.listen(PORT, function () {
		console.log('server is running at %d', PORT);
		});
}
		