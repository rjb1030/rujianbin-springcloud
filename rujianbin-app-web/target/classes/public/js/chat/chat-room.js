/**
 * 聊天室
 **/
var websocket = null;
var userName = document.querySelector('#ws_userName').value;
var nickName = document.querySelector('#ws_nickName').value;
var token = document.querySelector('#ws_token').value;
var createSpringSocket = function(){
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:7032/rujianbin-app-websocket-chatroom/websocket?token="+token+"&nickName="+nickName+"&userName="+userName);
        bindEvent();
    }
    else {
        alert('当前浏览器 Not support websocket');
    }
}


var createNioJdkSocket = function () {
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:7090?token="+token+"&nickName="+nickName+"&userName="+userName);
        bindEvent();
    }
    else {
        alert('当前浏览器 Not support websocket');
    }
}

var bindEvent = function () {
    websocket.onerror = function () {
        console.log("WebSocket连接发生错误");
    };

    websocket.onopen = function () {
        console.log("WebSocket连接成功");
        addMessage("系统","您成功聊天室")
    }

    websocket.onmessage = function (event) {
        console.log(event);

        var msg = eval("("+event.data+")");
        addMessage(msg.from,msg.content,msg.onlineCount);

    }

    websocket.onclose = function () {
        console.log("WebSocket连接关闭");
    }
}



window.onbeforeunload = function () {
    if(websocket){
        websocket.close();
    }
}


//发送聊天消息
var _sendMsg = function(msg){
    websocket.send(msg);
}


var addMessage = function(from, msg,onlineCount){
    var li = document.createElement('li')
    if(from=="你"){
        li.style.textAlign="right";
    }

    li.innerHTML = '<span>' + from + '</span>' + ' : ' + msg;
    document.querySelector('#chat_conatiner').appendChild(li);

    // 设置内容区的滚动条到底部
    document.querySelector('#chat').scrollTop = document.querySelector('#chat').scrollHeight;

    // 并设置焦点
    document.querySelector('textarea').focus();
    if(onlineCount){
        document.querySelector('#ws_online_count').innerHTML=onlineCount;
    }


}

var send = function(){
    if(!websocket){
        alert("websocket未创建");
        return;
    }
    var ele_msg = document.querySelector('textarea');
    var msg = ele_msg.value.replace('\r\n', '').trim();
    console.log(msg);
    if(!msg) return;
    _sendMsg(msg);
    // 添加消息到自己的内容区
    addMessage('你', msg);
    ele_msg.value = '';
}

document.querySelector('textarea').addEventListener('keypress', function(event){
    if(event.which == 13){
        send();
    }
});
document.querySelector('#send').addEventListener('click', function(){
    send();
});

document.querySelector('#clear').addEventListener('click', function(){
    document.querySelector('#chat_conatiner').innerHTML = '';
});

document.querySelector('#springwebsocket').addEventListener('click', function(){
    createSpringSocket();
});

document.querySelector('#niojdk').addEventListener('click', function(){
    createNioJdkSocket();
});