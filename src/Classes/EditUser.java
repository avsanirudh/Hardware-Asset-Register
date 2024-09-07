/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import static java.lang.Thread.sleep;
import java.sql.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import javax.swing.*;
/**
 *
 * @author Anirudh
 */
public class EditUser extends javax.swing.JFrame
{
Connection con;
ResultSet rs;
Statement stmt;
PreparedStatement pstmt;
    /**
     * Creates new form EditUser
     */
    public EditUser() {
        initComponents();
        clock();
    }

    public void clock()
    {
        Thread clock = new Thread()
        {
            public void run()
            {
                try 
                {
                    for(;;)
                    {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
                        java.util.Date date = new java.util.Date();
                        ClockLbl.setText(sdf.format(date)); 
                        sleep(1000);
                    }
                }
                catch (InterruptedException ex) 
                {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }        
        };
        clock.start();
    }
    
    public void getdet(String userdet, int IsAdmin, String approved, String Active)
    {
        NameTxt.setText(userdet);
        if(IsAdmin == 1)
        {
            IsAdminCB.setSelected(true);
        }
        else if(IsAdmin == 0)
        {
            IsAdminCB.setSelected(false);
        }
        
        if(approved.equals("True"))
        {
            ApproveCB.setSelected(true);
        }
        else if(approved.equals("False"))
        {
            ApproveCB.setSelected(false);
        }
        
        if(Active.equals("Yes"))
        {
            ActiveCB.setSelected(true);
        }
        else
        {
            ActiveCB.setSelected(false);
        }
    }
    
    public void updateuser()
    {
        try
        {
            con = new DesignHome().dbconn();
            con.setAutoCommit(false);
            System.out.println("Connection Established");

            String userdet = NameTxt.getText();
            int count = 0;
            String IsAdmin;
            String Activated;
            String active;
            
            if(IsAdminCB.isSelected())
            {
                IsAdmin = "True";
            }
            
            else
            {
                IsAdmin = "False";
            }

            if(ApproveCB.isSelected())
            {
                Activated = "True";
            }
            
            else
            {
                Activated = "False";
            }
            
            if(ActiveCB.isSelected())
                active = "Yes";
            else
                active = "No";
            
                        
            
            String query2 = "Update users set IsAdmin = ?, Activated = ?, Active = ? where UserName = '" + userdet + "'";

            pstmt = con.prepareStatement(query2);
            pstmt.setString(1, IsAdmin);
            pstmt.setString(2, Activated);
            pstmt.setString(3, active);

            System.out.println(query2);

            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to make these changes to this user?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if(response == JOptionPane.YES_OPTION)
            {
                pstmt.executeUpdate();
                con.commit();
                System.out.println("pstmt Executed");
                JOptionPane.showMessageDialog(null, "Updated Successfully!");
                this.dispose();
            }

            else if(response == JOptionPane.NO_OPTION)
            {
                JOptionPane.showMessageDialog(null, "Action Cancelled");
            }

             try
                {
                    pstmt.close();                    
                    con.close();
                }
            catch(SQLException e)
            {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
            
        catch(SQLException e)
            {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
    }
    
    public static String getMD5(String input) 
    { 
        try { 
  
            // Static getInstance method is called with hashing MD5 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
  
            // digest() method is called to calculate message digest 
            //  of an input digest() return array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            return hashtext; 
        }  
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        RegisterBtn = new javax.swing.JButton();
        ResultLbl = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        BackBtn1 = new javax.swing.JButton();
        IsAdminCB = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        NameTxt = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        ClockLbl = new javax.swing.JLabel();
        HeadingLbl = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        ApproveCB = new javax.swing.JCheckBox();
        ActiveCB = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();

        jRadioButton1.setText("jRadioButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 700));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setMinimumSize(new java.awt.Dimension(600, 600));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        RegisterBtn.setBackground(new java.awt.Color(0, 102, 204));
        RegisterBtn.setForeground(new java.awt.Color(255, 255, 255));
        RegisterBtn.setText("Update");
        RegisterBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegisterBtnActionPerformed(evt);
            }
        });
        jPanel1.add(RegisterBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 530, 221, 48));

        ResultLbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ResultLbl.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(ResultLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 548, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Username:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 220, -1, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Select if the user is an admin :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 400, -1, -1));

        BackBtn1.setBackground(new java.awt.Color(0, 102, 204));
        BackBtn1.setForeground(new java.awt.Color(255, 255, 255));
        BackBtn1.setText("Cancel");
        BackBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackBtn1ActionPerformed(evt);
            }
        });
        jPanel1.add(BackBtn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 600, 134, 45));

        IsAdminCB.setBackground(new java.awt.Color(0, 102, 204));
        IsAdminCB.setForeground(new java.awt.Color(255, 255, 255));
        IsAdminCB.setText("Admin");
        jPanel1.add(IsAdminCB, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 400, -1, -1));

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 280, 280, -1));

        NameTxt.setEditable(false);
        NameTxt.setBackground(new java.awt.Color(51, 51, 51));
        NameTxt.setForeground(new java.awt.Color(255, 255, 255));
        NameTxt.setBorder(null);
        NameTxt.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel1.add(NameTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, 280, 27));

        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 430, 280, -1));

        ClockLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ClockLbl.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(ClockLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 160, 20));

        HeadingLbl.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 24)); // NOI18N
        HeadingLbl.setForeground(new java.awt.Color(255, 255, 255));
        HeadingLbl.setText("Edit User");
        jPanel1.add(HeadingLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Select to approve the user:");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, -1, -1));

        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 350, 280, -1));

        ApproveCB.setBackground(new java.awt.Color(0, 102, 204));
        ApproveCB.setForeground(new java.awt.Color(255, 255, 255));
        ApproveCB.setText("Approve");
        ApproveCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApproveCBActionPerformed(evt);
            }
        });
        jPanel1.add(ApproveCB, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 320, -1, -1));

        ActiveCB.setBackground(new java.awt.Color(0, 102, 204));
        ActiveCB.setForeground(new java.awt.Color(255, 255, 255));
        ActiveCB.setText("Active");
        jPanel1.add(ActiveCB, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 470, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Select if the user is active:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 470, -1, -1));

        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 500, 280, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void RegisterBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegisterBtnActionPerformed
         updateuser();
    }//GEN-LAST:event_RegisterBtnActionPerformed

    private void BackBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackBtn1ActionPerformed
        // TODO add your handling code here:
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel the process?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
                                if(response == JOptionPane.YES_OPTION)
                                {
                                    this.dispose();
                                }

                                else if(response == JOptionPane.NO_OPTION)
                                {
                                    this.setVisible(true);
                                }
        
    }//GEN-LAST:event_BackBtn1ActionPerformed

    private void ApproveCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApproveCBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ApproveCBActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EditUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ActiveCB;
    private javax.swing.JCheckBox ApproveCB;
    private javax.swing.JButton BackBtn1;
    private javax.swing.JLabel ClockLbl;
    private javax.swing.JLabel HeadingLbl;
    private javax.swing.JCheckBox IsAdminCB;
    private javax.swing.JTextField NameTxt;
    private javax.swing.JButton RegisterBtn;
    private javax.swing.JLabel ResultLbl;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    // End of variables declaration//GEN-END:variables
}
