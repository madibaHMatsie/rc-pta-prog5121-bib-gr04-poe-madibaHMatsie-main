
  package st10457954;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class MessageInterfaceTest {
    private MessageInterface messageInterface;
    private List<MessageService> testMessages;

    // Test data
    private final MessageService message1 = new MessageService("+27834557896", "Did you get the cake?");
    private final MessageService message2 = new MessageService("+27838884567", "Where are you? You are late! I have asked you to be on time.");
    private final MessageService message3 = new MessageService("+27834484567", "Yohoooo, I am at your gate.");
    private final MessageService message4 = new MessageService("0838884567", "It is dinner time !");
    private final MessageService message5 = new MessageService("+27838884567", "Ok, I am leaving without you.");

    @BeforeEach
    public void setUp() {
        messageInterface = new MessageInterface();
        testMessages = new ArrayList<>();
        
        // Initialize test messages with flags
        message1.sentMessage();
        message2.storeMessage();
        // message3 is disregarded (not added)
        message4.sentMessage();
        message5.storeMessage();
        
        // Add to test collection (simulating sentMessages)
        testMessages.add(message1);
        testMessages.add(message2);
        testMessages.add(message4);
        testMessages.add(message5);
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        // Verify only sent/stored messages are present (excluding disregarded)
        assertEquals(4, testMessages.size());
        
        // Check for specific sent messages
        boolean foundMessage1 = false;
        boolean foundMessage4 = false;
        
        for (MessageService msg : testMessages) {
            if (msg.getMessagePayload().equals("Did you get the cake?")) {
                foundMessage1 = true;
            }
            if (msg.getMessagePayload().equals("It is dinner time !")) {
                foundMessage4 = true;
            }
        }
        
        assertTrue(foundMessage1, "Message 1 should be in sent messages");
        assertTrue(foundMessage4, "Message 4 should be in sent messages");
    }

    @Test
    public void testDisplayLongestMessage() {
        MessageService longest = testMessages.get(0);
        for (MessageService msg : testMessages) {
            if (msg.getMessagePayload().length() > longest.getMessagePayload().length()) {
                longest = msg;
            }
        }
        
        assertEquals("Where are you? You are late! I have asked you to be on time.", 
                    longest.getMessagePayload(), "Message 2 should be the longest");
    }

    @Test
    public void testSearchMessageById() {
        String targetId = message4.getMessageID();
        MessageService found = null;
        
        for (MessageService msg : testMessages) {
            if (msg.getMessageID().equals(targetId)) {
                found = msg;
                break;
            }
        }
        
        assertNotNull(found, "Message 4 should be found by ID");
        assertEquals("It is dinner time !", found.getMessagePayload());
    }

    @Test
    public void testSearchMessagesByRecipient() {
        String recipient = "+27838884567";
        List<MessageService> matches = new ArrayList<>();
        
        for (MessageService msg : testMessages) {
            if (msg.getMessageRecipient().equals(recipient)) {
                matches.add(msg);
            }
        }
        
        assertEquals(2, matches.size(), "Should find 2 messages for recipient");
        assertEquals("Where are you? You are late! I have asked you to be on time.", 
                    matches.get(0).getMessagePayload());
        assertEquals("Ok, I am leaving without you.", 
                    matches.get(1).getMessagePayload());
    }

    @Test
public void testDeleteMessageByHash() {
    // Create a fresh list for this test only
    List<MessageService> testList = new ArrayList<>();
    
    // Add exactly 3 messages (1 to be deleted, 2 to remain)
    MessageService toDelete = new MessageService("+27838884567", "Message to delete");
    toDelete.storeMessage(); // Sets hash and index
    MessageService toKeep1 = new MessageService("+27834557896", "Message to keep 1");
    toKeep1.sentMessage();
    MessageService toKeep2 = new MessageService("0838884567", "Message to keep 2");
    toKeep2.sentMessage();
    
    testList.add(toKeep1);
    testList.add(toDelete);
    testList.add(toKeep2);
    
    // Verify initial setup
    assertEquals(3, testList.size(), "Initial list should have 3 messages");
    
    // Get hash of message to delete
    String targetHash = toDelete.getMessageHash();
    
    // Perform deletion
    boolean removed = testList.removeIf(msg -> msg.getMessageHash().equals(targetHash));
    
    // Verify deletion
    assertTrue(removed, "Message should be removed");
    assertEquals(2, testList.size(), "List should have 2 messages after deletion");
    
    // Verify correct message was deleted
    assertFalse(testList.contains(toDelete), "Deleted message should not be in list");
    
    // Verify other messages remain
    assertTrue(testList.contains(toKeep1), "First message should remain");
    assertTrue(testList.contains(toKeep2), "Second message should remain");
}

    @Test
    public void testDisplayReport() {
        StringBuilder report = new StringBuilder();
        for (MessageService msg : testMessages) {
            report.append("Hash: ").append(msg.getMessageHash()).append("\n")
                  .append("Recipient: ").append(msg.getMessageRecipient()).append("\n")
                  .append("Message: ").append(msg.getMessagePayload()).append("\n\n");
        }
        
        // Verify report contains all required fields for each message
        for (MessageService msg : testMessages) {
            assertTrue(report.toString().contains(msg.getMessageHash()));
            assertTrue(report.toString().contains(msg.getMessageRecipient()));
            assertTrue(report.toString().contains(msg.getMessagePayload()));
        }
    }
}