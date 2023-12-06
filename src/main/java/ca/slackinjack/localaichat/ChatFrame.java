package ca.slackinjack.localaichat;

import ca.slackinjack.localaichat.service.LocalAIService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ChatFrame extends javax.swing.JFrame {

    private String apiKey = "xxx";
    private String address = "http://localhost:8080/v1/";
    private Duration timeout = Duration.ofSeconds(300);
    private String chatModel = "jordan-7b";
    private String compModel = "jordan-7b";

    private LocalAIService service;
    private static final JLabelColumnRenderer assistantColumnRenderer = new JLabelColumnRenderer();
    private static final JLabelColumnRenderer userColumnRenderer = new JLabelColumnRenderer();
    
    public ChatFrame() {
        initComponents();
        this.service = new LocalAIService(apiKey, address, timeout);
        this.tableChatLog.getColumnModel().getColumn(0).setCellRenderer(assistantColumnRenderer);
        this.tableChatLog.getColumnModel().getColumn(1).setCellRenderer(userColumnRenderer);
    }

    public void sendChat(String promptIn) {
        switch (promptIn) {
            case "chatmodel":
                break;
            case "compmodel":
                break;
            case "help":
                break;
            default:
                this.addTextArea(this.getResponse(this.chatModel, promptIn), false);
                break;
        }
    }

    private void addTextArea(String textIn, boolean isUser) {
        DefaultTableModel model = (DefaultTableModel) this.tableChatLog.getModel();
        int charSize = this.tableChatLog.getFont().getSize();
        int textWidth = textIn.length() * charSize;
        int columnWidth = Math.floorDiv(this.tableChatLog.getWidth(), 2) + 1;
        int rowHeight = textWidth > columnWidth ? (int) Math.ceil(textWidth / columnWidth) * charSize : charSize;
        
        JLabel label = new JLabel("<html>" + textIn + "</html>");
        label.setSize(columnWidth, rowHeight);
        if (isUser) {
            assistantColumnRenderer.addLabel(new JLabel());
            userColumnRenderer.addLabel(label);

        } else {
            assistantColumnRenderer.addLabel(label);
            userColumnRenderer.addLabel(new JLabel());
        }
        model.addRow(new Object[]{});

        this.tableChatLog.setRowHeight(this.tableChatLog.getModel().getRowCount() - 1, rowHeight);
    }

    public String getResponse(String modelIn, String promptIn) {
        List<ChatMessage> messages = new ArrayList();
        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setRole("system");
        systemMessage.setContent(": " + "You are a helpful assistant.");

        ChatMessage userMessage = new ChatMessage();
        userMessage.setRole("user");
        userMessage.setContent(": " + promptIn);
        
        ChatMessage assistantMessage = new ChatMessage();
        assistantMessage.setRole("assistant");
        assistantMessage.setContent(": ");

        messages.add(systemMessage);
        messages.add(userMessage);
        messages.add(assistantMessage);
        ChatCompletionRequest completion = ChatCompletionRequest.builder()
                .model(modelIn)
                .messages(messages)
                //.stream(true)
                .build();

        return this.service.createChatCompletion(completion).getChoices().get(0).getMessage().getContent();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textareaInput = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableChatLog = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        textareaInput.setColumns(20);
        textareaInput.setRows(5);
        textareaInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textareaInputKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(textareaInput);

        tableChatLog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Assistant", "User"
            }
        ));
        jScrollPane3.setViewportView(tableChatLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textareaInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textareaInputKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String promptIn = this.textareaInput.getText();
            this.textareaInput.setText(null);
            this.addTextArea(promptIn, true);
            this.sendChat(promptIn);
        }
    }//GEN-LAST:event_textareaInputKeyPressed

    /*public void setArguments(String[] args) {
        for (String s : args) {
            // key=value
            String key = s.split("=")[0];
            String val = s.split("=")[1];
        }
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tableChatLog;
    private javax.swing.JTextArea textareaInput;
    // End of variables declaration//GEN-END:variables

    private static class JLabelColumnRenderer extends DefaultTableCellRenderer {

        List<JLabel> labels = new ArrayList();

        public void addLabel(JLabel labelIn) {
            this.labels.add(labelIn);
        }
        
        public void setLabel(JLabel labelIn, int row) {
            this.labels.set(row, labelIn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this.labels.get(row);
        }
    }
}
