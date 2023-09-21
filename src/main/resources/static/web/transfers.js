let { createApp } = Vue

const options = {
    data() {
        return {
            response:[],
            accounts:[],
            transactions:[],
            selectedForm: 'form1',
            selectedAmount:"",
            selectedDescription:"",
            selectedNumberOrigin:"",
            selectedNumberDestiny:"",
        }
    },

    created() {
            this.getAccounts();
        },
        
        
    methods: {
            getAccounts(){
                axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    this.client = response.data;
                    this.accounts = this.client.accounts.sort((a,b) => a.id - b.id).filter(account => account.active);
                    console.log(this.accounts);



                    // HACER CARTEL PARA QUE NO ME DEJE HACER TRANSFER A LA MISMA CUENTA
                })
                .catch((error) => console.log(error));
            },
        createTransactions(){
            Swal.fire({
                icon: 'question',
                text: '¿Are you sure you want to create a transaction? ',
                showCancelButton: true,
                confirmButtonText: 'Yes',
                cancelButtonText: 'No',
              }).then((result) => {
                if (result.isConfirmed) {
                    
                  axios.post("/api/transactions", `amount=${this.selectedAmount}&description=${this.selectedDescription}&numberOrigin=${this.selectedNumberOrigin}&numberDestiny=${this.selectedNumberDestiny}`)
                  .then(response => {
                    console.log(response);
                    this.transactions = response.data.transactions;
                    console.log(this.transactions);
            
                    Swal.fire({
                      icon: 'success',
                      text: 'You have requested a new transaction',
                      showConfirmButton: false,
                    });
                    setTimeout(() => {
                        window.location.href = "http://localhost:8080/web/transfers.html"
                    }, 1000)
                  })
                  .catch((error) => {
                    console.log(error);
                    Swal.fire({
                      icon: 'error',
                      text: 'Cannot transfer to the same account',
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
                    text: 'Operation cancelled',
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
                        text: 'You closed your session',
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