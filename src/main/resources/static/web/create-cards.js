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
            Swal.fire({
                icon: 'question',
                title: '¿Are you sure you want to create a card ' + this.selectedCardType + ' ' + this.selectedCardColor + '?',
                showCancelButton: true,
                confirmButtonText: 'Yes',
                cancelButtonText: 'No'
              }).then((result) => {
                if (result.isConfirmed) {
                  // El usuario hizo clic en "Sí", procede a crear la tarjeta
                  axios.post("/api/clients/current/cards", `type=${this.selectedCardType}&color=${this.selectedCardColor}`)
                  .then(response => {
                    console.log(response);
                    this.cards = response.data.cards;
                    console.log(this.cards);
            
                    Swal.fire({
                      icon: 'success',
                      title: 'You have requested a new card',
                      showConfirmButton: false,
                    });
                    setTimeout(() => {
                      window.location.href = "http://localhost:8080/web/cards.html"
                    }, 1000)
                  })
                  .catch((error) => {
                    Swal.fire({
                      icon: 'error',
                      title: 'You already have a card with this type and color',
                      showConfirmButton: false,
                    });
                    setTimeout(() => {
                      // Puedes agregar aquí cualquier acción adicional si es necesario
                    }, 1000)
                  })
                } else {
                  // El usuario hizo clic en "No" o cerró el cuadro de diálogo
                  Swal.fire({
                    icon: 'info',
                    title: 'Operation cancelled',
                    showConfirmButton: false,
                    timer: 1000
                  });
                }
              });
        },
        logout() {
            axios.post("/api/logout")
                .then(response => {
                    console.log(response);

                    Swal.fire({
                        icon: 'success',
                        title: 'You closed your session',
                        text: 'Until next time!',
                        showConfirmButton: false,

                    });
                    setTimeout(() => {
                        window.location.href = "http://localhost:8080/web/index.html"
                    }, 2000)

                }).catch(error => {
                    console.log(error);
                });
        },
        
        },
            
            
    }
        


const app = createApp(options)

app.mount('#app')