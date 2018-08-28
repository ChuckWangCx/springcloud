// var createError = require('http-errors');
var express = require('express');
// var path = require('path');
// var cookieParser = require('cookie-parser');
// var logger = require('morgan');
//
// var indexRouter = require('./routes/index');
// var usersRouter = require('./routes/users');

var app = express();
var zookeeper = require('node-zookeeper-client');

var PORT = 1234;
var CONNECTION_STRING = '192.168.241.10:2185';
var REGISTRY_ROOT = '/registry';

// 连接 ZooKeeper
var zk = zookeeper.createClient(CONNECTION_STRING);
zk.connect();

// // view engine setup
// app.set('views', path.join(__dirname, 'views'));
// app.set('view engine', 'pug');
//
// app.use(logger('dev'));
// app.use(express.json());
// app.use(express.urlencoded({ extended: false }));
// app.use(cookieParser());
// app.use(express.static(path.join(__dirname, 'public')));
//
// app.use('/', indexRouter);
// app.use('/users', usersRouter);

// catch 404 and forward to error handler
// app.use(function(req, res, next) {
//     next(createError(404));
// });

// error handler
// app.use(function(err, req, res, next) {
//     // set locals, only providing error in development
//     res.locals.message = err.message;
//     res.locals.error = req.app.get('env') === 'development' ? err : {};
//
//     // render the error page
//     res.status(err.status || 500);
//     res.render('error');
// });

// app.set('port', PORT);
app.use(express.static('public'));
app.all('*', function (req, res) {
    // 处理图标请求
    if (req.path == '/favicon.ico') {
        res.end();
        return;
    }
    // 获取服务器名称
    var serviceName = req.get('Service-Name');
    console.log('serviceName: %s', serviceName);
    if (!serviceName) {
        console.log('Service-Name request header is not exist');
        res.end();
        return;
    }
    // 获取服务路径
    var servicePath = REGISTRY_ROOT + '/' + serviceName;
    console.log('servicePath: %s', servicePath);
    // 获取服务路径下的地址节点
    zk.getChildren(servicePath, function (error, addressNodes) {
        if (error) {
            console.log(error.stack);
            res.end();
            return;
        }
        var size = addressNodes.length;
        if (size == 0) {
            console.log('address node is not exist');
            res.end();
            return;
        }
        // 生成地址路径
        var addressPath = servicePath + '/';
        if (size == 1) {
            // 若只有一个地址，则随机获取一个地址
            addressPath += addressNodes[0];
        } else {
            // 若存在多个个地址，则随机获取一个地址
            addressPath += addressNodes[parseInt(Math.random() * size)]
        }
        console.log('addressPath: %s', addressPath);
        // 获取服务地址
        zk.getData(addressPath, function (error, serviceAddress) {
            if (error) {
                console.log(error.stack);
                res.end();
                return;
            }
            console.log('serviceAddress: %s', serviceAddress);
            if (!serviceAddress) {
                console.log('service address is not exist');
                res.end();
                return;
            }
            // TODO
        });
    });
});
app.listen(PORT, function () {
    console.log('server is running at %d',PORT);
});

// module.exports = app;