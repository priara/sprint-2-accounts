let { createApp } = Vue

const options = {
    data() {
        return {
            response:[],
            accounts: [],
            loans:[],

        }
    },

    created() {
            
            this.getAccountNumber();
        },
        

    methods: {
        getAccountNumber(){
            axios.get("http://localhost:8080/api/clients/1")
            .then(response => {
                    console.log(response);
                this.accounts = response.data.accounts;
                this.loans = response.data.loans;
                this.accounts.sort((a, b) => a.id - b.id);
                this.loans.sort((a, b) => a.id - b.id);
                
                


            })
            .catch((error) => console.log(error));
        },
        formattedAmount(amount){
            return amount.toLocaleString();
        }
                
        }


    
}


const app = createApp(options)

app.mount('#app')