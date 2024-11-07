import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Dashboard from '../views/Dashboard.vue';
import auth from '../services/auth';

const routes = [
    { path: '/', name: 'Home', component: Home },
    { path: '/login', name: 'Login', component: Login },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: { requiresAuth: true }
    },
    // Wildcard route for a 404 page can be added here
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

// Navigation Guard
router.beforeEach((to, from, next) => {
    if (to.meta.requiresAuth && !auth.isLoggedIn()) {
        next('/login');
    } else {
        next();
    }
});

export default router;