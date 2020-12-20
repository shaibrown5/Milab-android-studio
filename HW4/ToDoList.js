const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');
let app = express();
app.use(bodyParser.json());

app.get('/tasks',(req,res) => {
	let jsonData = readJson();
	res.send(jsonData);
});

app.get('/tasks/new',(req,res) => {
	let jsonData = readJson();
	let id = req.query.id || "NaN";
    let taskDescription = req.query.task || "None";
	
	if(isNaN(id)){
		res.send('Task must have an numbered ID');
	}
	else if(jsonData.hasOwnProperty(id)){
		res.send('Task ID is already taken, please choose a different ID');
	}
	else{
		jsonData[id] = taskDescription;
		writeJson(jsonData);
	}
	
	console.log('added task with id ' + id);
	res.redirect('/tasks');
});

app.get('/tasks/remove',(req,res) => {
	let jsonData = readJson();
	let id = req.query.id || NaN;
	
	if(isNaN(id)){
		res.send('Task must have an numbered ID');
	}
	else if(jsonData.hasOwnProperty(id){
		delete jsonData[id];
		writeJson(jsonData);
		console.log('deleted task with id ' + id);
	}
	else{
		res.send('no task with id ' + id + 'exists');
	}		
	
	res.redirect('/tasks');
});
	

app.listen(8080,() => {
	console.log('Listening on port 8080!');
});

function readJson(){
	let rawData = fs.readFileSync('./tasksFile.json', (err) => {
        if (err){
            console.log("Reading file info failed");
            throw err;
        });
	let jsonData = JSON.parse(rawData);
	
	return jsonData;
}

function writeJson(newTaskDict){
	let data = JSON.stringify(newTaskDict, null, 2);
	fs.writeFileSync('./tasksFile.json', data (err) => {
        if (err) {
            console.log("Error writing to file", err);
            throw err;
        }
	});
}

