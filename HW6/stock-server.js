const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');
const request = require('request');
const FCM = require('fcm-push');
const AlphaVantageAPI = require('alpha-vantage-cli').AlphaVantageAPI;
const serviceKey = 'AAAAKhsfFTI:APA91bHN1TEzNxsInwKu2QnSXPFIG27xW2_pErqtEEFghuYD4N5Uq6dh4jJKhqotYmkH2WgwKwKFU-3B70R4bQoIuMZkhG3w0_nRc22_ahZchpzLr02chXW7U3e4-zXSxbWNZIwevf1D';

let app = express();
let fcm = new FCM(serviceKey);
app.use(bodyParser.json());

const API_KEY = "SH5K04OEMU4XCSF3";
var symbol = "";
let isSending = false;

var tokens = {}; // this should probably be in a database

// save token
app.post('/:user/token', (req, res, next) => {
	let token = req.body.token;
	console.log(`Received save token request from ${req.params.user} for token=${token}`);

	if (!token) {
		console.log("missing token");
		return res.status(400).json({ err: "missing token" });
	}
	user = req.params.user;
	tokens.user = token;
	console.log("got token---------------");
	res.status(200).json({ msg: "saved ok" });
});

// Post request for fetching stock info:
app.post('/:user/input', (req, res, next) => {
	// Reset interval (stop sending info for previous stock):
	if (isSending) { // True only if a request has been sent previously
		isSending = false;
		clearInterval(interval);
	}

	// getting symbol query
	symbol = req.body.stock;
	if (!symbol) {
		console.log("error with symbol");
		return res.status(400).json({ err: "missing symbol" })
	}
	console.log("the symbol is " + symbol);

	let user = req.params.user;
	let targetToken = tokens.user;
	if (!targetToken) {
		console.log("error with token in /input ------");
		return res.status(404).json({ err: `no token for user ${req.params.user}` });
	}

	console.log("the symbol is " + symbol);

	// Send request:
	isSending = true;
	sendStock(symbol, targetToken);

	interval = setInterval(() => sendStock(symbol, targetToken), 15000);
	return res.status(200).json({ msg: "msg sent successfully" });
});


/**
 * 
 *
 * @param {any} symbol
 * @param {any} token
 */
function sendStock(symbol, token) {
	let URL = `https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=${symbol}&apikey=${API_KEY}`;
	

	console.log("in get stock info #############");
	console.log("the url is    " + URL);
	request(URL, (err, response, body) => {
		
		if (err) {
			console.log('error occured in get stock', err);
			return res.error(err);
        }
		else {
			console.log("the data body is " + body);
			let data = JSON.parse(body);
			// -1 will represent error for us
			let stockValue = -1;
			let title = symbol;

			// check that we got the response we were expecting
			if (data && data["Global Quote"]) {
				stockValue = data["Global Quote"]["05. price"];
				console.log("the stock value is " + stockValue);
			}

			let msgBody = stockValue;

			if (stockValue == -1) {
				title = "ERROR";
				msgBody = "ERROR"
			}
			console.log("the title is " + title);
			console.log("the msg is " + msgBody);

			// send the notification notification
			fcm.send(fcmMsg ={
				to: token,
				notification: {
					title: title,
					body: msgBody
				}
			}, (err, response) => {
				if (err) {
					console.log("error sending the fcm message" + token);
				}
			});
		}
	});

	return;
}

app.listen(8080,() => {
	console.log('Listening on port 8080!');
});