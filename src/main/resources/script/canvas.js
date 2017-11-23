var canvas = document.getElementById('canvas');
var context = canvas.getContext('2d');
var radius = 10;
var dragging = false;

canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

context.lineWidth = radius*2;

const vConstant = -60;

var dragNum = 0;
var alterCnt = 0;
var putPoint = function(e){
	if (dragging) {
		context.lineTo(e.clientX, e.clientY+vConstant); //Linea
		context.stroke(); //Linea

		context.beginPath();
		context.arc(e.clientX, e.clientY+vConstant, radius, 0, 100);
		context.fill();

		context.beginPath(); //Linea
		context.moveTo(e.clientX, e.clientY+vConstant); //Linea
		
		if(alterCnt=100){
			webSocket.send(id+" "+e.clientX+" "+(e.clientY+vConstant)+" "+radius+" "+context.strokeStyle+" "+dragNum);
			alterCnt=0;
		}
		alterCnt++;
	}
}

var engage = function(e){
    context.beginPath();
    dragging = true;
    dragNum++;
    putPoint(e);
}

var disengage = function(){
	dragging = false;
    context.beginPath();
}


var clearButton = document.getElementById('clear');
clearButton.addEventListener('click', function(){
	context.clearRect(0,0,canvas.width,canvas.height)
	webSocket.send("clear");
});

canvas.addEventListener('mousedown', engage);
canvas.addEventListener('mousemove', putPoint);
canvas.addEventListener('mouseup', disengage);




//websocket
var id = "";
var vis = false;
var webSocket = new WebSocket("ws://localhost:8000/communication/");

var preDragCnt=-1;
webSocket.onmessage = function (msg) {
	if (!vis) {
		vis = true;
		id = msg.data;
    }else{
        var data = msg.data;
		if(data==="clear")context.clearRect(0,0,canvas.width,canvas.height);
        arr = data.split(" ");
        curId = arr[0];
        if (curId!=id) {
            x = arr[1];
            y = arr[2];
            r = arr[3];
            c = arr[4];
            dragCnt = arr[5];
            if(dragCnt!=preDragCnt){
                context.beginPath();
            	preDragCnt = dragCnt;
            }
            var tradius = radius;
            var tlineWidth = context.lineWidth;
            radius = r;
            context.lineWidth = r*2;

            var tfillStyle = context.fillStyle;
            var tstrokeStyle = context.strokeStyle;
            context.fillStyle = c;
            context.strokeStyle = c;

            context.lineTo(x, y); //Linea
            context.stroke(); //Linea

            context.beginPath();
            context.arc(x, y, r, 0, 100);
            context.fill();

            context.beginPath(); //Linea
            context.moveTo(x, y); //Linea

			radius=tradius;
			context.lineWidth=tlineWidth;
            context.fillStyle = tfillStyle;
            context.strokeStyle = tstrokeStyle;
        }
	}
};
webSocket.onclose = function () { alert("WebSocket connection closed") };
