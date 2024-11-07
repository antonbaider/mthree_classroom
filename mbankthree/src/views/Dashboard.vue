<!-- src/views/Dashboard.vue -->
<template>
  <div class="dashboard">
<!--    <h1>Your Dashboard</h1>-->

    <section class="overview">
      <div class="overview-card">
        <i class="fas fa-wallet fa-2x"></i>
        <h3>Total Balance</h3>
        <p>{{ formatCurrency(totalBalance, latestTransactionCurrency) }}</p>
      </div>
      <div class="overview-card">
        <i class="fas fa-chart-line fa-2x"></i>
        <h3>Monthly Expenses</h3>
        <p>{{ formatCurrency(Math.abs(monthlyExpenses), getDefaultCurrency) }}</p>
      </div>
      <div class="overview-card">
        <i class="fas fa-arrow-up fa-2x"></i>
        <h3>Recent Deposits</h3>
        <p>{{ formatCurrency(recentDeposits, getDefaultCurrency) }}</p>
      </div>
    </section>

    <section class="transactions">
<!--      <h2>Recent Transactions</h2>-->
      <table>
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
          <td data-label="Date">{{ formatDate(transaction.timestamp) }}</td>
          <td data-label="Description">{{ formatDescription(transaction) }}</td>
          <td :class="{ 'negative': transaction.amount < 0 }" data-label="Amount">
            {{ formatCurrency(transaction.amount, transaction.currency) }}
          </td>
          <td data-label="Balance After">{{ formatCurrency(transaction.balanceAfter, transaction.currency) }}</td>
        </tr>
        </tbody>
      </table>
      <div v-if="transactions.length === 0" class="no-transactions">
        No recent transactions found.
      </div>
    </section>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import api from '@/services/api.js';

export default {
  name: 'Dashboard',
  setup() {
    const transactions = ref([]);
    const loading = ref(true);

    const fetchTransactions = async () => {
      try {
        const response = await api.get('/api/transactions/history');
        if (response.data && response.data.data) {
          // Sort transactions by timestamp descendingly to have the latest first
          transactions.value = response.data.data.sort(
              (a, b) => new Date(b.timestamp) - new Date(a.timestamp)
          );
        } else {
          console.error('No transaction data found.');
        }
      } catch (error) {
        console.error('Error fetching transactions:', error);
      } finally {
        loading.value = false;
      }
    };

    /**
     * Retrieves the latest transaction's balanceAfter as the total balance.
     */
    const totalBalance = computed(() => {
      if (transactions.value.length > 0) {
        return transactions.value[0].balanceAfter;
      }
      return 0;
    });

    /**
     * Retrieves the currency of the latest transaction.
     */
    const latestTransactionCurrency = computed(() => {
      if (transactions.value.length > 0) {
        return transactions.value[0].currency;
      }
      return 'USD';
    });

    /**
     * Sums all negative transaction amounts for the current month.
     */
    const monthlyExpenses = computed(() => {
      const currentMonth = new Date().getMonth();
      return transactions.value
          .filter(
              (tx) =>
                  new Date(tx.timestamp).getMonth() === currentMonth && tx.amount < 0
          )
          .reduce((acc, tx) => acc + tx.amount, 0);
    });

    /**
     * Sums all positive transaction amounts for the current month.
     */
    const recentDeposits = computed(() => {
      const currentMonth = new Date().getMonth();
      return transactions.value
          .filter(
              (tx) =>
                  new Date(tx.timestamp).getMonth() === currentMonth && tx.amount > 0
          )
          .reduce((acc, tx) => acc + tx.amount, 0);
    });

    /**
     * Formats the transaction description to include "From" and "To" card numbers.
     * @param {Object} transaction
     * @returns {string}
     */
    const formatDescription = (transaction) => {
      const from = transaction.senderCardNumber
          ? `From ${transaction.senderCardNumber}`
          : 'From N/A';
      const to = transaction.receiverCardNumber
          ? `to ${transaction.receiverCardNumber}`
          : 'to N/A';
      return `${from} ${to}`;
    };

    /**
     * Formats the timestamp to a readable date string.
     * @param {string} timestamp
     * @returns {string}
     */
    const formatDate = (timestamp) => {
      return new Date(timestamp).toLocaleDateString(undefined, {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      });
    };

    /**
     * Formats the amount to a currency string.
     * @param {number} amount
     * @param {string} currency
     * @returns {string}
     */
    const formatCurrency = (amount, currency) => {
      return new Intl.NumberFormat(undefined, {
        style: 'currency',
        currency,
      }).format(amount);
    };

    /**
     * Retrieves the default currency from the latest transaction or defaults to 'USD'.
     */
    const getDefaultCurrency = computed(() => {
      if (transactions.value.length > 0) {
        return transactions.value[0].currency;
      }
      return 'USD';
    });

    onMounted(() => {
      fetchTransactions();
    });

    return {
      transactions,
      loading,
      totalBalance,
      monthlyExpenses,
      recentDeposits,
      formatDate,
      formatDescription,
      formatCurrency,
      getDefaultCurrency,
      latestTransactionCurrency,
    };
  },
};
</script>

<style scoped>
.dashboard {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.dashboard h1 {
  text-align: center;
  margin-bottom: 30px;
  color: var(--text-color-primary);
}

.overview {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  margin-bottom: 40px;
}

.overview-card {
  background-color: var(--secondary-color);
  color: var(--text-color-primary);
  border-radius: 15px;
  padding: 20px;
  width: 250px;
  text-align: center;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform var(--transition-speed) ease, box-shadow var(--transition-speed) ease;
  margin-bottom: 20px;
}

.overview-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
}

.overview-card i {
  color: var(--primary-color);
  margin-bottom: 15px;
}

.overview-card h3 {
  font-size: var(--font-size-lg);
  margin-bottom: 10px;
  color: var(--text-color-primary);
}

.overview-card p {
  font-size: 1rem;
  font-weight: var(--font-weight-bold);
  color: var(--text-color-primary);
}

.transactions {
  width: 100%;
}

.transactions h2 {
  margin: 20px;
  color: var(--text-color-primary);
}

table {
  width: 100%;
  border-collapse: collapse;
  background-color: var(--secondary-color);
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: background-color var(--transition-speed) ease;
}

thead {
  background-color: var(--primary-color);
  color: var(--secondary-color);
}

th,
td {
  padding: 15px;
  text-align: left;
}

th {
  font-weight: var(--font-weight-bold);
}

tr:nth-child(even) {
  background-color: #f9f9f9;
}

.negative {
  color: #e74c3c; /* Red for negative amounts */
}

.no-transactions {
  text-align: center;
  padding: 20px;
  color: var(--text-color-primary);
}

/* Responsive Design */
@media (max-width: 768px) {
  .overview {
    flex-direction: column;
    align-items: center;
  }

  table,
  thead,
  tbody,
  th,
  td,
  tr {
    display: block;
  }

  thead tr {
    display: none;
  }

  tr {
    margin-bottom: 15px;
  }

  td {
    position: relative;
    padding-left: 50%;
  }

  td::before {
    content: attr(data-label);
    position: absolute;
    left: 15px;
    font-weight: var(--font-weight-bold);
    color: var(--primary-color);
  }
}
</style>