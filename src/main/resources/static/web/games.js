var gameData = new Vue({
    el: '#gameList',
    data: {
        games: [],
        results: [],
        loading: true,
        dataUrl: ["http://localhost:8080/api/games", "http://localhost:8080/api/leaderboard"],
        userEmail: "",
        userPassword: "",
        showForm: true,
        addEmail: "",
        addPassword: "",
        clickSignIn: true,
        viewingPlayer: null,
        playersEmail: false,
        viewingPlayerId: ""
    },
    created() {
        this.loadGames(this.dataUrl)
    },
    methods: {
        loadGames(urlArray) {
            Promise.all(urlArray.map(url => fetch(url)
                    .then(response => response.json())))
                .then(json => {
                    console.log(json);
                    this.viewingPlayer = json[0].player.email;
                    this.games = json[0].games;
                    this.results = json[1];
                    console.log(this.games);
                    console.log(this.results);
                    this.loading = false;
                    this.getDate();
                    this.addResults();
                    console.log(this.viewingPlayer);
                    this.showPlayer();
                    // this.showJoinButton();
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        getDate() {
            this.games.map(game => game.created = new Date(game.created).toLocaleString());
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
        },
        showPlayer() {
            if (this.viewingPlayer != null) {
                this.playersEmail = true;
            }
        },
        getUser() {
            fetch("/api/login", {
                    credentials: 'include',
                    method: "POST",
                    // body: JSON.stringify(this.user),
                    body: `email=${ this.userEmail }&password=${ this.userPassword }`,
                    // body: JSON.stringify({"email": this.userEmail, "password": this.userPassword}),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                .then(function (data) {
                    console.log('Request success: ', data);
                    if (data.status == 200) {
                        window.location.reload();
                        // gameData.showJoinButton();
                    } else if (data.status == 401) {
                        alert("User not found")
                    }
                })
                .catch(function (error) {
                    console.log('Request failure: ', error);
                });
        },
        addUser() {
            fetch("/api/players", {
                    credentials: 'include',
                    method: "POST",
                    body: `email=${ this.addEmail }&password=${ this.addPassword }`,
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                .then(function (data) {
                    console.log('Request success: ', data);
                    if (data.status == 201) {
                        gameData.userEmail = gameData.addEmail;
                        gameData.userPassword = gameData.addPassword;
                        gameData.getUser();
                    } else if (data.status == 403) {
                        alert("Please try again")
                    }
                })
                .catch(function (error) {
                    console.log('Request failure: ', error);
                });
        },
        loseUser() {
            this.showForm = true;
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
        showSignin() {
            this.clickSignIn = false;
        },
        hideSignin() {
            this.clickSignIn = true;
        },
        // showJoinButton() {
        //     for (var i = 0; i < this.games.length; i++) {
        //         for (var j = 0; j < this.games[i].gamePlayers.length; j++) {
        //             // console.log(this.viewingPlayer);
        //             // console.log(this.games[i].gamePlayers[j].name);
        //             console.log(this.games[i].id);
        //             console.log(`#showButton${this.games[i].id}`);
        //             console.log(document.querySelector(`#showButton${this.games[i].id}`));
        //             if (this.viewingPlayer != null && this.viewingPlayer == this.games[i].gamePlayers[j].name) {
        //                 document.querySelector(`#showButton${this.games[i].id}`).innerHTML = '<a href="/web/game.html?gp=' + this.game[i].id + '"><button>Join Game' + this.games[i].id + '</button></a>';

        //             }
        //         }

        //     }
        // },
        check(game){
            for(var i = 0; i < game.gamePlayers.length; i++) {
                if(game.gamePlayers[i]) {
                    if(game.gamePlayers[i].name == this.viewingPlayer) {
                        this.viewingPlayerId = game.gamePlayers[i].gpid;
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }
})