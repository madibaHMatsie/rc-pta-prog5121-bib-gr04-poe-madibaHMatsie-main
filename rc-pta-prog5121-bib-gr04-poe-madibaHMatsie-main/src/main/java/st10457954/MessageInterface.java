package st10457954;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class MessageInterface {
    private static List<MessageService> sentMessages = new ArrayList<>();

    /**
     * Starts and manages the interactive messaging session.
     */
    public void startMessagingInteraction() {
        JOptionPane.showMessageDialog(null,
                "Welcome to QuickChat v2",
                "Part 2", JOptionPane.INFORMATION_MESSAGE);

        boolean continueMessaging = true;
        while (continueMessaging) {
            String choice = JOptionPane.showInputDialog(null,
                    "Messaging Menu:\n1. Send Messages\n2. Show recently sent messages\n3. Quit",
                    "Main Menu", JOptionPane.QUESTION_MESSAGE);

            if (choice == null) {
                continueMessaging = false;
                break;
            }

            switch (choice) {
                case "1":
                    processMessageBatch();
                    break;
                case "2":
                    showRecentMessages();
                    break;
                case "3":
                    continueMessaging = false;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice.", "Menu Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        JOptionPane.showMessageDialog(null, "Exiting messaging feature.", "QuickChat Messaging", JOptionPane.INFORMATION_MESSAGE);
    }

    private void processMessageBatch() {
        String numMessagesStr = JOptionPane.showInputDialog(null,
                "How many messages would you like to process?",
                "Number of Messages", JOptionPane.QUESTION_MESSAGE);

        if (numMessagesStr == null || numMessagesStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Number of messages not provided.", "Input Missing", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int numMessages;
        try {
            numMessages = Integer.parseInt(numMessagesStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number entered.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (numMessages <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < numMessages; i++) {
            processSingleMessage(i + 1, numMessages);
        }
        
        JOptionPane.showMessageDialog(null, 
                "Part 3","Welcome to QuickChat v3", JOptionPane.INFORMATION_MESSAGE);

        showReportMenu();
        JOptionPane.showMessageDialog(null, "Batch processing complete. Total messages sent: " + MessageService.returnTotalMessages(), 
                "Batch Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void processSingleMessage(int current, int total) {
        JOptionPane.showMessageDialog(null, "Processing Message " + current + " of " + total, 
                "Message Progress", JOptionPane.INFORMATION_MESSAGE);

        String recipient = JOptionPane.showInputDialog(null, "Enter recipient's cell number (e.g., +27718693002):", 
                "Message " + current + " - Recipient", JOptionPane.PLAIN_MESSAGE);
        if (recipient == null) return;

        String payload = JOptionPane.showInputDialog(null, "Enter message payload:", 
                "Message " + current + " - Payload", JOptionPane.PLAIN_MESSAGE);
        if (payload == null) return;

        MessageService message = new MessageService(recipient, payload);
        JOptionPane.showMessageDialog(null, message.getGeneratedIdNotification(), "Message ID", JOptionPane.INFORMATION_MESSAGE);

        if (!validateMessage(message, current)) return;

        handleMessageAction(message, current);
    }

    private boolean validateMessage(MessageService message, int current) {
        String recipientValidation = message.validateRecipientNumber(message.getMessageRecipient());
        if (!recipientValidation.equals("Cell phone number successfully captured.")) {
            JOptionPane.showMessageDialog(null, "Validation Failed:\n" + recipientValidation, 
                    "Recipient Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String payloadValidation = message.validatePayloadLength(message.getMessagePayload());
        if (!payloadValidation.equals("Message ready to send.")) {
            JOptionPane.showMessageDialog(null, "Validation Failed:\n" + payloadValidation, 
                    "Payload Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void handleMessageAction(MessageService message, int current) {
        String[] options = {"Send Message", "Store Message", "Disregard Message"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose an action:\nTo: " + message.getMessageRecipient() + 
                "\nMessage: " + message.getMessagePayload().substring(0, Math.min(message.getMessagePayload().length(), 50)) + "...",
                "Message " + current + " - Action",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0:
                String sendResult = message.sentMessage();
                JOptionPane.showMessageDialog(null, sendResult, "Send Status", JOptionPane.INFORMATION_MESSAGE);
                if (sendResult.equals("Message successfully sent.")) {
                    sentMessages.add(message);
                    JOptionPane.showMessageDialog(null, "Details:\n" + getMessageDetails(message), 
                            "Sent Message Details", JOptionPane.INFORMATION_MESSAGE);
                    message.storeMessage();
                }
                break;
            case 1:
                String storeResult = message.storeMessage();
                JOptionPane.showMessageDialog(null, storeResult, "Store Status", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "Message disregarded.", "Disregarded", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    private void showReportMenu() {
        String reportOption = JOptionPane.showInputDialog(null, 
            "Message Reports:\n" +
            "a. Display sender and recipient of all sent messages\n" +
            "b. Display the longest sent message\n" + 
            "c. Search for a message by ID\n" +
            "d. Search messages by recipient\n" +
            "e. Delete a message by hash\n" +
            "f. Display full details of all sent messages\n" +
            "Enter your choice:",
            "Message Reports",
            JOptionPane.PLAIN_MESSAGE);

        if (reportOption == null) return;

        switch (reportOption.toLowerCase()) {
            case "a":
                displaySenderRecipients();
                break;
            case "b":
                displayLongestMessage();
                break;
            case "c":
                searchMessageById();
                break;
            case "d":
                searchMessageByRecipient();
                break;
            case "e":
                deleteMessageByHash();
                break;
            case "f":
                displayFullMessageDetails();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid option");
        }
    }

    private void displaySenderRecipients() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages sent yet.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (MessageService msg : sentMessages) {
            sb.append("ID: ").append(msg.getMessageID())
              .append(", Recipient: ").append(msg.getMessageRecipient())
              .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Sender and Recipients", JOptionPane.INFORMATION_MESSAGE);
    }

    private void displayLongestMessage() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages sent yet.");
            return;
        }

        MessageService longest = sentMessages.get(0);
        for (MessageService msg : sentMessages) {
            if (msg.getMessagePayload().length() > longest.getMessagePayload().length()) {
                longest = msg;
            }
        }

        JOptionPane.showMessageDialog(null, 
            "Longest Message (" + longest.getMessagePayload().length() + " chars):\n" +
            "Recipient: " + longest.getMessageRecipient() + "\n" +
            "Payload: " + longest.getMessagePayload(),
            "Longest Message", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchMessageById() {
        String id = JOptionPane.showInputDialog(null, "Enter message ID to search:", 
                "Search by ID", JOptionPane.PLAIN_MESSAGE);
        if (id == null || id.trim().isEmpty()) return;

        for (MessageService msg : sentMessages) {
            if (msg.getMessageID().equals(id)) {
                JOptionPane.showMessageDialog(null, getMessageDetails(msg), 
                        "Message Found", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Message with ID " + id + " not found.", 
                "Not Found", JOptionPane.WARNING_MESSAGE);
    }

    private void searchMessageByRecipient() {
        String recipient = JOptionPane.showInputDialog(null, "Enter recipient number to search:", 
                "Search by Recipient", JOptionPane.PLAIN_MESSAGE);
        if (recipient == null || recipient.trim().isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        for (MessageService msg : sentMessages) {
            if (msg.getMessageRecipient().equals(recipient)) {
                sb.append(getMessageDetails(msg)).append("\n\n");
            }
        }

        if (sb.length() > 0) {
            JOptionPane.showMessageDialog(null, sb.toString(), 
                    "Messages for " + recipient, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No messages found for " + recipient, 
                    "Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteMessageByHash() {
        String hash = JOptionPane.showInputDialog(null, "Enter message hash to delete:", 
                "Delete by Hash", JOptionPane.PLAIN_MESSAGE);
        if (hash == null || hash.trim().isEmpty()) return;

        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).getMessageHash().equals(hash)) {
                MessageService removed = sentMessages.remove(i);
                JOptionPane.showMessageDialog(null, "Message deleted:\n" + getMessageDetails(removed), 
                        "Deleted", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Message with hash " + hash + " not found.", 
                "Not Found", JOptionPane.WARNING_MESSAGE);
    }

    private void displayFullMessageDetails() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages sent yet.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (MessageService msg : sentMessages) {
            sb.append(getMessageDetails(msg)).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), 
                "All Message Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private String getMessageDetails(MessageService msg) {
        return "ID: " + msg.getMessageID() + "\n" +
               "Recipient: " + msg.getMessageRecipient() + "\n" +
               "Payload: " + msg.getMessagePayload() + "\n" +
               "Hash: " + msg.getMessageHash() + "\n" +
               "Index: " + msg.getMessageIndex();
    }

    private void showRecentMessages() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages sent yet.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        int start = Math.max(0, sentMessages.size() - 5); // Show last 5 messages
        for (int i = start; i < sentMessages.size(); i++) {
            sb.append(getMessageDetails(sentMessages.get(i))).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), 
                "Recent Messages", JOptionPane.INFORMATION_MESSAGE);
    }
}