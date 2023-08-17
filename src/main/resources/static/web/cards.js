let { createApp } = Vue

const options = {
    data() {
        return {
            response:[],
            cards : [],
            cardGold:"GOLD",
            cardTitanium:"TITANIUM",
            cardSilver:"SILVER",
            cardCredit:"CREDIT",
            cardDebit:"DEBIT",

        }
    },

    created() {
            this.getCards();
        },
        

    methods: {
        getCards(){
            axios.get("http://localhost:8080/api/clients/1")
            .then(response => {
                    console.log(response);
                    this.cards = response.data.cards;
                    console.log(this.cards);
                    this.cards.sort((a, b) => a.id - b.id);
                    this.cardCredit = this.cards.filter((card => card.type === "CREDIT"))
                    console.log(this.cardCredit);
                    this.cardDebit = this.cards.filter((card => card.type === "DEBIT"))
                    console.log(this.cardDebit);
            })
            .catch((error) => console.log(error));
        },
        getCardColor(card) {
            if (card.color === this.cardGold) {
                return "cardGold";
            } else if (card.color === this.cardTitanium) {
                return "cardTitanium";
            }else if (card.color === this.cardSilver){
                return "cardSilver";
            }
            return "";
        },
        }
}


const app = createApp(options)

app.mount('#app')