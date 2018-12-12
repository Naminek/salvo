var oneGame = new Vue({
	el: '#oneGame',
	data: {
		oneGameData: [],
		gamePlayers:[],
		gamePlayerId: null,
		loading: true,
		rowNumber: ["", "1", "2", "3", "4", "5", "6", "7", "8"],
		columnLetter: ["A", "B", "C", "D", "E", "F", "G", "H"],
		allCellArray: [],
		locationArray: [],
		viewingPlayer: null,
		oponentPlayer: null
	},
	created() {
		this.getUrl(),
			this.loadOneGame(),
			this.showPlayers(),
			this.makeTable()
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
					oneGame.getDate();
				})
				.catch(function (error) {
					console.log(error);
				});
		},
		getDate() {
			this.oneGameData.map(game => game.created = new Date(game.created).toLocaleString());
		},
		getUrl() {
			var splitUrl = window.location.href.split('?');
			// if (splitUrl.length == 2) {
			var splitUrl2 = splitUrl[1].split('=');
			// if (splitUrl2.length == 2) {
			this.gamePlayerId = splitUrl2[1];
			// }
			// }
			console.log(this.gamePlayerId);
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
			console.log(this.locationArray);

		},
		checkLocation(location) {
			for(var i = 0; i < this.oneGameData.ships.length; i++){
				if(this.oneGameData.ships[i].locations.includes(location)){
					return true;
				}
			}
		},
		showPlayers() {
			// for(var i = 0; i < this.oneGameData.gamePlayers.length; i++) {
			// 	if(this.gamePlayerId == this.oneGameData.gamePlayers[i].player.id) {
			// 		this.viewingPlayer = this.oneGameData.gamePlayers.player.email;
			// 	}
			// }
			console.log(this.gamePlayers.length);
		}
	}
})