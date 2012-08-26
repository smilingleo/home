var app = require('express').createServer()
    , jade = require('jade')
//    , sio = require('socket.io')
    , socket = require('socket.io').listen(app)
    , _ = require('underscore')._
    , Backbone = require('backbone')
    , redis = require('redis')
    , rc = redis.createClient()
    , models = require('./models/models');


app.set('view engine', 'jade');
app.set('view options', {layout: false});

app.get('/*.(js|css)', function(req, res){
    res.sendfile('./'+req.url);
});

app.get('/', function(req, res){
    res.render('index');
});

var activeClients = 0;
var nodeChatModel = new models.NodeChatModel();

rc.lrange('chatentries', -10, -1, function(err, data) {
    if (data) {
        _.each(data, function(jsonChat) {
            var chat = new models.ChatEntry();
            chat.mport(jsonChat);
            nodeChatModel.chats.add(chat);
        });

        console.log('Revived ' + nodeChatModel.chats.length + ' chats');
    }
    else {
        console.log('No data returned for key');
    }
});

app.listen(8000,function(){
    var addr = app.address();
    console.log('   app listening on http://' + addr.address + ':' + addr.port);
});

//var io = sio.listen(app);
//
//io.sockets.on('connection', function(socket){
//    
//});

socket.sockets.on('connection', function(client){
    activeClients += 1;
    client.on('disconnect', function(){
        clientDisconnect(client)
    });

    client.on('message', function(msg){
        chatMessage(client, socket, msg)
    });

    client.send({
        event: 'initial',
        data: nodeChatModel.xport()
    });
//    debugger;
//    client.broadcast.emit('update', activeClients);
});

function chatMessage(client, socket, msg){
    var chat = new models.ChatEntry();
    chat.mport(msg);

    rc.incr('next.chatentry.id', function(err, newId) {
        chat.set({id: newId});
        nodeChatModel.chats.add(chat);

        console.log('(' + client.sessionId + ') ' + chat.get('id') + ' ' + chat.get('name') + ': ' + chat.get('text'));

        rc.rpush('chatentries', chat.xport(), redis.print);
        rc.bgsave();

        client.broadcast.emit('chat', activeClients, chat.xport()); 
    }); 
}

function clientDisconnect(client) {
    activeClients -= 1;
    client.broadcast({clients:activeClients})
}

