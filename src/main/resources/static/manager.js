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
            axios.get("http://localhost:8080/clients", client)
            .then(datos => {
                this.clients.push(datos.data)
                this.firstName= "",
                this.lastName= "",
                this.email= ""
            })
                
        },
        logout(){
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
                  },2000)
                  
            }).catch(error => {
                console.log(error);
              });
        }


    }
}


const app = createApp(options)

app.mount('#app')