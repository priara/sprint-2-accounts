let { createApp } = Vue

const options = {
    data() {
        return {
            response: [],
            client: [],
            loans: [],
            accounts: [],
            formatter: new Intl.NumberFormat('en-US', {
                style: 'currency',
                currency: 'USD',
                minimumFractionDigits: 0
            }),
            selectedAccount:[],
            selectType:[],
            selectedLoan:[],
            selectedAccountLoan:[],

        }
    },

    created() {

        this.getAccountNumber();
        

    },


    methods: {
        getAccountNumber() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    console.log(response);
                    this.client = response.data;
                    console.log(this.client);
                    this.accounts = this.client.accounts.filter(account => account.active).sort((a, b) => a.id - b.id);
                    this.loans = this.client.loans.filter(loan => loan.active).sort((a, b) => a.id - b.id);
                    console.log(this.loans);

                    
                })
                .catch((error) => console.log(error));
        },
        createAccount() {
            Swal.fire({
                icon: 'question',
                text: '¿Are you sure you want to create a account ?',
                showCancelButton: true,
                confirmButtonText: 'Yes',
                cancelButtonText: 'No'
              }).then((result) => {
                if (result.isConfirmed) {

                    axios.post("/api/clients/current/accounts", `type=${this.selectType}`)
                        .then(response => {
                            console.log(response.data);
                            this.selectType = {};
                            
                            Swal.fire({
                                icon: 'success',
                                text: 'You have requested a new account',
                                showConfirmButton: false,
                              });
                              setTimeout(() => {
                                window.location.href = "http://localhost:8080/web/accounts.html"
                              }, 1000)
                            })
                        }
                        }).catch((error) => console.log(error));
                }, 
        applyloans(){
            window.location.href = "http://localhost:8080/web/loan-application.html"

        },
        removeAccounts(){
            Swal.fire({
                icon: 'question',
                text: '¿Are you sure you want to delete the account?',
                showCancelButton: true,
                confirmButtonText: 'Yes',
                cancelButtonText: 'No'
              }).then((result) => {
                if (result.isConfirmed) {

            axios.patch(`/api/clients/current/accounts/${this.selectedAccount.id}`)
            .then(response => {
                this.selectedAccount = {};

                Swal.fire({
                    icon: 'success',
                    text: 'You deleted your account!',
                    showConfirmButton: false,
                  });
                  setTimeout(() => {
                    window.location.href = "http://localhost:8080/web/accounts.html"
                  }, 1000)
                })
            }
                setTimeout(() => {
                    window.location.href = "http://localhost:8080/web/accounts.html"
                }, 2000)

            }).catch((error) => {
                console.log(error.response.data);
                Swal.fire({
                  icon: 'error',
                  text: 'Account cannot be deleted because it contains money',
                  showConfirmButton: false,
                });
                setTimeout(() => {
                  // Puedes agregar aquí cualquier acción adicional si es necesario
                }, 1000)
              })
        },
        payLoan(){
            axios.patch(`/api/loans?id=${this.selectedLoan.id}&accountNumber=${this.selectedAccountLoan.number}`)
            .then(response => {
                console.log(response.data);
                this.selectedLoan = {};
                Swal.fire({
                    icon: 'success',
                    text: 'You paid your loan!',
                    showConfirmButton: false,

                });
                setTimeout(() => {
                    window.location.href = "http://localhost:8080/web/accounts.html"
                }, 2000)


            }).catch(error => {
                console.log(error.response.data);
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

    }
}


const app = createApp(options)

app.mount('#app')

