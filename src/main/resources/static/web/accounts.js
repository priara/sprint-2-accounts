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
            accountsLength: 0,
            selectedAccount:[],
            selectType:[],

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
                    this.accounts = this.client.accounts.filter(account => account.active);
                    this.loans = this.client.loans;
                    console.log(this.loans);
                    this.accounts.sort((a, b) => a.id - b.id);
                    this.loans.sort((a, b) => a.id - b.id);
                })
                .catch((error) => console.log(error));
        },
        createAccount() {
                    axios.post("/api/clients/current/accounts", `type=${this.selectType}`)
                        .then(response => {
                            console.log(response);
                            this.selectType = {};
                            document.location.reload()

                            
                        }).catch((error) => console.log(error));
                }, 
        applyloans(){
            window.location.href = "http://localhost:8080/web/loan-application.html"

        },
        removeAccounts(){
            axios.patch(`/api/clients/current/accounts/${this.selectedAccount.id}`)
            .then(response => {
                this.selectedAccount = {};
                window.location.href = "http://localhost:8080/web/accounts.html"

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

