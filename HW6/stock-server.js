const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');
const request = require('request');
const FCM = require('fcm-push');
const serviceKey = 'AAAAKhsfFTI:APA91bHN1TEzNxsInwKu2QnSXPFIG27xW2_pErqtEEFghuYD4N5Uq6dh4jJKhqotYmkH2WgwKwKFU-3B70R4bQoIuMZkhG3w0_nRc22_ahZchpzLr02chXW7U3e4-zXSxbWNZIwevf1D';

let app = express();
let fcm = new FCM(serviceKey);
app.use(bodyParser.json());

const API_KEY = "SH5K04OEMU4XCSF3";
var symbol = "";
let isSending = false;

var tokens = {}; // saves the usernames: tokens -- should be in db

// registers token
app.post('/:user/token', (req, res, next) => {
	let token = req.body.token;

	if (!token) {
		console.log("missing token");
		return res.status(400).json({ err: "missing token" });
	}

	user = req.params.user;
	tokens.user = token;
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
		return res.status(400).json({ err: "missing symbol" })
	}
	console.log("the symbol is " + symbol);

	let user = req.params.user;
	let targetToken = tokens.user;
	if (!targetToken) {
		return res.status(404).json({ err: `no token for user ${req.params.user}` });
	}

	// Send request:
	isSending = true;
	sendStock(symbol, targetToken);

	//set interval
	interval = setInterval(() => sendStock(symbol, targetToken), 15000);
	return res.status(200).json({ msg: "msg sent successfully" });
});


/**
 * This function retrievs the value of the given symbol and send a notification to the user.
 *
 * @param {any} symbol - the symbol wanted by the user
 * @param {any} token- the users token
 */
function sendStock(symbol, token) {
	//api url
	let URL = `https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=${symbol}&apikey=${API_KEY}`;

	request(URL, (err, response, body) => {
		
		if (err) {
			return res.error(err);
        }
		else {
			let data = JSON.parse(body);
			// -1 will represent error
			let stockValue = -1;
			let title = symbol;

			// check that we got the response we were expecting
			if (data && data["Global Quote"]) {
				stockValue = data["Global Quote"]["05. price"];
			}

			let msgBody = stockValue;

			if (stockValue == -1) {
				title = "ERROR";
				msgBody = "ERROR"
			}

			// send the notification notification witht the tutle being the symbol and the body the value
			fcm.send(fcmMsg ={
				to: token,
				notification: {
					title: title,
					body: msgBody
				}
			}, (err, response) => {
				if (err) {
					return response.status(404).json({ err: `unable to send notification ${req.params.user}` });
				}
			});
		}
	});

	return;
}

app.listen(8080,() => {
	console.log('Listening on port 8080!');
});