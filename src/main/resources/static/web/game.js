var oneGame = new Vue({
    el: '#oneGame',
    data: {
        oneGameData: [],
        gamePlayerId: null,
        loading: true,
        row:["","1","2","3","4","5","6","7","8"],
        column:["A","B","C","D","E","F","G","H"]
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
                    this.oneGameData = json;
                    this.loading = false;
                    console.log(this.oneGameData);
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
        }
    }
})