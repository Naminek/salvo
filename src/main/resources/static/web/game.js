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
		salvoArray:[],
		viewingPlayerId: null,
		viewingPlayer: null,
		oponentPlayerId: null,
		oponentPlayer: null
	},
	created() {
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
					this.loading = false;
					this.oneGameData = json;
					this.gamePlayers = json.gamePlayers;

					console.log(this.oneGameData);
					console.log(this.gamePlayers);
					// this.getDate();
					this.showPlayers();
					this.makeTable();
					this.test();

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
			// if (splitUrl.length == 2) {
			var splitUrl2 = splitUrl[1].split('=');
			// if (splitUrl2.length == 2) {
			this.gamePlayerId = splitUrl2[1];
			// }
			// }
			// console.log(this.gamePlayerId);
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
				this.salvoArray.push(smallArray);
			}
			console.log(this.salvoArray);

		},
		checkLocation(location) {
			for (var i = 0; i < this.oneGameData.ships.length; i++) {
				if (this.oneGameData.ships[i].locations.includes(location)) {
					// console.log(this.oneGameData.ships[i].locations);
					return true;
				}
			}
		},
		checkMySalvoLocation(salvo) {
			for (var i = 0; i < this.oneGameData.salvoes.length; i++) {
				if (this.viewingPlayerId == this.oneGameData.salvoes[i].gamePlayerId 
					&& this.oneGameData.salvoes[i].locations.includes(salvo)) {
					return true;
				}
			}
		},
		checkOponentSalvoLocation(location) {
			for (var i = 0; i < this.oneGameData.ships.length; i++) {
				if (this.oneGameData.ships[i].locations.includes(location)) {
					// console.log(this.oneGameData.ships[i].locations);
					return false;
				} else {
					for (var i = 0; i < this.oneGameData.salvoes.length; i++) {
						if (this.oponentPlayerId == this.oneGameData.salvoes[i].gamePlayerId && this.oneGameData.salvoes[i].locations.includes(location)) {
							return true;
						}
					}
				}
			}
			
		},
		hitShip(location) {
			for (var i = 0; i < this.oneGameData.ships.length; i++) {
				if (this.oneGameData.ships[i].locations.includes(location)) {
					for (var i = 0; i < this.oneGameData.salvoes.length; i++) {
						if (this.oponentPlayerId == this.oneGameData.salvoes[i].gamePlayerId && this.oneGameData.salvoes[i].locations.includes(location)) {
							return true;
						}
					}
				}
			}

		},
		showPlayers() {
			if (this.gamePlayerId == this.oneGameData.gamePlayers[0].player.playerId) {
				this.viewingPlayer = this.oneGameData.gamePlayers[0].player.email;
				this.viewingPlayerId = this.oneGameData.gamePlayers[0].player.playerId;
				this.oponentPlayer = this.oneGameData.gamePlayers[1].player.email;
				this.oponentPlayerId = this.oneGameData.gamePlayers[1].player.playerId;
				console.log(this.oneGameData.gamePlayers[i].player.email);
			} else {
				this.viewingPlayer = this.oneGameData.gamePlayers[1].player.email;
				this.viewingPlayerId = this.oneGameData.gamePlayers[1].player.playerId;
				this.oponentPlayer = this.oneGameData.gamePlayers[0].player.email;
				this.oponentPlayerId = this.oneGameData.gamePlayers[0].player.playerId;
			}
		},
		test() {
			console.log(document.querySelectorAll(".normal"));
		}
	}
})