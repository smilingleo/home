var NodeChatController = {
    init: function() {
        this.socket = io.connect(); //new io.Socket({port: 8000});
        var mysocket = this.socket;

        this.model = new models.NodeChatModel();
        this.view = new NodeChatView({model: this.model, socket: this.socket, el: $('#content')});
        var view = this.view;

        this.socket.on('message', function(msg) {view.msgReceived(msg)});
        this.socket.connect();

        this.view.render();

        return this;
    }
};

$(document).ready(function () {
    window.app = NodeChatController.init({});
});
