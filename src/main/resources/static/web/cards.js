let { createApp } = Vue

const options = {
    data() {
        return {
            response:[],
            cards : [],
            cardGold:"GOLD",
            cardTitanium:"PLATINUM",
            cardSilver:"SILVER",
            cardCredit:"CREDIT",
            cardDebit:"DEBIT",
            cardsLength: 0,

        }
    },

    created() {
            this.getCards();
        },
        

    methods: {
        getCards(){
            axios.get("http://localhost:8080/api/clients/current")
            .then(response => {
                    console.log(response);
                    this.cards = response.data.cards;
                    console.log(this.cards);
                    this.cards.sort((a, b) => a.id - b.id);
                    this.cardCredit = this.cards.filter((card => card.type === "CREDIT"))
                    console.log(this.cardCredit);
                    this.cardDebit = this.cards.filter((card => card.type === "DEBIT"))
                    console.log(this.cardDebit);
                    this.cardsLength = this.cards.length;
                    console.log(this.cardsLength);
            })
            .catch((error) => console.log(error));
        },
        getCardColor(card) {
            if (card.color === this.cardGold) {
                return "cardGold";
            } else if (card.color === this.cardTitanium) {
                return "cardPlatinum";
            }else if (card.color === this.cardSilver){
                return "cardSilver";
            }
            return "";
        },
            logout(){
                axios.post("/api/logout")
                .then(response => {
                    console.log(response);
                    Swal.fire({
                        icon: 'success',
                        text: 'You closed your session',
                        text: 'Until next time!',
                        showConfirmButton: false,
                      });
                      setTimeout(() => {
                        window.location.href = "http://localhost:8080/web/index.html"
                      },1000)

                }).catch(error => {
                    console.log(error);
                    
                    
                  });
            },
            createCards(){
                window.location.href = "http://localhost:8080/web/create-cards.html"
            }
    }
        }


const app = createApp(options)

app.mount('#app')