const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');
let app = express();
app.use(bodyParser.json());

app.get('/tasks'.(rew,res) => {
	let jsonData = readJson();
	res.send(jsonData);
});

app.get('/tasks/new'.(rew,res) => {
	let jsonData = readJson();
	let id = req.query.id || "NaN";
    let taskDescription = req.query.task || "None";
	
	if(isNaN(id)){
		res.send('Task must have an ID');
	}
	else if(jsonData.hasOwnProperty(id)){
		res.send('Task ID is already taken, please choose a different ID');
	}
	else{
		jsonData[id] = taskDescription;
		writeJson(jsonData);
	}
	
	res.send('Task added to json file.");
});

app.get('/tasks/remove?id=1'.(rew,res) => {
	let jsonData = readJson();
	let id = req..query.id;
	
	res.send('Hello World!");
});
	

app.listen(3000,() => {
	console.log('Example app listening on port 3000!');
});

function readJson(){
	let rawData = fs.readFileSync('./tasksFile.json');
	let jsonData = JSON.parse(rawData);
	
	return jsonData;
}

function writeJson(newTaskDict){
	let data = JSON.stringify(newTaskDict, null, '\t');
	fs.writeFileSync('./taskFile.json', data);
}

