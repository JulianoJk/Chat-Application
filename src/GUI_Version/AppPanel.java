package GUI_Version;

import javax.swing.*;
import java.awt.*;

public class AppPanel extends JPanel {
    private JTextArea textArea;

    public AppPanel() {
        textArea = new JTextArea(" ");

        // Wrap the text in order to go in a new line
        textArea.setLineWrap(true);

        // Make it not to cut the words
        textArea.setWrapStyleWord(true);

        // Change the background
        textArea.setBackground(Color.black);

        // Create a background
        textArea.setBorder(BorderFactory.createBevelBorder(1));

        // Change the fonts color
        textArea.setForeground(Color.WHITE);

        // Change the font style
        textArea.setFont(new Font("Comic Sans", Font.PLAIN, 20));

        // Disable the option for the user to edit the text
        textArea.setEditable(false);

        //Set an auto-scrolling option; When the user types anything and reaches the panel limit, scroll automatically
        textArea.getCaret().setDot(Integer.MAX_VALUE);

        // Add a scroll bar
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Set the size for the textArea now
        scrollPane.setPreferredSize(new Dimension(400, 400));

        //Place the scrollbar
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // add the scrollbar which contains also the text area to the panel
        add(scrollPane);

    }


    // Append what the user typed in a new line, without overwriting the same text
    public void setTextLabel(String label) {
        // Check if the text is empty or null. If it contains characters, append it, otherwise do nothing
        if (!(label.isEmpty() || label.isBlank())) {
            textArea.append(label + "\n");
        }
    }

}
