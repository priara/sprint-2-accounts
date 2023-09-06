let { createApp } = Vue

const options = {
    data() {
        return {
            response:[],
            accounts:[],
            selectedAmount:"",
            selectedName:"",
            selectedPayments:"",
            loans:[],
            payments:[],
            loanSelect:{},
            loanPayments:[],
            selectedNumber:"",
            
        }
    },

    created() {
            this.getAccounts();
            this.getLoans();
        },
        
        
    methods: {
            getAccounts(){
                axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    this.client = response.data;
                    this.accounts = this.client.accounts.sort((a,b) => a.id - b.id);
                    console.log(this.accounts);



                    
                })
                .catch((error) => console.log(error));
            },
            getLoanss(){
                Swal.fire({
                    icon: 'question',
                    text: '¿Are you sure you want to create a loan? ',
                    showCancelButton: true,
                    confirmButtonText: 'Yes',
                    cancelButtonText: 'No',
                  }).then((result) => {
                    if (result.isConfirmed) {
                        
                axios.post("/api/loans", {"amount":this.selectedAmount, "name":this.selectedName,"payments":this.selectedPayments,"number":this.selectedNumber})
                .then(response => {
                    console.log(response);

                    Swal.fire({
                        icon: 'success',
                        text: 'You have requested a new loan',
                        showConfirmButton: false,
                      });
                      setTimeout(() => {
                          window.location.href = "http://localhost:8080/web/accounts.html"
                      }, 1000)
                }).catch(error => {
                    console.error(error)
                    Swal.fire({
                        icon: 'error',
                        text: 'You already have this loan',
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
            getLoans(){
                axios.get("/api/loans")
                .then(response => {
                    this.loans= response.data.sort((a,b) => a.id - b.id);
                    console.log(this.loans);



                }).catch(error => {
                    console.log(error);
                });
            },
            filterPayments() {
                this.loanSelect = {...this.loans.filter(loan => loan.name === this.selectedName)};
                if (this.loanSelect[0]) {
                    this.loanPayments = this.loanSelect[0].payments || [];
                } else {
                    this.loanPayments = [];
                }
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
        computed:{
            hola(){
                console.log(this.selectedName);
            },
            
        }
        
    }
        


const app = createApp(options)

app.mount('#app')