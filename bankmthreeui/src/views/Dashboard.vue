<template>
  <div class="dashboard-container">
    <h2>Dashboard</h2>
    <div v-if="loading">Loading...</div>
    <div v-if="error" class="error">{{ error }}</div>
    <div v-if="data">
      <pre>{{ data }}</pre>
    </div>
  </div>
</template>

<script>import axiosInstance from '../services/axios';
import auth from '../services/auth';

export default {
  data() {
    return {
      data: null,
      loading: true,
      error: null
    };
  },
  created() {
    if (!auth.isLoggedIn()) {
      this.$router.push('/login');
      return;
    }

    axiosInstance.get('/protected-endpoint')
        .then(response => {
          this.data = JSON.stringify(response.data, null, 2);
          this.loading = false;
        })
        .catch(() => {
          this.error = 'Failed to fetch data. Please log in again.';
          this.loading = false;
          auth.logout();
        });
  }
};
</script>

<style scoped>
/* Add your styles here */
</style>