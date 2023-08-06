let { createApp } = Vue

const options = {
    data() {
        return {
            datos:[],
            accounts: null,

        }
    },

    created() {
            
            this.getAccountNumber();
        },
        

    methods: {
        getAccountNumber(){
            axios.get("http://localhost:8080/api/clients/1")
            .then(datos => {
                this.accounts = datos.data.accounts;
                console.log(this.accounts);

            })
        },
                
        }


    
}


const app = createApp(options)

app.mount('#app')