const win = Ti.UI.createWindow({
	backgroundColor: '#161f2b'
});
win.open();

const lines = require('ti.lines');

const lineView = lines.createLine({
	width: 190,
	height: 250,
	lineWidth: 1,
	lineColor: "#fff",
	xAxis: true,
	yAxis: true,
	maxValue: 10,
	yLines: 1,
	xLines: 2,
	values: [2, 2, 5, 1, 3, 1, 3, 2, 3, 1, 1],
	startAt: lines.START_BOTTOM,
	fillSpace: true,
	fillColorTop: "#FF8310",
	fillColorBottom: "rgba(253, 200, 48,0)",
	padding: 10
});

const outerView = Ti.UI.createView({
	top: 20,
	left: 4,
	borderRadius: 10,
	width: 190,
	height: 250,
	elevation: 30,
	backgroundGradient: {
		type: 'linear',
		colors: [
			'#726dfc', '#504df4'
		],
		startPoint: {
			x: '100%',
			y: '100%'
		},
		endPoint: {
			x: '0%',
			y: '0%'
		},
		backFillStart: true
	}
})

const bg = Ti.UI.createView({
	top: 0,
	width: 190,
	height: 150,
	backgroundGradient: {
		type: 'linear',
		colors: [
			'#726dfc', "rgba(80,77,244,0)"
		],
		startPoint: {
			x: '0%',
			y: '0%'
		},
		endPoint: {
			x: '0%',
			y: '100%'
		},
		backFillStart: true
	}
})

outerView.add(lineView);
outerView.add(bg);
outerView.add(Ti.UI.createLabel({
	top: 10,
	left: 14,
	color: "#fff",
	text: "Simple graph",
	font: {
		fontSize: 18,
		fontWeight: "bold"
	}
}));
outerView.add(Ti.UI.createLabel({
	top: 35,
	left: 14,
	color: "#fff",
	text: "Titanium Mobile",
	font: {
		fontSize: 14
	}
}));
outerView.add(Ti.UI.createLabel({
	bottom: 15,
	right: 14,
	color: "#000",
	text: "click to change",
	font: {
		fontWeight: "bold",
		fontSize: 12
	}
}));

win.add(outerView);
win.add(lines.createLine({
	top: 20,
	right: 4,
	backgroundColor: "#c23b3b",
	width: 190,
	height: 250,
	lineWidth: 4,
	lineColorFrom: "#ffff00",
	lineColorTo: "#0000FF",
	xAxis: false,
	yAxis: false,
	maxValue: 10,
	padding: [10, 20, 10, 20],
	startAt: lines.START_BOTTOM,
	elevation: 40,
	borderRadius: 10,
	values: [0, 2, 5, 1, 3, 1, 3, 2, 3, 1, 1]
}));

const straightLines = lines.createLine({
	top: 290,
	left: 4,
	backgroundColor: "#3bc256",
	width: 190,
	height: 250,
	elevation: 40,
	borderRadius: 10,
	strokeType: lines.STROKE_DASHED
})
win.add(straightLines);
win.add(lines.createLine({
	top: 290,
	right: 4,
	backgroundColor: "#c23ba2",
	lineWidth: 2,
	lineColor: "#fff",
	width: 190,
	height: 250,
	elevation: 40,
	borderRadius: 10,
	values: [{
		x: 0,
		y: 0
	}, {
		x: 50,
		y: 200
	}, {
		x: 200,
		y: 10
	}, {
		x: 70,
		y: 20
	}, {
		x: 150,
		y: 200
	}, {
		x: 0,
		y: 20
	}]
}));

win.add(lines.createLine({
	top: 550,
	backgroundColor: "#c23ba2",
	lineWidth: 4,
	lineColorFrom: "#0000FF",
	lineColorTo: "#ffffff",
	left: 10,
	right: 10,
	height: 50,
	elevation: 40,
	borderRadius: 10,
	maxValue: 10,
	xAxis: true,
	yAxis: true,
	xLines: 2,
	yLines: 6,
	axisColor: "#000",
	axisWidth: 2,
	values: Array(8).fill().map(() => 10 * Math.random() - 5)
}));

const aniLines = lines.createLine({
	top: 610,
	backgroundColor: "#c23ba2",
	lineWidth: 2,
	lineColor: "#fff",
	width: 100,
	height: 50,
	elevation: 40,
	borderRadius: 10,
	maxValue: 10,
	values: Array(8).fill().map(() => 10 * Math.random())
})
win.add(aniLines);
var ani = Ti.UI.createAnimation({
	width: 200,
	duration: 2000,
	autoreverse: true,
	repeat: 1
});
aniLines.animate(ani);


win.addEventListener("open", function() {
	straightLines.values = [{
		x: 0,
		y: 0
	}, {
		x: 50,
		y: 200
	}, {
		x: 200,
		y: 10
	}, {
		x: 70,
		y: 20
	}, {
		x: 150,
		y: 200
	}, {
		x: 0,
		y: 20
	}]
	straightLines.lineWidth = 4;
	straightLines.lineColor = "#000";
	straightLines.lineType = lines.TYPE_STRAIGHT;
	straightLines.redraw()
})

lineView.addEventListener("click", function() {
	lineView.values = Array(8).fill().map(() => 10 * Math.random());
	lineView.redraw()
})
