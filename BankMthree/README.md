
# MthreeBank App

## Overview

MthreeBank App is a modern banking application designed to provide users with a seamless experience for managing their finances. The app offers features such as account management, transaction handling, and enhanced security measures to protect sensitive information.

## Features

- **User Authentication**: Secure login and registration process.
- **Account Management**: Users can create and manage multiple accounts with unique card numbers and expiration dates.
- **Transaction Processing**: Users can transfer funds between accounts with built-in validation to prevent unauthorized transfers.
- **Swagger API Documentation**: The application uses Swagger to provide interactive API documentation, making it easier for developers to understand and utilize the application's endpoints.
- **Caching**: Implements caching to enhance performance and reduce database load.
- **Security Measures**: Sensitive information such as passwords, Social Security Numbers (SSNs), and card numbers are securely hidden and protected.
- **Email Notifications**: Sends modern, branded email notifications for transactions and account updates.
- **PDF Generation**: Ability to generate and send PDF receipts for transactions, enhancing user experience and record-keeping.

## Database

The application uses MySQL as the database management system, ensuring reliable data storage and retrieval.

## Getting Started

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd MthreeBankApp
   ```

2. **Set up the database**:
   - Create a new MySQL database.
   - Update the database connection settings in `application.properties`.

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the API documentation**:
   - Open your browser and go to `http://localhost:8080/swagger-ui.html`.


## Security Considerations

- The application implements various security practices, including:
  - **Password hashing** to protect user credentials.
  - **Data encryption** for sensitive information such as SSNs and card numbers.
  - **Input validation** to prevent malicious inputs.

## Contributing

Contributions are welcome! Please follow these steps to contribute to the project:

1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature/YourFeature
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add some feature"
   ```
4. Push to the branch:
   ```bash
   git push origin feature/YourFeature
   ```
5. Open a Pull Request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
