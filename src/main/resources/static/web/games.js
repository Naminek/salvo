var gameData = new Vue({
    el: '#gameList',
    data: {
        games: [],
        results: [],
        loading: true,
        dataUrl: ["http://localhost:8080/api/games", "http://localhost:8080/api/leaderboard"],
        user: {
            "email": "",
            "password": ""
        },
        userEmail: "",
        userPassword: "",
        showForm: true,
        addEmail:"",
        addPassword: "",
        newUser: {
            "email": "",
            "password": ""
        }
    },
    created() {
        this.loadGames(this.dataUrl)
        // this.loadResults()
    },
    computed: {
        getUser() {
            console.log(document.form.email.value);
            console.log(document.form.password.value);
        }
    },
    methods: {
        loadGames(urlArray) {
            Promise.all(urlArray.map(url => fetch(url)
                    .then(response => response.json())))
                .then(json => {
                    console.log(json);
                    this.games = json[0].games;
                    this.results = json[1];
                    console.log(this.games);
                    console.log(this.results);
                    this.loading = false;
                    this.getDate();
                    this.addResults();
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
        getUser() {
            this.user.email = this.userEmail;
            this.user.password = this.userPassword;
            console.log(this.user);
            fetch("/api/login", {
                    credentials: 'include',
                    method: "POST",
                    // body: JSON.stringify(this.user),
                    body: `email=${ this.user.email }&password=${ this.user.password }`,
                    // body: JSON.stringify({"email": this.userEmail, "password": this.userPassword}),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                .then(function (data) {
                    console.log('Request success: ', data);
                })
                .catch(function (error) {
                    console.log('Request failure: ', error);
                });
                this.showForm = false;
        },
        addUser() {
            this.newUser.email = this.addEmail;
            this.newUser.password = this.addPassword;
            console.log(this.newUser);
            fetch("/api/player", {
                    credentials: 'include',
                    method: "POST",
                    body: `email=${ this.newUser.email }&password=${ this.newUser.password }`,
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                .then(function (data) {
                    console.log('Request success: ', data);
                })
                .catch(function (error) {
                    console.log('Request failure: ', error);
                });
                this.showForm = false;
        },
        loseUser() {
            this.showForm = true;
            fetch("/api/logout", {
                method: "POST"
            })
            .then(function (data) {
                console.log('Request success: ', data);
            })
            .catch(function (error) {
                console.log('Request failure: ', error);
            });
        }
    }
})