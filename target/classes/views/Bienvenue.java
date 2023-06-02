package com.nale.newslettermanagement.views;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

public class Bienvenue extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public Bienvenue() {
		initComponents();
	}
	
	public static void run() {
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Bienvenue.class.getName()).log(Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> new Bienvenue().setVisible(true));
	}
	
	
	private void initComponents() {
		JPanel jPanel = new JPanel();
		jPanel.setName("Welcome Nate");
		jPanel.setToolTipText(getName());
		
        JLabel title = new JLabel();
        title.setFont(new Font("Poppins", 1, 25)); 
        title.setText("Bienvenue sur NATE");
        title.setHorizontalAlignment(HEIGHT);
        
        JLabel description = new javax.swing.JLabel();
        description.setFont(new Font("Poppins", 1, 16));
        description.setText("Votre gestionnaire de NewsLetters");
        description.setHorizontalAlignment(HEIGHT);
        
        JButton register = new JButton();
        register.setFont(new Font("Poppins", 1, 15));
        register.setBackground(Color.gray);
        register.setText("Configurer votre compte");
        
        JButton goToData = new JButton();
        goToData.setFont(new Font("Poppins", 1, 15));
        goToData.setBackground(Color.PINK);
        goToData.setText("Consulter le dashboard");
        
        jPanel.add(title);
        jPanel.add(description);
        jPanel.add(register);
        jPanel.add(goToData);
        jPanel.setAlignmentY(CENTER_ALIGNMENT);
        jPanel.setAlignmentX(CENTER_ALIGNMENT);
        
        /*GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(
                    		jPanel, 
                    		javax.swing.GroupLayout.PREFERRED_SIZE, 
                    		javax.swing.GroupLayout.DEFAULT_SIZE, 
                    		javax.swing.GroupLayout.PREFERRED_SIZE
                    )
                    .addGap(0, 35, Short.MAX_VALUE))
            );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(
                		jPanel, 
                		javax.swing.GroupLayout.PREFERRED_SIZE, 
                		javax.swing.GroupLayout.DEFAULT_SIZE, 
                		javax.swing.GroupLayout.PREFERRED_SIZE
                )
                .addGap(0, 158, Short.MAX_VALUE))
        );
        */
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        pack();
	}

}
