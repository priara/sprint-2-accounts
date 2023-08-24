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
        
    },

    methods: {
        addClient(){
            // let client = {
            //     firstName: this.firstName,
            //     lastName: this.lastName,
            //     email:this.email,
            // }
            //enviar a la api un nuevo cliente
            axios.post("/api/clients", `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}`)
            .then(response => {
                console.log(response);
                // this.clients.push(datos.data)
                // this.firstName= "",
                // this.lastName= "",
                // this.email= ""
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