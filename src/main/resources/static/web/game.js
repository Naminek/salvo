var oneGame = new Vue({
    el: '#oneGame',
    data: {
        oneGame: [],
        gamePlayerId: null,
        loading: true
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
                    this.oneGame = json;
                    this.loading = false;
                    console.log(this.oneGame);
                    gameData.getDate();
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        getDate() {
            this.games.map(game => game.created = new Date(game.created).toLocaleString());
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
        }
    }
})