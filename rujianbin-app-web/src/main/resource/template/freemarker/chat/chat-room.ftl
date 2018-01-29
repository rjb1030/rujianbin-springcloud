<!DOCTYPE html>
<html>
<head>
    <title>socket.io 聊天室例子</title>
    <meta charset="utf-8">

    <link rel="stylesheet" href="/${baseContext}/css/chat-room/reset.css"/>
    <link rel="stylesheet" href="/${baseContext}/css/chat-room/bootstrap.css"/>
    <link rel="stylesheet" href="/${baseContext}/css/chat-room/app.css"/>
</head>
<body>
<div class="wrapper">
    <input type="hidden" id="ws_token" name="token" value="${token}">
    <input type="hidden" id="ws_nickName" name="nickName" value="${nickName}">
    <input type="hidden" id="ws_userName" name="userName" value="${userName}">
    <div style="padding: 10px 0px 0px 12px;"><span style="color: green">当前聊天室人数：</span><span id="ws_online_count" style="color: red">0</span></div>
    <div class="content" id="chat">
        <ul id="chat_conatiner">
        </ul>

    </div>
    <div class="action">
        <textarea ></textarea>
        <button class="btn btn-success" id="springwebsocket">springwebsocket连接</button>
        <button class="btn btn-success" id="niojdk">niojdk连接</button>
        <button class="btn btn-success" id="clear">清屏</button>
        <button class="btn btn-success" id="send">发送</button>
    </div>
</div>

<script type="text/javascript" src="/${baseContext}/js/chat/chat-room.js"></script>
<script type="text/javascript">


</script>
</body>
</html>