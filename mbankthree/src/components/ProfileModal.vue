<!-- src/components/ProfileModal.vue -->
<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <button class="close-button" @click="closeModal">×</button>
      <h2>User Profile</h2>
      <ul class="profile-details">
        <li><strong>First Name:</strong> {{ profile.firstName }}</li>
        <li><strong>Last Name:</strong> {{ profile.lastName }}</li>
        <li><strong>Username:</strong> {{ profile.username }}</li>
        <li><strong>Email:</strong> {{ profile.email }}</li>
        <li><strong>Phone:</strong> {{ profile.phone }}</li>
        <li><strong>SSN:</strong> {{ profile.ssn }}</li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProfileModal',
  props: {
    profile: Object,
    visible: Boolean,
  },
  methods: {
    closeModal() {
      this.$emit('close');
    },
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: var(--secondary-color);
  padding: 30px;
  border-radius: 15px;
  width: 90%;
  max-width: 400px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  position: relative;
  text-align: left;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: transparent;
  border: none;
  font-size: 1.5rem;
  color: var(--text-color);
  cursor: pointer;
}

.modal-content h2 {
  text-align: center;
  color: var(--primary-color);
}

.profile-details {
  list-style: none;
  padding: 0;
  color: var(--text-color);
}

.profile-details li {
  margin: 10px 0;
}

.profile-details strong {
  color: var(--accent-color);
}
</style>