/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;
import com.email.durgesh.Email;
import java.awt.HeadlessException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;
import java.sql.*;
import javax.mail.MessagingException;
import org.apache.commons.validator.routines.EmailValidator;
/**
 *
 * @author Anirudh
 */
public class ChangeEmail extends javax.swing.JFrame
{
Connection con;
Statement stmt;
PreparedStatement pstmt;
ResultSet rs;
int OTP = (int)(Math.random()*1000000);
    /**
     * Creates new form Changemal
     */
    public ChangeEmail() {
        initComponents();
        Username_setter();
    }

    public void Username_setter()
    {
        try
        {
            con = new DesignHome().dbconn();
            String query = "Select userid,(select username from users where id = userid) Uname, (select email from users where username = uname) Email from ActivityLog order by sno desc limit 1";
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            
            String uname = "";
            String email = "";
            
            if(rs.next())
                uname = rs.getString("Uname");
                email = rs.getString("Email");
                
            OldEmailTxt.setText(uname);
            EmailTxt1.setText(email);
            
            stmt.close();
            rs.close();
            con.close();
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void updateuser()
    {
        
        try
        {
            if(OldEmailTxt.getText().equals("") || EmailTxt.getText().equals("") || OtpTxt.getText().equals(""))
            JOptionPane.showMessageDialog(null, "None of the fields can be empty");
            else
            {
                String email = EmailTxt.getText();
                if(emailValidator(email))
                {
                    String username = OldEmailTxt.getText();
                    if(new String(OldPassTxt.getPassword()).equals(""))
                        JOptionPane.showMessageDialog(null, "Enter password");
                    else
                    {
                        String pass = new String(OldPassTxt.getPassword());
                        pass = getMD5(pass);
                        String dbpass = "";

                        int uotp = Integer.parseInt(OtpTxt.getText());


                        con = new DesignHome().dbconn();
                        con.setAutoCommit(false);

                       String query1 = "select password from users where username = '" + username +"'"; 
                       stmt = con.createStatement();
                       rs = stmt.executeQuery(query1);
                       if(rs.next())
                       {
                           dbpass = rs.getString(1);


                        if(pass.equals(dbpass))
                        {
                            if(OTP == uotp)
                            {                                
                                String query2 = "update users set email = ? where username = '" + username +"'";
                                pstmt = con.prepareStatement(query2);
                                pstmt.setString(1, email);

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
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Invalid OTP");
                        }
                        else
                             JOptionPane.showMessageDialog(null, "Invalid Credentials!");
                   }
                   else
                       JOptionPane.showMessageDialog(null, "User does not exist");
                }
            }
            else
            JOptionPane.showMessageDialog(null, "Email ID not valid");
            }    
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Error : " + e.getMessage());
        }
    }
    
    public boolean emailValidator(String email)
    {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
    
    
    public void sendmail(String recipient, int otp)
    {
        try
        {
        
        System.out.println(OTP);
        String myemail = "auth.hardwareassetregister@gmail.com";
        String password = "Auth.Hardware";
       
            Email email= new Email(myemail, password);
            email.setFrom(myemail, "Hardware Asset Register");
            email.setSubject("Hardware Asset Register Authentication");
            email.setContent("<h1>Your OTP for registration is " + otp + "</h1>", "text/html");
            
            email.addRecipient(recipient);
            email.send();
            JOptionPane.showMessageDialog(null, "An OTP has been sent to " + recipient + ". Please check your E-Mail and enter the OTP in the text field given below.");
        
        }
        catch (HeadlessException | UnsupportedEncodingException | MessagingException ex) 
        {
            JOptionPane.showMessageDialog(null, "Error Sending E-Mail. " + ex.getMessage());
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

        jPanel1 = new javax.swing.JPanel();
        RegisterBtn = new javax.swing.JButton();
        ResultLbl = new javax.swing.JLabel();
        BackBtn1 = new javax.swing.JButton();
        ClockLbl = new javax.swing.JLabel();
        HeadingLbl = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        EmailTxt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        OtpTxt = new javax.swing.JTextField();
        SendOtpBtn = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        OldEmailTxt = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        ViewOldPassBtn = new javax.swing.JButton();
        OldPassTxt = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        EmailTxt1 = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 700));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setMinimumSize(new java.awt.Dimension(600, 600));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        RegisterBtn.setBackground(new java.awt.Color(0, 102, 204));
        RegisterBtn.setForeground(new java.awt.Color(255, 255, 255));
        RegisterBtn.setText("Update ");
        RegisterBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegisterBtnActionPerformed(evt);
            }
        });
        jPanel1.add(RegisterBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 530, 280, 50));

        ResultLbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ResultLbl.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(ResultLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 554, -1, -1));

        BackBtn1.setBackground(new java.awt.Color(0, 102, 204));
        BackBtn1.setForeground(new java.awt.Color(255, 255, 255));
        BackBtn1.setText("Cancel");
        BackBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackBtn1ActionPerformed(evt);
            }
        });
        jPanel1.add(BackBtn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 610, 100, 35));

        ClockLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ClockLbl.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(ClockLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 160, 20));

        HeadingLbl.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 24)); // NOI18N
        HeadingLbl.setForeground(new java.awt.Color(255, 255, 255));
        HeadingLbl.setText("Change Email");
        jPanel1.add(HeadingLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Enter New Email :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 350, -1, -1));

        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, 280, -1));

        EmailTxt.setBackground(new java.awt.Color(51, 51, 51));
        EmailTxt.setForeground(new java.awt.Color(255, 255, 255));
        EmailTxt.setBorder(null);
        EmailTxt.setCaretColor(new java.awt.Color(255, 255, 255));
        EmailTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmailTxtActionPerformed(evt);
            }
        });
        jPanel1.add(EmailTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 380, 280, 27));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Enter OTP: ");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 440, -1, -1));

        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 500, 280, -1));

        OtpTxt.setBackground(new java.awt.Color(51, 51, 51));
        OtpTxt.setForeground(new java.awt.Color(255, 255, 255));
        OtpTxt.setBorder(null);
        OtpTxt.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel1.add(OtpTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 470, 280, 27));

        SendOtpBtn.setBackground(new java.awt.Color(0, 102, 204));
        SendOtpBtn.setForeground(new java.awt.Color(255, 255, 255));
        SendOtpBtn.setText("Send OTP");
        SendOtpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendOtpBtnActionPerformed(evt);
            }
        });
        jPanel1.add(SendOtpBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 380, 100, 30));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Enter User Name :");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, -1, -1));

        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 280, -1));

        OldEmailTxt.setBackground(new java.awt.Color(51, 51, 51));
        OldEmailTxt.setForeground(new java.awt.Color(255, 255, 255));
        OldEmailTxt.setBorder(null);
        OldEmailTxt.setCaretColor(new java.awt.Color(255, 255, 255));
        OldEmailTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OldEmailTxtActionPerformed(evt);
            }
        });
        jPanel1.add(OldEmailTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 280, 27));

        jSeparator5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 230, 280, -1));

        ViewOldPassBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/show_password_32px.png"))); // NOI18N
        ViewOldPassBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ViewOldPassBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ViewOldPassBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ViewOldPassBtnMouseReleased(evt);
            }
        });
        ViewOldPassBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewOldPassBtnActionPerformed(evt);
            }
        });
        jPanel1.add(ViewOldPassBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 200, 30, 27));

        OldPassTxt.setBackground(new java.awt.Color(51, 51, 51));
        OldPassTxt.setForeground(new java.awt.Color(255, 255, 255));
        OldPassTxt.setBorder(null);
        OldPassTxt.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel1.add(OldPassTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, 280, 27));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Enter Password :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, -1, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Old Email:");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 260, -1, -1));

        EmailTxt1.setEditable(false);
        EmailTxt1.setBackground(new java.awt.Color(51, 51, 51));
        EmailTxt1.setForeground(new java.awt.Color(255, 255, 255));
        EmailTxt1.setBorder(null);
        EmailTxt1.setCaretColor(new java.awt.Color(255, 255, 255));
        EmailTxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmailTxt1ActionPerformed(evt);
            }
        });
        jPanel1.add(EmailTxt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 280, 27));

        jSeparator6.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 320, 280, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void SendOtpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendOtpBtnActionPerformed
          verifymail();        
    }//GEN-LAST:event_SendOtpBtnActionPerformed

    public void verifymail()
            {
                
            String email = EmailTxt.getText();
        String query = "Select username from users where email = '" + email + "'";
        try
        {
        con = new DesignHome().dbconn();
        stmt = con.createStatement();
        rs = stmt.executeQuery(query);
        if(rs.next())
            JOptionPane.showMessageDialog(null, "The Email ID Entered Is Already Signed Up With A Different Account.");
        else if(EmailTxt.getText().equals(""))
            JOptionPane.showMessageDialog(null, "Please Enter Your Email Address");
        else if(!emailValidator(EmailTxt.getText()))
            JOptionPane.showMessageDialog(null, "The Email ID Entered Is Invalid.");
        else
            sendmail(email, OTP);
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }        
            }
    private void RegisterBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegisterBtnActionPerformed
        updateuser();
    }//GEN-LAST:event_RegisterBtnActionPerformed

    private void BackBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackBtn1ActionPerformed
        // TODO add your handling code here:
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel the process? You will be redirected to the previous window.", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if(response == JOptionPane.YES_OPTION)
        {
            this.dispose();
        }

        else if(response == JOptionPane.NO_OPTION)
        {
            this.setVisible(true);
        }
    }//GEN-LAST:event_BackBtn1ActionPerformed

    private void EmailTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmailTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailTxtActionPerformed

    private void OldEmailTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OldEmailTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OldEmailTxtActionPerformed

    private void ViewOldPassBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewOldPassBtnMousePressed
        // TODO add your handling code here:
        OldPassTxt.setEchoChar((char)0);
    }//GEN-LAST:event_ViewOldPassBtnMousePressed

    private void ViewOldPassBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewOldPassBtnMouseReleased
        // TODO add your handling code here:
        OldPassTxt.setEchoChar('*');
    }//GEN-LAST:event_ViewOldPassBtnMouseReleased

    private void ViewOldPassBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewOldPassBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ViewOldPassBtnActionPerformed

    private void EmailTxt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmailTxt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailTxt1ActionPerformed

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
            java.util.logging.Logger.getLogger(ChangeEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChangeEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChangeEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChangeEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChangeEmail().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BackBtn1;
    private javax.swing.JLabel ClockLbl;
    private javax.swing.JTextField EmailTxt;
    private javax.swing.JTextField EmailTxt1;
    private javax.swing.JLabel HeadingLbl;
    private javax.swing.JTextField OldEmailTxt;
    private javax.swing.JPasswordField OldPassTxt;
    private javax.swing.JTextField OtpTxt;
    private javax.swing.JButton RegisterBtn;
    private javax.swing.JLabel ResultLbl;
    private javax.swing.JButton SendOtpBtn;
    private javax.swing.JButton ViewOldPassBtn;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    // End of variables declaration//GEN-END:variables
}
