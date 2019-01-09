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
        viewingPlayerId: "",
        link: ""
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
        check(gamePlayers) {
            if(gamePlayers[0].name == this.viewingPlayer){
                this.link = `game.html?gp=${gamePlayers[0].gpid}`
                return true;
            }
            if(gamePlayers[1] && gamePlayers[1].name == this.viewingPlayer){
                this.link = `game.html?gp=${gamePlayers[1].gpid}`
                return true
            }
            return false
        }
        // check(game) {
        //     for (var i = 0; i < game.gamePlayers.length; i++) {
        //         if (game.gamePlayers[i] && this.viewingPlayer) {
        //             if (this.viewingPlayer == game.gamePlayers[i].name) {
        //                 this.viewingPlayerId = game.gamePlayers[i].gpid;
        //                 // this.link = `game.html?gp=${game.gamePlayers[i].gpid}`
        //                 return true;
        //             }
        //         } else {
        //             return false;
        //         }
        //     }
        // },
        // check(game) {
        //     if (game.gamePlayers[0] && this.viewingPlayer) {
        //         if (this.viewingPlayer == game.gamePlayers[0].name) {
        //             this.viewingPlayerId = game.gamePlayers[0].gpid;
        //             return true;
        //         } else if (game.gamePlayers[1] && this.viewingPlayer == game.gamePlayers[1].name) {
        //             this.viewingPlayerId = game.gamePlayers[1].gpid;
        //             return true;
        //         } else {
        //             return false;
        //         }
        //     } else {
        //         return false;
        //     }
        // }
    }
})