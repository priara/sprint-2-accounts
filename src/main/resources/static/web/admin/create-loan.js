
let { createApp } = Vue



const options = {
    data() {
        return {
            selectedName:[],
            selectedAmount:[],
            selectedPayments:[],
            selectedPercentage:[],
            listarray:[],

        }
    },

    created() {
        
    },

    methods: {
      
        createLoan(){
          
            this.listarray = this.selectedPayments.split(/\s*,\s*/);
            axios.post("/api/loans/create", {name:this.selectedName, maxAmount:this.selectedAmount,payments:this.listarray,percentage:this.selectedPercentage})
            .then(response => {
                console.log(response);
                
            Swal.fire({
                icon: 'success',
                text: 'You have requested a new loan',
                showConfirmButton: false,

              });
              setTimeout(() => {
                window.location.href = "http://localhost:8080/web/admin/create-loan.html"
              },2000)
                
                
            }).catch(error => {
                console.log(error.response.data);
              });
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