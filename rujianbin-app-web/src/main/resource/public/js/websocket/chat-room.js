/**
 * 聊天室
 **/
var websocket = null;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://localhost:7070/rujianbin-provider/websocket");
}
else {
    alert('当前浏览器 Not support websocket')
}

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
    addMessage(msg.from,msg.content)

}

websocket.onclose = function () {
    console.log("WebSocket连接关闭");
}
window.onbeforeunload = function () {
    websocket.close();
}


//发送聊天消息
var sendMsg = function(msg){
    websocket.send(msg);
}


var addMessage = function(from, msg){
    var li = document.createElement('li');
    li.innerHTML = '<span>' + from + '</span>' + ' : ' + msg;
    document.querySelector('#chat_conatiner').appendChild(li);

    // 设置内容区的滚动条到底部
    document.querySelector('#chat').scrollTop = document.querySelector('#chat').scrollHeight;

    // 并设置焦点
    document.querySelector('textarea').focus();

}

var send = function(){

    var ele_msg = document.querySelector('textarea');
    var msg = ele_msg.value.replace('\r\n', '').trim();
    console.log(msg);
    if(!msg) return;
    sendMsg(msg);
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