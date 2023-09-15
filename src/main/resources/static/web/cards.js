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
            selectedCard:[],

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
                    this.cards = response.data.cards.filter(card => card.isActive)
                    console.log(this.cards);
                    this.cards.sort((a, b) => a.id - b.id);
                    this.cardCredit = this.cards.filter(card => card.type === "CREDIT" )
                    console.log(this.cardCredit);
                    this.cardDebit = this.cards.filter(card => card.type === "DEBIT")
                    console.log(this.cardDebit);

                    const newDate = new Date();
                    const year = newDate.getFullYear();
                    const month = String(newDate.getMonth() + 1 ).padStart(2, '0');
                    this.currentDate = `${month}/${year}`;
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
            },
            removeCards(){
                axios.patch(`/api/clients/current/cards/${this.selectedCard.id}`)
                .then(response => {
                    this.selectedCard = {};
                    window.location.href = "http://localhost:8080/web/cards.html"

                }).catch(error => {
                    console.log(error.response.data);
                  });
            }
    }
        }


const app = createApp(options)

app.mount('#app')