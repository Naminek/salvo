var oneGame = new Vue({
	el: '#oneGame',
	data: {
		oneGameData: [],
		gamePlayers: [],
		gamePlayerId: null,
		loading: true,
		rowNumber: ["", "1", "2", "3", "4", "5", "6", "7", "8"],
		columnLetter: ["A", "B", "C", "D", "E", "F", "G", "H"],
		allCellArray: [],
		locationArray: [],
		viewingPlayerId: null,
		viewingPlayer: null,
		opponentPlayerId: null,
		opponentPlayer: null,
		myShipsArr: [],
		showLogout: true,
		chosenShip: null,
		showRadio: false,
		checkShipDirection: null,
		selectedCell: null,
		shipLength: null,
		aircraftButton: true,
		battleshipButton: true,
		destroyerButton: true,
		submarineButton: true,
		patrolButton: true,
		cellNumber: null,
		cellAlpha: null,
		allShips: [],
		oneShip: {
			shipType: "",
			locations: []
		},
		oneShipLocations: []
	},
	created() {
		this.makeTable();
	},
	mounted() {
		this.getUrl(),
			this.loadOneGame()
	},
	methods: {
		loadOneGame() {
			fetch("http://localhost:8080/api/game_view/" + this.gamePlayerId, {
					method: "GET"
				})
				.then(response => response.json())
				.then(json => {
					this.oneGameData = json;
					this.gamePlayers = json.gamePlayers;
					this.loading = false;
					console.log(this.oneGameData);
					console.log(this.gamePlayers);
					// this.getDate();
					this.showPlayers();
					this.markShips();
					this.markSalvo();
					// this.writeTurn();
				})
				.catch(function (error) {
					console.log(error);
				});
		},
		// getDate() {
		// 	this.oneGameData.map(game => game.created = new Date(game.created).toLocaleString());
		// },
		getUrl() {
			var splitUrl = window.location.href.split('?');
			var splitUrl2 = splitUrl[1].split('=');
			this.gamePlayerId = splitUrl2[1];
		},
		makeTable() {
			for (var i = 0; i < this.columnLetter.length; i++) {
				for (var j = 0; j < this.rowNumber.length; j++) {
					this.allCellArray.push(this.columnLetter[i] + this.rowNumber[j]);
				}
			}
			for (var k = 0; k < Math.ceil(this.allCellArray.length / 9); k++) {
				var startSlice = k * 9;
				var smallArray = this.allCellArray.slice(startSlice, startSlice + 9);
				this.locationArray.push(smallArray);
			}
		},
		showPlayers() {
			for (var i = 0; i < this.oneGameData.gamePlayers.length; i++) {
				if (this.gamePlayerId == this.oneGameData.gamePlayers[i].gamePlayerId) {
					this.viewingPlayer = this.oneGameData.gamePlayers[i].player.email;
					this.viewingPlayerId = this.oneGameData.gamePlayers[i].gamePlayerId;
				} else {
					this.opponentPlayer = this.oneGameData.gamePlayers[i].player.email;
					this.opponentPlayerId = this.oneGameData.gamePlayers[i].gamePlayerId;
				}
			}
		},
		markShips() {
			this.oneGameData.ships.forEach(ship => ship.locations.forEach(loc =>
				this.myShipsArr.push(loc)
			))
			console.log(this.myShipsArr)
			this.myShipsArr.forEach(loc => {
				document.querySelector(`#${loc}`).classList.add("shipLocation");
				// document.querySelector(`#${loc}`).innerHTML = '<div class="shipLocation"></div>';
			})
		},
		markSalvo() {
			for (var i = 0; i < this.oneGameData.salvoes.length; i++) {
				if (this.opponentPlayerId == this.oneGameData.salvoes[i].gamePlayerId) {
					for (var j = 0; j < this.oneGameData.salvoes[i].locations.length; j++) {
						if (this.myShipsArr.includes(this.oneGameData.salvoes[i].locations[j])) {
							// document.querySelector(`#${this.oneGameData.salvoes[i].locations[j]}`).classList.add("hitShip");
							document.querySelector(`#${this.oneGameData.salvoes[i].locations[j]}`).innerHTML = '<div class="hitShip">' + this.oneGameData.salvoes[i].turn + '</div>';
						} else {
							// document.querySelector(`#${this.oneGameData.salvoes[i].locations[j]}`).classList.add("salvoLocation");
							document.querySelector(`#${this.oneGameData.salvoes[i].locations[j]}`).innerHTML = '<div class="salvoLocation">' + this.oneGameData.salvoes[i].turn + '</div>';
						}
					}
				} else if (this.viewingPlayerId == this.oneGameData.salvoes[i].gamePlayerId) {
					for (var k = 0; k < this.oneGameData.salvoes[i].locations.length; k++) {
						// document.querySelector(`#salvo${this.oneGameData.salvoes[i].locations[k]}`).classList.add("salvoLocation");
						document.querySelector(`#salvo${this.oneGameData.salvoes[i].locations[k]}`).innerHTML = '<div class="salvoLocation">' + this.oneGameData.salvoes[i].turn + '</div>';
					}
				}
			}
		},
		// writeTurn() {
		// 	for (var i = 0; i < this.oneGameData.salvoes.length; i++) {
		// 		if (this.opponentPlayerId == this.oneGameData.salvoes[i].gamePlayerId) {
		// 			this.oneGameData.salvoes[i].locations.forEach(loc => {
		// 				document.querySelector(`#${loc}`).innerHTML = this.oneGameData.salvoes[i].turn;
		// 			})
		// 		} else {
		// 			this.oneGameData.salvoes[i].locations.forEach(loc => {
		// 				document.querySelector(`#salvo${loc}`).innerHTML = this.oneGameData.salvoes[i].turn;
		// 			})
		// 		}
		// 	}
		// },
		loseUser() {
			this.showLogout = false;
			fetch("/api/logout", {
					method: "POST"
				})
				.then(function (data) {
					console.log('Request success: ', data);
					window.location.reload();
					// this.player = null;
				})
				.catch(function (error) {
					console.log('Request failure: ', error);
				});
		},
		chooseShip(ev) {
			this.chosenShip = ev.target.value;
			console.log(this.chosenShip);
			if (this.checkShipDirection == null) {
				document.getElementById("showMessage").innerHTML = "Please choose horizontal or vertical"
			} else {
				document.getElementById("showMessage").innerHTML = "Please choose a place to set the stern"
			}
			this.showRadio = true;
			this.oneShip.shipType = this.chosenShip;
			console.log(this.oneShip);
		},
		decideDirection(ev) {
			console.log(ev.target.value);
			this.checkShipDirection = ev.target.value;
			document.getElementById("showMessage").innerHTML = "Please choose a place to set the stern";
		},
		placeShips() {
			fetch("/api/games/players/" + this.gamePlayerId + "/ships", {
					credentials: 'include',
					method: "POST",
					// body: `shipType=${ this.addEmail }&locations=${ this.addPassword }`,
					headers: {
						'Accept': 'application/json',
						'Content-Type': 'application/json'
					},
					body: JSON.stringify([{
							"shipType": "destroyer",
							"locations": ["A1", "B1", "C1"]
						},
						{
							"shipType": "patrol boat",
							"locations": ["H5", "H6"]
						}
					])
				})
				.then(function (data) {
					console.log('Request success: ', data);
					window.location.reload();
				})
				.catch(function (error) {
					console.log('Request failure: ', error);
					alert("Failure");
				});
		},
		setShip(location) {

			if (this.chosenShip == null) {
				alert("Please choose a ship!")
			} else if (this.checkShipDirection == null) {
				alert("Please choose a direction!")
			} else {
				if (this.chosenShip != null && this.checkShipDirection != null) {
					if (this.chosenShip == "aircraft carrier") {
						this.shipLength = 5;
					} else if (this.chosenShip == "battleship") {
						this.shipLength = 4;
					} else if (this.chosenShip == "patrol boat") {
						this.shipLength = 2;
					} else {
						this.shipLength = 3;
					}
					this.shipFunction(location, this.shipLength)
				}
			}
		},
		shipFunction(location, shipLength) {
			this.selectedCell = location;
			this.cellNumber = this.selectedCell.substr(1, 1);
			this.cellAlpha = this.selectedCell.substr(0, 1);
			if (this.checkShipDirection == "horizontal") {
				var selectedNumbers = [];
				for (var i = 0; i < shipLength; i++) {
					selectedNumbers.push(Number(this.cellNumber) + i);
				}
				if (selectedNumbers[selectedNumbers.length - 1] < 9) {
					selectedNumbers.forEach(number => {
						console.log("#" + this.cellAlpha + parseInt(number));
						document.querySelector("#" + this.cellAlpha + parseInt(number)).classList.add("chosenLocation");
						this.oneShipLocations.push(this.cellAlpha + parseInt(number));
					});
				} else {
					// for(var j = 0; j < selectedNumbers; j++) {

					// }
					var newSelectedNumbers = selectedNumbers.filter(num => num < 9);
					newSelectedNumbers.forEach(num => {
						document.querySelector("#" + this.cellAlpha + parseInt(num)).classList.add("noLocation");
					})
					
				}
			}
		},
		removeHover() {
			if (this.chosenShip != null && this.checkShipDirection != null) {
				this.oneShipLocations.forEach(oneCell => {
					document.querySelector("#" + oneCell).classList.remove("chosenLocation");
				});
			}
			this.oneShipLocations = [];
		}
	}
})