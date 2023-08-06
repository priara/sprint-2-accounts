let { createApp } = Vue

const options = {
    data() {
        return {
            clients: [],
            firstName: "",
            lastName: "",
            email: "",
            jsonData: null,
        }
    },

    created() {
        axios.get("http://localhost:8080/clients")
        .then(datos => {
            console.log(datos);
            this.clients = datos.data._embedded.clients
            console.log(this.clients);
            this.jsonData = JSON.stringify(datos.data, null, 1);
        })
        .catch((error) => console.log(error));
    },

    methods: {
        addClient(){
            let client = {
                firstName: this.firstName,
                lastName: this.lastName,
                email:this.email,
            }
            //enviar a la api un nuevo cliente
            axios.post("http://localhost:8080/clients", client)
            .then(datos => {
                this.clients.push(datos.data)
                this.firstName= "",
                this.lastName= "",
                this.email= ""
            })
                
        }


    }
}


const app = createApp(options)

app.mount('#app')