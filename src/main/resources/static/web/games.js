var gameData = new Vue({
    el: '#gameList',
    data: {
        games: [],
        loading: true
    },
    created() {
        this.loadGames()
    },

    methods: {
        loadGames() {
            fetch("http://localhost:8080/api/games", {
                    method: "GET"
                })
                .then(response => response.json())
                .then(json => {
                    this.games = json;
                    this.loading = false;
                    console.log(this.games);
                    gameData.getDate();
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        getDate() {
            this.games.map(game => game.created = new Date(game.created).toLocaleString());
        }
    }
})