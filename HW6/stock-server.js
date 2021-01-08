const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');
const https = require('https');
let app = express();
app.use(bodyParser.json());

const URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
const API_KEY = "SH5K04OEMU4XCSF3"
var symbol = ""

/*
	this will be used to get the symbol input from the user
*/
app.get('/input',(req,res) => {
	let symbol = req.query.symbol || null;
	
	if(!symbol){
		res.send('Must have a symbol');
	}
	else{
		path = URL + symbol + "&apikey=" + API_KEY;
		https.get(path, (resp) => {
			let data = '';

			// A chunk of data has been received.
			resp.on('data', (chunk) => {
				data += chunk;
			});

			// The whole response has been received. Print out the result.
			resp.on('end', () => {
				console.log(JSON.parse(data));
			});

		});
	}
	
	res.send("ok");
});

app.listen(8080,() => {
	console.log('Listening on port 8080!');
});