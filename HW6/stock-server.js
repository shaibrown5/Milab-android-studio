const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');
const https = require('https');
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


// Post request for fetching stock info:
app.post('/input', (req, res, next) => {
	// Reset interval (stop sending info for previous stock):
	if (isSending) { // True only if a request has been sent previously
		isSending = false;
		clearInterval(interval);
	}

	// getting symbol query
	symbol = req.body.stock;
	if(!symbol){
		return res.status(400).json({ err: "missing symbol" })
	}
	let token = req.body.token;
	if (!token) {
		return res.status(400).json({ err: "missing token" });
	}

	// Send request:
	isSending = true;
	let value = getStockInfo(symbol);
	sendStock(value, token);

	interval = setInterval(() => sendStockInfo(symbol, targetToken), 15000);
	return res.status(200).json({ msg: "msg sent successfully" });
});

/**
 * This function send the stock value over FCM to the user
 * @param {any} value - value of the stock
 * @param {any} token - token to send
 */
function sendStock(value, token) {
	let title = "Stock value:";
	let body = symbol + "; " + value;

	if (value == -1) {
		title = "ERROR";
		body = "ERROR"
    }

	// send the notification notification
	fcm.send(fcmMsg = {
		to: token,
		notification: {
			title: title,
			body: body
		}
	}, (err, response) => {
			if (err) {
				console.log("error sending the fcm message" + token);
		}
	});

	return;
}

/**
 * This method gets the stock info. returnds -1 if there was an error
 * @param {any} symbol - the smbol of the wated stock
 */
function getStockInfo(symbol) {
	let URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=${symbol}&apikey=${API_KEY}";
	// -1 will represent error for us
	let stockValue = -1;

	request(usrl, (err, response, body) => {
		if (err) {
			console.log('error occured', err);
			return res.error(err
        }
		else {
			let data = JSON.parse(body);

			// check that we got the response we were expecting
			if (data && data["Global Quote"]) {
				stockValue = stockValue["Global Quote"]["05. price"];
			}
		}
	});

	return stockValue;
}

app.listen(8080,() => {
	console.log('Listening on port 8080!');
});