let { createApp } = Vue

const options = {
    data() {
        return {
            response:[],
            cards : [],
            cardType:["CREDIT", "DEBIT"],
            cardColor:["GOLD", "SILVER","PLATINUM"],
            selectedCardType: "", 
            selectedCardColor: "",

        }
    },

    created() {

        },
        

    methods: {
        createCards(){
            console.log(this.selectedCardColor);
            console.log(this.selectedCardType);
            console.log(this.cardColor);
            axios.post("/api/clients/current/cards", `type=${this.selectedCardType}&color=${this.selectedCardColor}`)
            .then(response => {
                    console.log(response);
                    this.cards = response.data.cards;
                    console.log(this.cards);

                    Swal.fire({
                        icon: 'success',
                        title: 'You created a new card',
                        text: 'Until next time!',
                        showConfirmButton: false,
                      });
                      setTimeout(() => {
                        window.location.href = "http://localhost:8080/web/cards.html"
                      },1000)
            })
            .catch((error) => {
                Swal.fire({
                    icon: 'error',
                    title: 'You already have a card with this type and color',
                    showConfirmButton: false,
                  });
                  setTimeout(() => {
                  },1000)

            })
        },
        
        },
            logout(){
                axios.post("/api/logout")
                .then(response => {
                    console.log(response);
                    window.location.href = "http://localhost:8080/web/index.html"
                    Swal.fire({
                        icon: 'success',
                        title: 'You closed your session',
                        text: 'Until next time!',
                        showConfirmButton: false,
                      });
                      setTimeout(() => {
                        window.location.href = "http://localhost:8080/web/index.html"
                      },1000)

                }).catch(error => { console.log(error);
                    
                    
                  });
            },
            
    }
        


const app = createApp(options)

app.mount('#app')