var gameData = new Vue({
    el: '#gameList',
    data: {
        games: [],
        results: [],
        loading: true
    },
    created() {
        this.loadGames(),
        this.loadResults()
    },

    methods: {
        loadGames() {
            fetch("http://localhost:8080/api/games", {
                    method: "GET"
                })
                .then(response => response.json())
                .then(json => {
                    this.games = json;
                    console.log(this.games);
                    gameData.getDate();
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        getDate() {
            this.games.map(game => game.created = new Date(game.created).toLocaleString());
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