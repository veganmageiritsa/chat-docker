var stompClient = null;

function setConnected(connected) {

    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}

function connect() {

    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {

        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (messageOutput) {

            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

function disconnect() {

    if (stompClient != null) {
        stompClient.disconnect();
    }

    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {

    var from = document.getElementById('from').value;
    var text = document.getElementById('text').value;
    stompClient.send("/app/chat", {}, JSON.stringify({'name': from, 'message': text}));
}

function showMessageOutput(messageOutput) {
    messageOutput.Id = undefined;
    console.log(messageOutput)
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode( "Id :" + messageOutput.id + " " + messageOutput.name + ": " + messageOutput.message + " (" + messageOutput.date + ")"));
    response.appendChild(p);
}

function connectionSuccess() {
    stompClient.subscribe('/topic/messages', onMessageReceived);

    stompClient.send("/app/chat.newUser", {}, JSON.stringify({
        sender: name,
        type: 'newUser'
    }))

}


















// var stompClient = null;
//
// $(document).ready(function() {
//     console.log("Index page is ready");
//     $("#send-private").click(function() {
//         sendPrivateMessage();
//     });
//     $("#connect").click(function() {
//         connect();
//     });
// });
//
// function connect() {
//
//     // Try to set up WebSocket connection with the handshake
//     const socket = new SockJS('/ws-register');
//     // Create a new StompClient object with the WebSocket endpoint
//     stompClient = Stomp.over(socket);
//
//     // Start the STOMP communications, provide a callback for when the CONNECT frame arrives.
//     stompClient.connect(
//       function (frame) {
//           showInfo(frame);
//           console.log(frame);
//           stompClient.subscribe('/user/topic/messages', function (message) {
//               showMessage(JSON.parse(message.body).content);
//           });
//       },
//       function (err){
//         showInfo(err);
//       }
//     );
// }
// function sendPrivateMessage() {
//     let message = document.getElementById("private-message").value;
//
//     console.log("Sending private message '"+message);
//
//     stompClient.send(
//       "/app/message/",
//       JSON.stringify({'messageContent': message})
//     );
// }
//
// function showMessage(message) {
//     $("#messages").append("<tr><td style='font-size: 8px'>" + message + "</td></tr>");
// }
//
// function showInfo(message) {
//     $("#connected").append("<tr><td>" + message + "</td></tr>");
// }


