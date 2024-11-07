<!-- frontend/src/views/Home.vue -->
<template>
  <div class="home">
    <h1>Welcome to the Vue Banking App</h1>
    <p>Please log in to view your dashboard and transactions.</p>
  </div>
</template>

<script>
export default {
  name: 'Home'
};
</script>

<style scoped>
.home {
  text-align: center;
  margin-top: 50px;
}

h1 {
  color: #333;
}

p {
  color: #666;
}
</style>