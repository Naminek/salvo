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
		placeShipsButton: false,
		cellNumber: null,
		cellAlpha: null,
		allShips: [],
		allLocationsArray: [],
		aircraft: {
			shipType: "",
			locations: []
		},
		battleship: {
			shipType: "",
			locations: []
		},
		destroyer: {
			shipType: "",
			locations: []
		},
		submarine: {
			shipType: "",
			locations: []
		},
		patrol: {
			shipType: "",
			locations: []
		},
		oneShipLocations: [],
		badOneShipLocations: [],
		salvoLocationsArray: [],
		salvosAreChosen: false,
		showTurnNum: false,
		currentTurn: null,
		showingSalvoLocations: "",
		hitResults: null,
		myAttacks: [],
		opponentAttacks: [],
		arrayToShowTable: [],
		attackingPlayer: null,
		myFinishingStatus: null,
		opponentFinishingStatus: null,
		opponentsTurn: false,
		myTurn: false,
		interval: "",
		finishMessage: "",
		game_is_over: false,
		allMySalvos: [],
		mySalvo: []
	},
	created() {
		this.makeTable()
	},
	mounted() {
		this.getUrl();
		this.loadOneGame();
		// this.startInterval();


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
					this.hitResults = json.hitResults;
					this.loading = false;
					console.log(this.oneGameData);
					console.log(this.gamePlayers);
					// this.getDate();
					this.showPlayers();
					this.markShips();
					this.markSalvo();
					// this.writeTurn();
					this.checkShipAndSalvoData();
					this.showAttackResults();
					this.markHitSalvo();
					this.checkSink();
					// this.showAttackingPlayer();
					this.alertWinner();
					this.startInterval();
					this.stopInterval();
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
			// console.log(this.myShipsArr)
			this.myShipsArr.forEach(loc => {
				document.querySelector(`#${loc}`).classList.add("shipLocation");
				// document.querySelector(`#${loc}`).innerHTML = '<div class="shipLocation"></div>';
			})
		},
		markSalvo() {
			for (var i = 0; i < this.oneGameData.salvos.length; i++) {
				if (this.opponentPlayerId == this.oneGameData.salvos[i].gamePlayerId) {
					for (var j = 0; j < this.oneGameData.salvos[i].locations.length; j++) {
						if (this.myShipsArr.includes(this.oneGameData.salvos[i].locations[j])) {
							// document.querySelector(`#${this.oneGameData.salvos[i].locations[j]}`).classList.add("hitShip");
							document.querySelector(`#${this.oneGameData.salvos[i].locations[j]}`).innerHTML = '<div class="hitShip">' + this.oneGameData.salvos[i].turn + '</div>';
						} else {
							// document.querySelector(`#${this.oneGameData.salvos[i].locations[j]}`).classList.add("salvoLocation");
							document.querySelector(`#${this.oneGameData.salvos[i].locations[j]}`).innerHTML = '<div class="salvoLocation">' + this.oneGameData.salvos[i].turn + '</div>';
						}
					}
				} else if (this.viewingPlayerId == this.oneGameData.salvos[i].gamePlayerId) {
					for (var k = 0; k < this.oneGameData.salvos[i].locations.length; k++) {
						// document.querySelector(`#salvo${this.oneGameData.salvos[i].locations[k]}`).classList.add("salvoLocation");
						document.querySelector(`#salvo${this.oneGameData.salvos[i].locations[k]}`).innerHTML = '<div class="salvoLocation">' + this.oneGameData.salvos[i].turn + '</div>';
					}
				}
			}
		},
		markHitSalvo() {

			this.hitResults.forEach(result => {
				let myHitSalvo = [];
				if (result.gamePlayerId == this.viewingPlayerId) {
					for (var i = 0; i < result.attack.length; i++) {
						myHitSalvo = result.attack[i].hits.map(hit => hit.hitPlace);
						console.log(myHitSalvo);
						myHitSalvo.forEach(salvo => {
							document.querySelector("#salvo" + salvo).classList.add("shipLocation");
						})

					}
				}

			});

		},
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
			this.checkShipDirection = "horizontal"
			// console.log(this.chosenShip);
			// if (this.checkShipDirection == null) {
			// 	document.getElementById("showMessage").innerHTML = "Please choose horizontal or vertical"
			// } else {
			document.getElementById("showMessage").innerHTML = "Please choose a place to set a ship"
			// }
			this.showRadio = true;
		},
		decideDirection(ev) {
			// console.log(ev.target.value);
			this.checkShipDirection = ev.target.value;
			document.getElementById("showMessage").innerHTML = "Please choose a place to set a ship";
		},
		placeShips() {
			fetch("/api/games/players/" + this.gamePlayerId + "/ships", {
					credentials: 'include',
					method: "POST",
					headers: {
						'Accept': 'application/json',
						'Content-Type': 'application/json'
					},
					body: JSON.stringify(this.allShips)
				})
				.then(function (data) {
					console.log('Request success: ', data);

					if (data.status == 201) {
						window.location.reload();
						alert("You placed all ships!!");
					}
					if (data.status == 403) {
						window.location.reload();
						alert("You can't place more than 5 ships");
					}
				})
				.catch(function (error) {
					console.log('Request failure: ', error);
					alert("Failure");
				});

		},
		gridHover(location) {
			if (this.chosenShip != "" && this.checkShipDirection != null) {
				var oneShip = {};
				if (this.chosenShip == "aircraft carrier") {
					this.shipLength = 5;
					oneShip = this.aircraft;
				} else if (this.chosenShip == "battleship") {
					this.shipLength = 4;
					oneShip = this.battleship;
				} else if (this.chosenShip == "patrol boat") {
					this.shipLength = 2;
					oneShip = this.patrol;
				} else if (this.chosenShip == "destroyer") {
					this.shipLength = 3;
					oneShip = this.destroyer;
				} else if (this.chosenShip == "submarine") {
					this.shipLength = 3;
					oneShip = this.submarine;
				}
				this.showShadow(location, this.shipLength, oneShip)

			}
		},
		showShadow(location, shipLength, oneShip) {
			this.selectedCell = location;
			this.cellNumber = this.selectedCell.substr(1, 1);
			this.cellAlpha = this.selectedCell.substr(0, 1);
			if (this.checkShipDirection == "horizontal") {
				let selectedNumbers = [];
				for (var i = 0; i < shipLength; i++) {
					selectedNumbers.push(Number(this.cellNumber) + i);
				}
				// console.log(selectedNumbers)
				if (selectedNumbers[selectedNumbers.length - 1] < 9) {
					selectedNumbers.forEach(number => {
						document.querySelector("#" + this.cellAlpha + parseInt(number)).classList.add("chosenLocation");
						this.oneShipLocations.push(this.cellAlpha + parseInt(number));
						if (this.checkLocations() == false) {
							this.oneShipLocations.forEach(loc => {
								document.querySelector("#" + loc).classList.add("noLocation");
							});
						}
					});
					// console.log(this.oneShipLocations)
					oneShip.locations = this.oneShipLocations;
					oneShip.shipType = this.chosenShip;
				} else {
					let badSelectedNumbers = selectedNumbers.filter(num => num < 9);
					badSelectedNumbers.forEach(num => {
						document.querySelector("#" + this.cellAlpha + parseInt(num)).classList.add("noLocation");
						this.badOneShipLocations.push(this.cellAlpha + parseInt(num));
					});
					// console.log(this.badOneShipLocations);
				}
			} else if (this.checkShipDirection == "vertical") {
				let cellAlphaNum = this.cellAlpha.charCodeAt(0);
				// console.log(cellAlphaNum);
				let selectedAlphaNums = [];
				for (var j = 0; j < shipLength; j++) {
					selectedAlphaNums.unshift(cellAlphaNum - j);
				}
				// console.log(selectedAlphaNums);
				if (selectedAlphaNums[0] > 64) {
					selectedAlphaNums.forEach(alphaNum => {
						document.querySelector("#" + String.fromCharCode(alphaNum) + this.cellNumber).classList.add("chosenLocation");
						this.oneShipLocations.push(String.fromCharCode(alphaNum) + this.cellNumber);
						if (this.checkLocations() == false) {
							this.oneShipLocations.forEach(loc => {
								document.querySelector("#" + loc).classList.add("noLocation");
							});
						}
					});
					// console.log(this.oneShipLocations);
					oneShip.locations = this.oneShipLocations;
					oneShip.shipType = this.chosenShip;
				} else {
					let badSelectedAlphaNums = selectedAlphaNums.filter(num => num > 64);
					badSelectedAlphaNums.forEach(alphaNum => {
						document.querySelector("#" + String.fromCharCode(alphaNum) + this.cellNumber).classList.add("noLocation");
						this.badOneShipLocations.push(String.fromCharCode(alphaNum) + this.cellNumber);
					});
				}
			}
		},
		removeHover() {
			this.badOneShipLocations.forEach(oneCell => {
				document.querySelector("#" + oneCell).classList.remove("noLocation");
			});
			this.badOneShipLocations = [];
			this.oneShipLocations.forEach(oneCell => {
				document.querySelector("#" + oneCell).classList.remove("noLocation");
				document.querySelector("#" + oneCell).classList.remove("chosenLocation");
			});
			this.oneShipLocations = [];
		},
		pushShip(loc) {
			if (this.oneGameData.ships.length == 0) {
				if (this.checkShipDirection == null && this.allShips.length == 0) {
					alert("please choose a ship and orientation!")
				} else if (this.checkShipDirection == null && this.allShips.length > 0) {
					this.replaceShip(loc);
				} else {
					if (this.oneShipLocations.length == this.shipLength) {
						// console.log(this.checkLocations());
						if (this.checkLocations() == false) {
							alert("already ship located")
						} else {
							if (this.chosenShip == "aircraft carrier") {
								this.allShips.push(this.aircraft);
								document.getElementById("aircraft").disabled = true;
							} else if (this.chosenShip == "battleship") {
								this.allShips.push(this.battleship);
								document.getElementById("battleship").disabled = true;
							} else if (this.chosenShip == "patrol boat") {
								this.allShips.push(this.patrol);
								document.getElementById("patrol").disabled = true;
							} else if (this.chosenShip == "destroyer") {
								this.allShips.push(this.destroyer);
								document.getElementById("destroyer").disabled = true;
							} else if (this.chosenShip == "submarine") {
								this.allShips.push(this.submarine);
								document.getElementById("submarine").disabled = true;
							}
							this.allShips.map(ship => ship.locations.forEach(cell => {
								document.querySelector("#" + cell).classList.remove("chosenLocation");
								document.querySelector("#" + cell).classList.add("shipLocation");
							}));
							this.chosenShip = "";
							this.checkShipDirection = null;
							this.showRadio = false;
							// this.selectedCell = null;
							console.log(this.chosenShip);
							console.log(this.allShips);
							document.getElementById("showMessage").innerHTML = "Please choose next ship"
							if (this.allShips.length == 5) {
								this.placeShipsButton = true;
								document.getElementById("showMessage").innerHTML = 'Please push "Place ships" button!'
							}
						}
					} else {
						alert("wrong place")
					}
				}
			}
		},
		checkLocations() {
			if (this.allShips.length == 0) {
				return true;
			} else {
				this.allLocationsArray = [].concat.apply([], this.allShips.map(ship => ship.locations));
				// console.log(this.allLocationsArray);
				// console.log(this.oneShipLocations);
				for (var i = 0; i < this.oneShipLocations.length; i++) {
					if (this.allLocationsArray.includes(this.oneShipLocations[i])) {
						return false;
					}
				}
				return true;
			}
		},
		replaceShip(loc) {
			this.selectedCell = loc
			for (var i = 0; i < this.allShips.length; i++) {
				if (this.allShips[i].locations.includes(this.selectedCell)) {
					this.chosenShip = this.allShips[i].shipType;
					this.showRadio = true;
					this.allShips[i].locations.forEach(location => {
						document.querySelector("#" + location).classList.remove("shipLocation");
					});
					var tempoLocations = this.allShips[i].locations;
					var checkCellNum = tempoLocations.map(loc => loc.substr(1, 1));

					this.allShips.splice(this.allShips.indexOf(this.allShips[i]), 1);
					// console.log(checkCellNum);
					if (checkCellNum[0] == checkCellNum[1]) {
						this.checkShipDirection = "vertical";
						// console.log()
						this.gridHover(tempoLocations[tempoLocations.length - 1]);
					} else {
						this.checkShipDirection = "horizontal";
						this.gridHover(tempoLocations[0]);
					}

					if (this.chosenShip == "aircraft carrier") {
						document.getElementById("aircraft").disabled = false;
					} else if (this.chosenShip == "battleship") {
						document.getElementById("battleship").disabled = false;
					} else if (this.chosenShip == "patrol boat") {
						document.getElementById("patrol").disabled = false;
					} else if (this.chosenShip == "destroyer") {
						document.getElementById("destroyer").disabled = false;
					} else if (this.chosenShip == "submarine") {
						document.getElementById("submarine").disabled = false;
					}
				}
			}
			document.getElementById("showMessage").innerHTML = "Please choose a place to set a ship"
			// console.log(this.allShips);
			// console.log(this.chosenShip);
		},
		checkShipAndSalvoData() {
			if (this.oneGameData.ships.length > 0) {
				document.getElementById("aircraft").disabled = true;
				document.getElementById("battleship").disabled = true;
				document.getElementById("patrol").disabled = true;
				document.getElementById("destroyer").disabled = true;
				document.getElementById("submarine").disabled = true;
				document.getElementById("showMessage").innerHTML = "Your ships are placed";

				let turnArray = [];
				this.oneGameData.salvos.forEach(salvo => {
					if (salvo.gamePlayerId == this.viewingPlayerId) {
						turnArray.push(salvo.turn);
					}
				});
				if (turnArray.length == 0) {
					this.currentTurn = 1
				} else {
					this.currentTurn = Math.max(...turnArray) + 1;
				}
				this.showTurnNum = true;
			}
		},
		salvoHover(salvoLoc) {
			if(this.oneGameData.ships.length > 0 && this.oneGameData.opponentShipsSet == true)
			if(this.allMySalvos.includes(salvoLoc)) {
				document.querySelector("#salvo" + salvoLoc).classList.remove("salvoHover");
			} else {
				document.querySelector("#salvo" + salvoLoc).classList.add("salvoHover");
			}
		},
		removeSalvoHover(salvoLoc) {
			document.querySelector("#salvo" + salvoLoc).classList.remove("salvoHover");
		},
		checkSalvo(salvoLoc) {
			if (this.oneGameData.ships.length > 0) {
				if (this.oneGameData.opponentPlayer == false) {
					alert("No opponent player");
				} else if (this.oneGameData.opponentShipsSet == false) {
					alert("Please wait until opponent player place ships");
					// } else if (this.oneGameData.lastTurn.myLastTurn > this.oneGameData.lastTurn.opponentLastTurn) {
					// 	alert("Please wait for opponent's attack")
				} else if (this.myFinishingStatus == true || this.opponentFinishingStatus == true) {
					alert("Game finished");
				} else {
					if (this.oneGameData.salvos.length > 0) {
						this.mySalvos = [];
						this.oneGameData.salvos.forEach(salvo => {
							if (salvo.gamePlayerId == this.viewingPlayerId) {
								this.mySalvos.push(salvo.locations);
							}
						});
						this.allMySalvos = [].concat.apply([], this.mySalvos);
						if (this.allMySalvos.includes(salvoLoc)) {
							alert("You already attack this place")
						} else {
							this.setSalvo(salvoLoc);
						}
					} else {
						this.setSalvo(salvoLoc);
					}
				}
			}
		},
		setSalvo(salvoLocation) {
			if (this.salvoLocationsArray.length != 0 && this.salvoLocationsArray.includes(salvoLocation)) {
				let index = this.salvoLocationsArray.findIndex(salvo => salvo == salvoLocation);
				this.salvoLocationsArray.splice(index, 1);
				document.querySelector("#salvo" + salvoLocation).classList.remove("salvoLocation");
				console.log(this.salvoLocationsArray);
				this.salvosAreChosen = false;
			} else {
				if (this.salvoLocationsArray.length == 5) {
					alert("You can't choose more than 5 locations")
				} else {
					this.salvoLocationsArray.push(salvoLocation);
					document.querySelector("#salvo" + salvoLocation).classList.add("salvoLocation");
					console.log(this.salvoLocationsArray);
				}
				if (this.salvoLocationsArray.length == 5) {
					this.salvosAreChosen = true;
				} 
			}
			this.showingSalvoLocations = this.salvoLocationsArray.join(",");
		},
		placeSalvos() {
			fetch("/api/games/players/" + this.gamePlayerId + "/salvos", {
					credentials: 'include',
					method: "POST",
					headers: {
						'Accept': 'application/json',
						'Content-Type': 'application/json'
					},
					body: JSON.stringify({
						turn: this.currentTurn,
						salvoLocations: this.salvoLocationsArray
					})
				})
				.then(function (data) {
					console.log('Request success: ', data);

					if (data.status == 201) {
						window.location.reload();
						alert("You attacked!!");
					}
					if (data.status == 403) {
						window.location.reload();
						alert("You already attacked in this turn");
					}
					if (data.status == 406) {
						window.location.reload();
						alert("Please wait until opponent Player attack")
					}
				})
				.catch(function (error) {
					console.log('Request failure: ', error);
					alert("Failure");
				});
		},
		showAttackResults() {
			for (var i = 0; i < this.hitResults.length; i++) {
				console.log(this.hitResults[i].gamePlayerId);
				for (var j = 0; j < this.hitResults[i].attack.length; j++) {
					let oneResult = {
						"turn": null,
						"hits": []
					};
					oneResult.turn = this.hitResults[i].attack[j].turn;
					// console.log(oneResult.turn);
					oneHitResult = this.hitResults[i].attack[j].hits.reduce((acc, it) => ({
						...acc,
						[it.hitShip]: (acc[it.hitShip] || 0) + 1
					}), {});
					// oneResult.hits = oneHitResult;
					let resultsKeys = Object.keys(oneHitResult);
					let resultsValue = Object.values(oneHitResult);
					for (var k = 0; k < resultsKeys.length; k++) {
						let combertOneResult = {
							"shipType": null,
							"damage": null
						}
						combertOneResult.shipType = resultsKeys[k];
						combertOneResult.damage = resultsValue[k];
						// console.log(combertOneResult);
						oneResult.hits.push(combertOneResult);
						// console.log(oneResult);
					}
					if (this.hitResults[i].gamePlayerId == this.viewingPlayerId) {
						this.myAttacks.push(oneResult);
						this.compareTurn(this.myAttacks);
					} else if (this.hitResults[i].gamePlayerId == this.opponentPlayerId) {
						this.opponentAttacks.push(oneResult);
						this.compareTurn(this.opponentAttacks);
					}
				}
			}
			let maxTurn = this.currentTurn - 1;
			for (var i = 0; i < maxTurn; i++) {
				let oneHitObject = {};
				oneHitObject.turn = i + 1;
				for (var j = 0; j < this.myAttacks.length; j++) {
					if (oneHitObject.turn == this.myAttacks[j].turn) {
						oneHitObject.myHits = this.myAttacks[j].hits;
					}
				}
				for (var k = 0; k < this.opponentAttacks.length; k++) {
					if (oneHitObject.turn == this.opponentAttacks[k].turn) {
						oneHitObject.opponentHits = this.opponentAttacks[k].hits;
					}
				}
				this.arrayToShowTable.push(oneHitObject);
				this.compareTurn(this.arrayToShowTable);
			}
			console.log(this.myAttacks);
			console.log(this.opponentAttacks);
			console.log(this.arrayToShowTable);

		},
		compareTurn(array) {
			function compare(a, b) {
				const turnA = a.turn;
				const turnB = b.turn;

				let comparison = 0;
				if (turnA > turnB) {
					comparison = 1;
				} else if (turnA < turnB) {
					comparison = -1;
				}
				return comparison;
			}
			array.sort(compare);
		},
		checkSink() {
			if (this.hitResults[0].attack.length > 0) {
				if (this.hitResults[0].attack[this.hitResults[0].attack.length - 1].isSunk.aircraftIsSunk == true) {
					document.getElementById("opponent_aircraft").disabled = true;
				}
				if (this.hitResults[0].attack[this.hitResults[0].attack.length - 1].isSunk.battleshipIsSunk == true) {
					document.getElementById("opponent_battleship").disabled = true;
				}
				if (this.hitResults[0].attack[this.hitResults[0].attack.length - 1].isSunk.destroyerIsSunk == true) {
					document.getElementById("opponent_destroyer").disabled = true;
				}
				if (this.hitResults[0].attack[this.hitResults[0].attack.length - 1].isSunk.submarineIsSunk == true) {
					document.getElementById("opponent_submarine").disabled = true;
				}
				if (this.hitResults[0].attack[this.hitResults[0].attack.length - 1].isSunk.patrolIsSunk == true) {
					document.getElementById("opponent_patrol").disabled = true;
				}
			}
		},
		
		alertWinner() {
			if (this.hitResults[0].attack.length > 0 && this.hitResults[1].attack.length > 0 && (this.oneGameData.lastTurn.myLastTurn == this.oneGameData.lastTurn.opponentLastTurn)) {
				if (this.hitResults[0].attack[this.hitResults[0].attack.length - 1].gameIsOver == true ||
					this.hitResults[1].attack[this.hitResults[1].attack.length - 1].gameIsOver == true) {
					this.myFinishingStatus = this.hitResults[0].attack[this.hitResults[0].attack.length - 1].gameIsOver;
					this.opponentFinishingStatus = this.hitResults[1].attack[this.hitResults[1].attack.length - 1].gameIsOver;
					console.log(this.myFinishingStatus);
					console.log(this.opponentFinishingStatus);

					if (this.myFinishingStatus == true && this.opponentFinishingStatus == true) {
						alert("GAME OVER!! Tied game");
						this.finishMessage = "GAME OVER: Tied game"
						this.game_is_over = true;
						this.showTurnNum = false;
						return true;
					} else if (this.myFinishingStatus == true && this.opponentFinishingStatus == false) {
						alert("GAME OVER!! You won!!");
						this.finishMessage = "GAME OVER: You won!"
						this.game_is_over = true;
						this.showTurnNum = false;
						return true;
					} else if (this.myFinishingStatus == false && this.opponentFinishingStatus == true) {
						alert("GAME OVER!! You lost...");
						this.finishMessage = "GAME OVER: You lost..."
						this.game_is_over = true;
						this.showTurnNum = false;
						return true;
					}
				} else {
					return false;
				}
			}
		},

		startInterval() {
			if (this.oneGameData.opponentShipsSet == true) {
				if ((this.oneGameData.lastTurn.myLastTurn != null && this.oneGameData.lastTurn.opponentLastTurn == null) ||
					this.oneGameData.lastTurn.myLastTurn > this.oneGameData.lastTurn.opponentLastTurn) {
					this.myTurn = false;
					this.opponentsTurn = true;

					this.interval = setInterval(() => {
						window.location.reload()
					}, 10000);
				} else {
					this.myTurn = true;
					this.opponentsTurn = false;

				}
			}
		},
		stopInterval() {
			if (this.alertWinner() == true) {
				clearInterval(this.interval);
			}
		}
	}
})