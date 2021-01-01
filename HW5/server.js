const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');
let app = express();
app.use(express.static('./files'));


app.get('/server/:dir/:fileName',(req,res) => {
	let dirName = req.params.dir || null;
	let fileName = req.params.fileName || null;
	
	// conditional statements to handle illegal urls
	if(!dirName){
		res.send("invalid dir name!");
	}
	else if(!fileName){
		res.send("No file was requested! please enter a file name");
	}
	
	// concat dir name and file name into a single path
	let path = "./" + dirName + "/" + fileName;
	
	// check that the path given 
	if(fs.existsSync(path)){
		//if open read stream and pipe it throught the res stream
		var readStream = fs.createReadStream(path);
		readStream.pipe(res);
	}
	else{
		res.send("Path does not exist! please check the name of the dir and the file");
	}
});	
	
app.listen(8080,() => {
	console.log('Listening on port 8080!');
});
