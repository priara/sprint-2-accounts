let { createApp } = Vue

const options = {
    data() {
        return {
            response:[],
            accounts:[],
            selectedAmount:"",
            selectedName:"",
            selectedPayments:[],
            loans:[],
            payments:[],
            loanSelect:{},
            loanPayments:[],
            
        }
    },

    created() {
            this.getAccounts();
            this.getLoans();
            console.log( this.loanPayments.length);
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
            getLoans(){
                axios.get("/api/loans")
                .then(response => {
                    console.log(response);
                    this.loans= response.data.sort((a,b) => a.id - b.id);
                    console.log(this.loans);



                }).catch(error => {
                    console.log(error);
                });
            },
            filterPayments(){
                this.loanSelect=  {...this.loans.filter(loan => loan.name == this.selectedName)}
                this.loanPayments = this.loanSelect[0].payments
                console.log( this.loanPayments.length);
              },
            
        // getLoans(){
        //     axios.post("/api/loans", `amount=${this.selectedAmount}&name=${this.selectedName}&payments=${this.selectedPayments}`)
            
        // },
        
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