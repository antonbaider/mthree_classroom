import axios from 'axios';
import jwtDecode from 'jwt-decode';
import router from '../router';

const tokensKey = 'tokens';

export default {
    user: null,

    /**
     * Logs in the user by sending credentials to the backend.
     * Stores the received tokens and decodes the access token to get user info.
     * @param {string} username
     * @param {string} password
     * @returns {Promise<void>}
     */
    login(username, password) {
        return axios.post('http://localhost:8080/api/auth/login', { username, password })
            .then(response => {
                // Store tokens in localStorage
                localStorage.setItem(tokensKey, JSON.stringify(response.data));
                // Decode the access token to get user information
                this.user = jwtDecode(response.data.accessToken);
            });
    },

    /**
     * Logs out the user by removing tokens and redirecting to the login page.
     */
    logout() {
        localStorage.removeItem(tokensKey);
        this.user = null;
        router.push('/login');
    },

    /**
     * Retrieves tokens from localStorage.
     * @returns {Object|null} The tokens object or null if not found.
     */
    getTokens() {
        const tokens = localStorage.getItem(tokensKey);
        return tokens ? JSON.parse(tokens) : null;
    },

    /**
     * Checks if the user is logged in by verifying the access token's expiration.
     * @returns {boolean} True if logged in, false otherwise.
     */
    isLoggedIn() {
        const tokens = this.getTokens();
        if (!tokens) return false;
        try {
            const decoded = jwtDecode(tokens.accessToken);
            const currentTime = Date.now() / 1000;
            return decoded.exp > currentTime;
        } catch (error) {
            console.error('Invalid token:', error);
            return false;
        }
    }
};