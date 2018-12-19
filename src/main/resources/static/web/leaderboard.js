var leaderBoard = new Vue({
    el: '#leader_board',
    data: {
        results: [],
        loading: true,
    },
    created() {
        this.loadResults()
    },
    methods: {
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
                    this.addData();
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        // getDate() {
        //     this.games.map(game => game.created = new Date(game.created).toLocaleString());
        // }
        addData() {

            for (var i = 0; i < this.results.length; i++) {
                var oneTotalScore = 0;
                var numberOfWin = 0;
                var numberOfLoss = 0;
                var numberOfTie = 0;
                for (var j = 0; j < this.results[i].scores.length; j++) {
                    oneTotalScore += this.results[i].scores[j];
                    if(this.results[i].scores[j] == 1.0){
                        numberOfWin++;
                    } else if(this.results[i].scores[j] == 0.5) {
                        numberOfTie++;
                    } else if(this.results[i].scores[j] == 0) {
                        numberOfLoss++;
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