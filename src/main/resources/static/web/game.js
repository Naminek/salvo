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
		results: []
	},
	created() {
		this.makeTable();
	},
	mounted() {
		this.getUrl(),
			this.loadOneGame()
			this.loadResults()
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
		loadResults() {
            fetch("http://localhost:8080/api/leaderboard", {
                    method: "GET"
                })
                .then(response => response.json())
                .then(json => {
                    this.results = json;
                    this.loading = false;
                    console.log(this.results);
                    // gameData.getDate();
                    this.addResults();
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
		addResults() {

            for (var i = 0; i < this.results.length; i++) {
                if (this.results[i].scores.length < 1) {
                    var oneTotalScore = "No Game Data";
                    var numberOfWin = "-";
                    var numberOfTie = "-";
                    var numberOfLoss = "-";
                } else {
                    var oneTotalScore = 0;
                    var numberOfWin = 0;
                    var numberOfLoss = 0;
                    var numberOfTie = 0;
                    for (var j = 0; j < this.results[i].scores.length; j++) {
                        oneTotalScore += this.results[i].scores[j];
                        if (this.results[i].scores[j] == 1.0) {
                            numberOfWin++;
                        } else if (this.results[i].scores[j] == 0.5) {
                            numberOfTie++;
                        } else if (this.results[i].scores[j] == 0) {
                            numberOfLoss++;
                        }
                    }
                }
                this.results[i]["totalScore"] = oneTotalScore;
                this.results[i]["wins"] = numberOfWin;
                this.results[i]["losses"] = numberOfLoss;
                this.results[i]["ties"] = numberOfTie;
            }
            console.log(this.results)
        }
	}
})