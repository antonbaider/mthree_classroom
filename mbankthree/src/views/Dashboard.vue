<!-- src/views/Dashboard.vue -->
<template>
  <div class="dashboard">
    <h1>Your Transactions</h1>
    <div v-if="loading">Loading transactions...</div>
    <div v-else>
      <table v-if="transactions.length">
        <thead>
        <tr>
          <th>Date</th>
          <th>Description</th>
          <th>Amount</th>
          <th>Balance After</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="transaction in transactions" :key="transaction.transactionId">
          <td>{{ formatDate(transaction.timestamp) }}</td>
          <td>{{ formatDescription(transaction) }}</td>
          <td :class="{ 'negative': transaction.amount < 0 }">
            {{ formatCurrency(transaction.amount, transaction.currency) }}
          </td>
          <td>{{ formatCurrency(transaction.balanceAfter, transaction.currency) }}</td>
        </tr>
        </tbody>
      </table>
      <div v-else>No transactions found.</div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import api from '@/services/api.js';

export default {
  name: 'Dashboard',
  setup() {
    const transactions = ref([]);
    const loading = ref(true);

    const fetchTransactions = async () => {
      try {
        const response = await api.get('/api/transactions/history');
        console.log('Transaction Response:', response.data);

        if (response.data && response.data.data) {
          transactions.value = response.data.data;
          console.log('Transactions fetched:', transactions.value);
        } else {
          console.error('No transaction data found in response');
          alert('No transactions available.');
        }
      } catch (error) {
        console.error('Error fetching transactions:', error.response ? error.response.data : error.message);
        alert('An error occurred while fetching transactions.');
      } finally {
        loading.value = false;
      }
    };

    const formatDate = (timestamp) => {
      const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
      return new Date(timestamp).toLocaleDateString(undefined, options);
    };

    const formatDescription = (transaction) => {
      return `Transfer to ${transaction.receiverCardNumber}`;
      // Customize based on transaction type or other fields if available
    };

    const formatCurrency = (amount, currency) => {
      return new Intl.NumberFormat(undefined, { style: 'currency', currency }).format(amount);
    };

    onMounted(() => {
      fetchTransactions();
    });

    return {
      transactions,
      loading,
      formatDate,
      formatDescription,
      formatCurrency,
    };
  },
};
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

h1 {
  margin-bottom: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background-color: #f2f2f2;
}

th,
td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.negative {
  color: red;
}
</style>