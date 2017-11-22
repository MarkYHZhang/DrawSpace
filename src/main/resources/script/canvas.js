var canvas = document.getElementById('canvas');
var context = canvas.getContext('2d');
var radius = 10;
var dragging = false;

canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

context.lineWidth = radius*2;

const vConstant = -60;

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
		
		// if(alterCnt=6){
			webSocket.send(e.clientX+" "+(e.clientY+vConstant)+" "+radius+" "+context.strokeStyle);
			alterCnt=0;
		// }
		alterCnt++;
	}
}

var engage = function(e){
	dragging = true;
	putPoint(e);
}

var disengage = function(){
	dragging = false;
	context.beginPath();
}


var clearButton = document.getElementById('clear');
clearButton.addEventListener('click', function(){
	context.clearRect(0,0,canvas.width,canvas.height)
});

canvas.addEventListener('mousedown', engage);
canvas.addEventListener('mousemove', putPoint);
canvas.addEventListener('mouseup', disengage);




//websocket
var webSocket = new WebSocket("ws://localhost:8000/communication/");
webSocket.onmessage = function (msg) {

	arr = msg.data.split(" ");
	x=arr[0];
	y=arr[1];
	r=arr[2];
	c=arr[3];

    context.fillStyle = c;
    context.strokeStyle = c;

    context.lineTo(x, y); //Linea
    context.stroke(); //Linea

    context.beginPath();
    context.arc(x, y, r, 0, 100);
    context.fill();

    context.beginPath(); //Linea
    context.moveTo(x, y); //Linea

    context.beginPath();
};
webSocket.onclose = function () { alert("WebSocket connection closed") };