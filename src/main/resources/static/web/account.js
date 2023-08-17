let { createApp } = Vue


const options = {
    data() {
        return {
            datos: [],
            id: "",
            parametro: "",
            parametros: "",
            accountSelect: {},
            transactions: [],
            creditGreen: "CREDIT",
            debitRed: "DEBIT",
            formatter: new Intl.NumberFormat('en-US', {
                style: 'currency',
                currency: 'USD',
                minimumFractionDigits: 0
            }),

        }
    },

    created() {

        this.getAccountNumber();

    },


    methods: {
        getAccountNumber() {
            this.parametro = location.search;
            this.parametros = new URLSearchParams(this.parametro);
            this.id = this.parametros.get('id');

            axios.get("http://localhost:8080/api/accounts/" + this.id)
                .then(response => {
                    console.log(response);
                    this.accountSelect = response.data;
                    console.log(this.accountSelect);
                    this.transactions = response.data.transaction;
                    console.log(this.transactions);


                })
                .catch((error) => console.log(error));
        },
        getTransactionColorClass(transaction) {
            if (transaction.type === this.creditGreen) {
                return "creditGreen";
            } else if (transaction.type === this.debitRed) {
                return "debitRed";
            }
            return "";
        },
        formattedDate(date) {
            const options = { year: "numeric", month: "numeric", day: "numeric" , hour: '2-digit', minute: '2-digit' , second: '2-digit'};
            const formatted = new Date(date).toLocaleDateString(undefined, options);
            return formatted;
        }

    }
}


const app = createApp(options)

app.mount('#app')