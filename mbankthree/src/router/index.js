// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/views/Home.vue';
import Login from '@/views/Login.vue';
import Dashboard from '@/views/Dashboard.vue';
import Accounts from '@/views/Accounts.vue';
import { useAuthStore } from '@/stores/auth.js';

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: { guest: true },
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: { requiresAuth: true },
    },
    {
        path: '/accounts',
        name: 'Accounts',
        component: Accounts,
        meta: { requiresAuth: true },
    },
    // Add other routes here
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// Navigation Guards
router.beforeEach((to, from, next) => {
    const auth = useAuthStore();
    if (to.meta.requiresAuth && !auth.isLoggedIn) {
        next({ name: 'Login' });
    } else if (to.meta.guest && auth.isLoggedIn) {
        next({ name: 'Dashboard' });
    } else {
        next();
    }
});

export default router;