package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SystemPanel extends JPanel {

    public SystemPanel() {

        setLayout(
                new BorderLayout()
        );


        setBackground(
                UITheme.BACKGROUND
        );


        createHeader();

        createContent();
    }


    // =========================================
    // HEADER
    // =========================================

    private void createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );


        header.setBackground(
                Color.WHITE
        );


        header.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        20,
                        30
                )
        );


        JLabel title =
                new JLabel(
                        "System Information"
                );


        title.setFont(
                UITheme.TITLE_FONT
        );


        JLabel subtitle =
                new JLabel(
                        "Data structures used in the Hospital Management System"
                );


        subtitle.setFont(
                UITheme.SUBTITLE_FONT
        );


        subtitle.setForeground(
                UITheme.TEXT_SECONDARY
        );


        JPanel textPanel =
                new JPanel();


        textPanel.setBackground(
                Color.WHITE
        );


        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );


        textPanel.add(title);


        textPanel.add(
                Box.createVerticalStrut(5)
        );


        textPanel.add(subtitle);


        header.add(
                textPanel,
                BorderLayout.WEST
        );


        add(
                header,
                BorderLayout.NORTH
        );
    }


    // =========================================
    // CONTENT
    // =========================================

    private void createContent() {

        JPanel content =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                20,
                                20
                        )
                );


        content.setBackground(
                UITheme.BACKGROUND
        );


        content.setBorder(
                new EmptyBorder(
                        30,
                        30,
                        30,
                        30
                )
        );


        content.add(
                createStructureCard(

                        "👤 Patient Registry",

                        "Record Registry",

                        "Stores and manages patient records efficiently.",

                        "Used for: Add, Search, Update and Delete patients"
                )
        );


        content.add(
                createStructureCard(

                        "👨‍⚕️ Doctor Management",

                        "Hash Table",

                        "Provides fast doctor lookup using unique IDs.",

                        "Used for: Doctor registration and searching"
                )
        );


        content.add(
                createStructureCard(

                        "📅 Appointment Queue",

                        "Queue (FIFO)",

                        "First patient added is the first patient served.",

                        "Used for: Appointment waiting queue"
                )
        );


        content.add(
                createStructureCard(

                        "🚨 Emergency Triage",

                        "Max Heap",

                        "Patients with higher severity receive higher priority.",

                        "Used for: Emergency patient prioritization"
                )
        );


        add(
                content,
                BorderLayout.CENTER
        );
    }


    // =========================================
    // STRUCTURE CARD
    // =========================================

    private JPanel createStructureCard(

            String title,

            String structure,

            String description,

            String usage
    ) {

        JPanel card =
                new JPanel();


        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );


        card.setBackground(
                UITheme.CARD
        );


        card.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                UITheme.BORDER
                        ),

                        new EmptyBorder(
                                25,
                                25,
                                25,
                                25
                        )
                )
        );


        JLabel titleLabel =
                new JLabel(title);


        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );


        JLabel structureLabel =
                new JLabel(structure);


        structureLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );


        structureLabel.setForeground(
                UITheme.PRIMARY
        );


        JLabel descriptionLabel =
                new JLabel(
                        "<html>"
                                + description
                                + "</html>"
                );


        descriptionLabel.setFont(
                UITheme.NORMAL_FONT
        );


        descriptionLabel.setForeground(
                UITheme.TEXT_SECONDARY
        );


        JLabel usageLabel =
                new JLabel(
                        "<html><b>"
                                + usage
                                + "</b></html>"
                );


        usageLabel.setFont(
                UITheme.NORMAL_FONT
        );


        card.add(titleLabel);


        card.add(
                Box.createVerticalStrut(20)
        );


        card.add(structureLabel);


        card.add(
                Box.createVerticalStrut(20)
        );


        card.add(descriptionLabel);


        card.add(
                Box.createVerticalGlue()
        );


        card.add(usageLabel);


        return card;
    }
}