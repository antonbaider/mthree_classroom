package com.mthree.bankmthree.service;

import com.mthree.bankmthree.entity.Transaction;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final PdfGeneratorService pdfGeneratorService;
    private final TemplateEngine templateEngine;

    @Value("${recipient.email}")
    private String recipientEmail;

    @Value("${email.logo.path}")
    private String logoPath;

    public EmailService(JavaMailSender mailSender, PdfGeneratorService pdfGeneratorService, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.pdfGeneratorService = pdfGeneratorService;
        this.templateEngine = templateEngine;
    }

    public void notifyTransaction(Transaction transaction) {
        String formattedMessage = String.format("Dear Customer,\n\nYour transaction was successful!\n\n" + "Transaction Details:\n" + "Amount: %.2f %s\n" + "Date: %s\n\n" + "Sender Information:\n" + "Name: %s %s\n" + "Account Balance: %.2f %s\n\n" + "Receiver Information:\n" + "Name: %s %s\n\n" + "Thank you for using our service!\n\nBest Regards,\nBankMthree Team", transaction.getAmount(), transaction.getSenderAccount().getCurrency(), transaction.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), transaction.getSender().getFirstName(), transaction.getSender().getLastName(), transaction.getSenderAccount().getBalance(), transaction.getSenderAccount().getCurrency(), transaction.getReceiver().getFirstName(), transaction.getReceiver().getLastName());

        // Send the formatted email
        sendTransactionNotification(formattedMessage);
    }

    public void sendTransactionNotification(String messageContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Transaction Notification");
        message.setText(messageContent);
        mailSender.send(message);
    }

    /**
     * Sends an email with a transaction receipt using Thymeleaf template and attaches a PDF receipt.
     *
     * @param transaction The transaction details to include in the email and PDF.
     */
    public void sendTransactionEmail(Transaction transaction) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(recipientEmail);
            helper.setSubject("Transaction Notification");

            // Load and process the Thymeleaf template with transaction data
            String emailContent = buildEmailContent(transaction);
            helper.setText(emailContent, true);

            // Attach logo for email inline image
            FileSystemResource logo = new FileSystemResource(new File(logoPath));
            helper.addInline("logoImage", logo);

            // Attach PDF receipt
            DataSource pdfReceipt = pdfGeneratorService.generatePdfReceipt(transaction);
            helper.addAttachment("TransactionReceipt.pdf", pdfReceipt);
             mailSender.send(message);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds the email content by processing the Thymeleaf template.
     *
     * @param transaction The transaction details to include in the email.
     * @return The processed HTML email content.
     */
    private String buildEmailContent(Transaction transaction) {
        Context context = new Context();
        context.setVariable("transaction", transaction);
        String formattedTimestamp = transaction.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        context.setVariable("formattedTimestamp", formattedTimestamp);
        return templateEngine.process("transaction-receipt", context);
    }

    // Mask card numbers for security
    private String maskCardNumber(String cardNumber) {
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    public Map<String, Object> prepareTransactionModel(Transaction transaction) {
        Map<String, Object> model = new HashMap<>();
        model.put("transaction", transaction);
        model.put("formattedTimestamp", transaction.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return model;
    }
}

//    public void sendTransactionNotification(String subject, Transaction transaction) {
//        MimeMessage message = mailSender.createMimeMessage();
//
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Enable multipart for attachments
//
//            helper.setTo(recipientEmail);
//            helper.setSubject(subject);
//
//            // Define the email content using StringBuilder for structured formatting
//            StringBuilder formattedMessage = new StringBuilder();
//            formattedMessage.append("<html>").append("<head>").append("<style>").append("body { font-family: Arial, sans-serif; background-color: #f9f9fb; margin: 0; padding: 0; color: #333; }").append(".container { max-width: 600px; margin: 30px auto; background-color: #ffffff; padding: 25px; border-radius: 12px; box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1); }").append(".header { text-align: center; padding-bottom: 20px; }").append(".header img { width: 80px; margin-bottom: 15px; }").append(".header h1 { color: #4a90e2; font-size: 26px; margin: 0; }").append(".content { margin-top: 20px; font-size: 15px; color: #555; line-height: 1.6; }").append(".section-title { font-weight: bold; margin-top: 25px; color: #4a90e2; font-size: 17px; }").append(".details-table { width: 100%; border-collapse: separate; border-spacing: 0; margin-top: 10px; }").append(".details-table th, .details-table td { padding: 10px 15px; border: 1px solid #e0e4e8; border-radius: 6px; }").append(".details-table th { background-color: #f1f5f8; color: #4a90e2; text-align: left; }").append(".details-table td { background-color: #ffffff; }").append(".footer { margin-top: 30px; text-align: center; color: #888888; font-size: 14px; padding-top: 15px; border-top: 1px solid #e0e4e8; }").append(".footer-links { margin-top: 10px; }").append(".footer-links a { margin: 0 5px; color: #4a90e2; text-decoration: none; }").append("</style>").append("</head>").append("<body>").append("<div class='container'>").append("<div class='header'>").append("<img src='cid:logoImage' alt='BankMthree Logo'>").append("<h1>Transaction Receipt</h1>").append("</div>").append("<div class='content'>").append("<p>Dear Customer,</p>").append("<p>Your transaction was successful! Here are the details:</p>")
//
//                    // Transaction Details
//                    .append("<p class='section-title'>Transaction Details:</p>").append("<table class='details-table'>").append("<tr><th>Amount</th><td>").append(String.format("%.2f %s", transaction.getAmount(), transaction.getSenderAccount().getCurrency())).append("</td></tr>").append("<tr><th>Date</th><td>").append(transaction.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("</td></tr>").append("<tr><th>Transaction ID</th><td>").append(transaction.getId()).append("</td></tr>").append("</table>")
//
//                    // Sender Information
//                    .append("<p class='section-title'>Sender Information:</p>").append("<table class='details-table'>").append("<tr><th>Name</th><td>").append(transaction.getSender().getFirstName()).append(" ").append(transaction.getSender().getLastName()).append("</td></tr>").append("<tr><th>Card Number</th><td>").append(maskCardNumber(transaction.getSenderAccount().getCardNumber())).append("</td></tr>").append("</table>")
//
//                    // Receiver Information
//                    .append("<p class='section-title'>Receiver Information:</p>").append("<table class='details-table'>").append("<tr><th>Name</th><td>").append(transaction.getReceiver().getFirstName()).append(" ").append(transaction.getReceiver().getLastName()).append("</td></tr>").append("<tr><th>Card Number</th><td>").append(maskCardNumber(transaction.getReceiverAccount().getCardNumber())).append("</td></tr>").append("</table>")
//
//                    // Footer
//                    .append("<div class='footer'>").append("<p>Thank you for using our service!<br>Best Regards,<br>BankMthree Team</p>").append("<p class='footer-links'>").append("<a href='#'>Privacy Policy</a> | ").append("<a href='#'>Terms of Service</a> | ").append("<a href='#'>Contact Us</a>").append("</p>").append("<p>Follow us: ").append("<a href='https://www.facebook.com'><img src='https://upload.wikimedia.org/wikipedia/commons/5/51/Facebook_f_logo_%282019%29.svg' alt='Facebook' style='width:20px;'></a>").append("<a href='https://www.twitter.com'><img src='https://upload.wikimedia.org/wikipedia/commons/c/ce/X_logo_2023.svg' alt='Twitter' style='width:20px;'></a>").append("<a href='https://www.linkedin.com'><img src='https://upload.wikimedia.org/wikipedia/commons/8/81/LinkedIn_icon.svg' alt='LinkedIn' style='width:20px;'></a>").append("</p>").append("</div>").append("</div>").append("</body>").append("</html>");
//
//            helper.setText(formattedMessage.toString(), true);  // Enable HTML content
//
//            // Attach the logo as an inline image
//            FileSystemResource logo = new FileSystemResource(new File("src/main/resources/images/logo.png"));
//            helper.addInline("logoImage", logo);
//
//            DataSource pdfReceipt = generatePdfReceipt(transaction);
//            // Generate the PDF receipt and attach to the email
//            helper.addAttachment("TransactionReceipt.pdf", pdfReceipt);
//
//            mailSender.send(message);
//
//        } catch (Exception e) {
//            e.printStackTrace(); // Handle exception for better error tracking
//        }
//    }
//
//    private String maskCardNumber(String cardNumber) {
//        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
//    }
//
//    // Method to generate PDF receipt using iText
//     DataSource generatePdfReceipt(Transaction transaction) throws Exception {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//        // Setup PDF document and writer
//        PdfWriter writer = new PdfWriter(outputStream);
//        PdfDocument pdfDocument = new PdfDocument(writer);
//        Document document = new Document(pdfDocument);
//
//        // Add the logo to the document
//        String logoPath = "src/main/resources/images/logo.png"; // Update with your logo path
//        ImageData logoData = ImageDataFactory.create(logoPath);
//        Image logo = new Image(logoData);
//        logo.setWidth(100);
//        logo.setTextAlignment(TextAlignment.CENTER);
//        document.add(logo);
//
//        // Add transaction receipt content
//        document.add(new Paragraph("Transaction Receipt").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER).setMarginTop(10f));
//
//        // Transaction Details Table
//        document.add(new Paragraph("Transaction Details:").setBold().setMarginTop(20f));
//        Table transactionTable = new Table(UnitValue.createPercentArray(new float[]{2, 3})).useAllAvailableWidth();
//        transactionTable.addCell("Amount:");
//        transactionTable.addCell(String.format("%.2f %s", transaction.getAmount(), transaction.getSenderAccount().getCurrency()));
//        transactionTable.addCell("Date:");
//        transactionTable.addCell(transaction.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        transactionTable.addCell("ID:");
//        transactionTable.addCell(String.valueOf(transaction.getId()));
//        document.add(transactionTable);
//
//        // Sender Information Table
//        document.add(new Paragraph("Sender Information:").setBold().setMarginTop(20f));
//        Table senderTable = new Table(UnitValue.createPercentArray(new float[]{2, 3})).useAllAvailableWidth();
//        senderTable.addCell("Name:");
//        senderTable.addCell(transaction.getSender().getFirstName() + " " + transaction.getSender().getLastName());
//        senderTable.addCell("Card Number:");
//        senderTable.addCell(transaction.getSenderAccount().getCardNumber());
//        document.add(senderTable);
//
//        // Receiver Information Table
//        document.add(new Paragraph("Receiver Information:").setBold().setMarginTop(20f));
//        Table receiverTable = new Table(UnitValue.createPercentArray(new float[]{2, 3})).useAllAvailableWidth();
//        receiverTable.addCell("Name:");
//        receiverTable.addCell(transaction.getReceiver().getFirstName() + " " + transaction.getReceiver().getLastName());
//        receiverTable.addCell("Card Number:");
//        receiverTable.addCell(transaction.getReceiverAccount().getCardNumber());
//        document.add(receiverTable);
//
//        // Footer
//        document.add(new Paragraph("Thank you for using our service!").setTextAlignment(TextAlignment.CENTER).setMarginTop(20f));
//        document.add(new Paragraph("Best Regards,\nBankMthree Team").setTextAlignment(TextAlignment.CENTER));
//
//        // Close the document
//        document.close();
//
//        // Return PDF as ByteArrayDataSource for email attachment
//        return new ByteArrayDataSource(outputStream.toByteArray(), "application/pdf");
//    }
