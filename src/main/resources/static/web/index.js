let { createApp } = Vue

const options = {
    data() {
        return {
            password:"",
            email:"",
            firstName:"",
            lastName:"",
            emailRegister:"",
            passwordRegister:"",
            showForm:false,
            clientEmail:{},

        }
    },

    created() {
      
        
        
    },

    methods: {  
        login() {
            axios.post("/api/login", `email=${this.email}&password=${this.password}`)
              .then(response => {
                console.log(response.status);

                Swal.fire({
                  icon: 'success',
                  title: 'Login successful',
                  text: 'Welcome!',
                  showConfirmButton: false,

                });
                setTimeout(() => {
                  console.log(this.email);
                  if (this.email.includes("admin")) {
                    window.location.href = "http://localhost:8080/manager.html";
                  } else {
                    window.location.href = "http://localhost:8080/web/accounts.html";
                }
                },1000)

              })
              .catch(error => {
                console.log(error.response.status);

                Swal.fire({
                  icon: 'error',
                  title: 'Failed to login',
                  text: 'Please verify your email and password.',
                });
                setTimeout(() => {
                },1000)
              });
          },
          register() {
            axios.post("/api/clients", `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`)
              .then(response => {
                console.log(response);
          
                Swal.fire({
                  icon: 'success',
                  title: 'Register successful',
                  text: 'Welcome!',
                  showConfirmButton: false,
                });
                setTimeout(() => {
                  window.location.href = "http://localhost:8080/web/accounts.html"
                },1000)
                
                axios.post("/api/login", `email=${this.emailRegister}&password=${this.passwordRegister}`)
                .then(response => {
                  console.log(response);
                  window.location.href = "http://localhost:8080/web/accounts.html"
                })
              })
              .catch(error => {
                console.log(error);
                Swal.fire({
                  icon: 'error',
                  title: 'Failed to register',
                  text: 'Please verify your email and password.',
                });
                setTimeout(() => {
                },1000)
              });
          },
          showForms(){
            this.showForm = !this.showForm;
            console.log(this.showForm);
          }
          

            }
        }



const app = createApp(options)

app.mount('#app')