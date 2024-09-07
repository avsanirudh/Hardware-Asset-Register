
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import static java.lang.Thread.sleep;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.util.ArrayList;
/**
 *
 * @author Anirudh
 */
public class ModifyAsset extends javax.swing.JFrame
{
    Connection con;
    Statement stmt;
    Statement stmt2;
    PreparedStatement pstmt;
    ResultSet rs;
    /**
     * Creates new form ModifyAsset
     */
    public ModifyAsset() {
        initComponents();
        clock();
    }

    ArrayList<String> info = new ArrayList<>();
    ArrayList<String> newinfo = new ArrayList<>();
    
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
    
    public String getdet(String s)
    {
        FindAsset(s);
        System.out.println("getdet = " + s);
        return s;
    }
    
    public void FindAsset(String det)
    {
        try
        {
            
//            AssetDetTxt.setText(det);
            String AssetDetail = det;
            System.out.println("Det = " + AssetDetail);
            con = new DesignHome().dbconn();
            con.setAutoCommit(false);
            System.out.println("Connection Established");
            
            
            
            int count = 0;
            String assetname, assetid, invoicenum , assettype , make , serialnum , floor;
            
            String query1 = "Select a.AssetID, a.AssetName, a.InvoiceNumber, m.description AssetType, n.description Make, a.SerialNumber, a.Floor, (select username from users where id = a.AddedBy) AddedBy, a.AddedOn, a.LastModifiedBy, a.LastModifiedOn from assets a, masterdata m, masterdata n where a.assettype=m.code and m.category='A' and a.make = n.code and n.category = 'M' and (a.assetname like '%" + AssetDetail + "%' or a.assetid = " + AssetDetail + ") order by assetid asc";
            stmt = con.createStatement();
            rs = stmt.executeQuery(query1);
            System.out.println("Executing stmt");
            
            try
            {
                while(rs.next())
                {
                    // add count of rows
                    System.out.println("Setting fields");
                    
                    count++;
                    assetid = rs.getString(1);
                    System.out.println("AssetID = " + assetid);
                    info.add(assetid);
                    
                    assetname = rs.getString(2);
                    System.out.println("assetname = " + assetname);
                    info.add(assetname);
                                        
                    invoicenum = rs.getString(3);
                    System.out.println("inv num = " + invoicenum);
                    //InvoiceNumberTxt.validate();
                    info.add(invoicenum);
                       
                    assettype = rs.getString(4);
                    System.out.println("assettype = " + assettype);
                    info.add(assettype);
                    
                    make = rs.getString(5);
                    System.out.println("make = " + make);
                    info.add(make);
                    
                    serialnum = rs.getString(6);
                    System.out.println("s num = " + serialnum);
                    //SerialNumTxt.validate();
                    info.add(serialnum);
                 
                    floor = rs.getString(7);
                    System.out.println("floor = " + floor);
                    info.add(floor);
                    
                    AssetIDTxt.setText(assetid);
                    AssetNameTxt.setText(assetname);
                    InvoiceNumberTxt.setText(invoicenum);
                    AssetTypeCB.setSelectedItem(assettype);
                    MakeCB.setSelectedItem(make);
                    SerialNumTxt.setText(serialnum);
                    FloorTxt.setText(floor);    
                    
                    
                }
                    
                if(count == 0)
                    {
                        JOptionPane.showMessageDialog(null, "No Asset Found");
                    }
                

                try
                    {
                    stmt.close();
                    rs.close();
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
            catch(SQLException sqle)
        {
            JOptionPane.showMessageDialog(null, sqle.getMessage());
        }
    }
    
    
    public void SaveChanges()
    {
        try
        {
            String UserEditing = "";
            con = new DesignHome().dbconn();
            con.setAutoCommit(false);
            System.out.println("Connection Established");
            
            String assetdet = AssetIDTxt.getText();
            String assetid =  AssetIDTxt.getText();
            String assetname = AssetNameTxt.getText();
            String invoicenum = InvoiceNumberTxt.getText();
            String assettype = AssetTypeCB.getItemAt(AssetTypeCB.getSelectedIndex());
            String make = MakeCB.getItemAt(MakeCB.getSelectedIndex());
            String serialnum = SerialNumTxt.getText(); 
            String floor = FloorTxt.getText();
            String id = "";
            String assettypecode = Integer.toString(AssetTypeCB.getSelectedIndex()+1);
            String makecode = Integer.toString(MakeCB.getSelectedIndex()+7);
            
            newinfo.add(assetdet);
            newinfo.add(assetname);
            newinfo.add(invoicenum);
            newinfo.add(assettype);
            newinfo.add(make);
            newinfo.add(serialnum);
            newinfo.add(floor);
            
            if(newinfo.equals(info))
            JOptionPane.showMessageDialog(null, "Make Some Changes First.");
                    
            else
            {             
                String que = "Select userid from activitylog order by Sno desc limit 1";
                stmt = con.createStatement();
                rs = stmt.executeQuery(que);
                if(rs.next())
                    id = rs.getString(1);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                java.util.Date date = new java.util.Date();
                sdf.format(date);
                String datetime = date.toString();

                String query2 = "Update assets set assetid = ?, assetname = ?, invoicenumber = ?, assettype = ?, make = ?, serialnumber = ?, floor = ?, LastModifiedBy = ?, LastModifiedOn = ? where assetid = '" + assetdet + "'";
                pstmt = con.prepareStatement(query2);
                pstmt.setString(1, assetid);
                pstmt.setString(2, assetname);
                pstmt.setString(3, invoicenum);
                pstmt.setString(4, assettypecode);
                pstmt.setString(5, makecode);
                pstmt.setString(6, serialnum);
                pstmt.setString(7, floor);
                pstmt.setString(8, id);
                pstmt.setString(9, datetime);

                try
                {
                    pstmt.executeUpdate();
                    con.commit();
                    JOptionPane.showMessageDialog(null, "Updated Successfully");
                    this.dispose();
                }
                catch(SQLException pstmte)
                {
                    JOptionPane.showMessageDialog(null, pstmte.getMessage());
                }

                try
                    { 
                        System.out.println("Closing");
                        pstmt.close();
                        stmt.close();
//                        stmt2.close();
                        rs.close();
                        con.close();
                    }
                catch(SQLException e)
                    {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                }
            }
            catch(SQLException sqle)
            {     
            JOptionPane.showMessageDialog(null, sqle.getMessage());
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

        MainCard = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        AssetTypeCB = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        MakeCB = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        AssetIDTxt = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        AssetNameTxt = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        InvoiceNumberTxt = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        SerialNumTxt = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        FloorTxt = new javax.swing.JTextField();
        AddAssetBtn = new javax.swing.JButton();
        ClockLbl = new javax.swing.JLabel();
        BackBtn1 = new javax.swing.JButton();
        HeadingLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(603, 700));
        setResizable(false);

        MainCard.setBackground(new java.awt.Color(51, 51, 51));
        MainCard.setMinimumSize(new java.awt.Dimension(603, 700));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Invoice Number :");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Asset Type:");

        AssetTypeCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laptop", "PC", "Server", "Printer", "Router", "Hard Drive" }));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Make:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Serial Number:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Floor:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Asset Name:");

        MakeCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dell", "HP", "Lenovo", "IBM", "Acer", "Huawei", "Indosat", "Telkomsel", "Seagate", "Samsung", "Western Digital", "" }));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Asset ID:");

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        AssetIDTxt.setEditable(false);
        AssetIDTxt.setBackground(new java.awt.Color(51, 51, 51));
        AssetIDTxt.setForeground(new java.awt.Color(255, 255, 255));
        AssetIDTxt.setBorder(null);
        AssetIDTxt.setCaretColor(new java.awt.Color(255, 255, 255));

        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));

        AssetNameTxt.setBackground(new java.awt.Color(51, 51, 51));
        AssetNameTxt.setForeground(new java.awt.Color(255, 255, 255));
        AssetNameTxt.setBorder(null);
        AssetNameTxt.setCaretColor(new java.awt.Color(255, 255, 255));

        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));

        InvoiceNumberTxt.setBackground(new java.awt.Color(51, 51, 51));
        InvoiceNumberTxt.setForeground(new java.awt.Color(255, 255, 255));
        InvoiceNumberTxt.setBorder(null);
        InvoiceNumberTxt.setCaretColor(new java.awt.Color(255, 255, 255));

        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));

        SerialNumTxt.setBackground(new java.awt.Color(51, 51, 51));
        SerialNumTxt.setForeground(new java.awt.Color(255, 255, 255));
        SerialNumTxt.setBorder(null);
        SerialNumTxt.setCaretColor(new java.awt.Color(255, 255, 255));

        jSeparator5.setForeground(new java.awt.Color(255, 255, 255));

        FloorTxt.setBackground(new java.awt.Color(51, 51, 51));
        FloorTxt.setForeground(new java.awt.Color(255, 255, 255));
        FloorTxt.setBorder(null);
        FloorTxt.setCaretColor(new java.awt.Color(255, 255, 255));

        AddAssetBtn.setBackground(new java.awt.Color(0, 102, 204));
        AddAssetBtn.setForeground(new java.awt.Color(255, 255, 255));
        AddAssetBtn.setText("Update");
        AddAssetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddAssetBtnActionPerformed(evt);
            }
        });

        ClockLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ClockLbl.setForeground(new java.awt.Color(255, 255, 255));

        BackBtn1.setBackground(new java.awt.Color(0, 102, 204));
        BackBtn1.setForeground(new java.awt.Color(255, 255, 255));
        BackBtn1.setText("Cancel");
        BackBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackBtn1ActionPerformed(evt);
            }
        });

        HeadingLbl.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 24)); // NOI18N
        HeadingLbl.setForeground(new java.awt.Color(255, 255, 255));
        HeadingLbl.setText("Modify Asset");

        javax.swing.GroupLayout MainCardLayout = new javax.swing.GroupLayout(MainCard);
        MainCard.setLayout(MainCardLayout);
        MainCardLayout.setHorizontalGroup(
            MainCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainCardLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(HeadingLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ClockLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addGroup(MainCardLayout.createSequentialGroup()
                .addGroup(MainCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainCardLayout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addGroup(MainCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AssetIDTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(AssetNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(InvoiceNumberTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(MainCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(SerialNumTxt)
                                .addComponent(jSeparator4)
                                .addComponent(jSeparator3)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainCardLayout.createSequentialGroup()
                                    .addGroup(MainCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(MainCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(AssetTypeCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(MakeCB, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel6)
                                .addComponent(jLabel7)
                                .addComponent(FloorTxt)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                .addComponent(AddAssetBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(MainCardLayout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addComponent(BackBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(163, Short.MAX_VALUE))
        );
        MainCardLayout.setVerticalGroup(
            MainCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainCardLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(MainCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ClockLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HeadingLbl))
                .addGap(34, 34, 34)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AssetIDTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AssetNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(InvoiceNumberTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(MainCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(AssetTypeCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(MainCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MakeCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(28, 28, 28)
                .addComponent(jLabel6)
                .addGap(10, 10, 10)
                .addComponent(SerialNumTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FloorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(AddAssetBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(BackBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(MainCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void AddAssetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddAssetBtnActionPerformed
        // TODO add your handling code here:
        SaveChanges();
    }//GEN-LAST:event_AddAssetBtnActionPerformed

    private void BackBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackBtn1ActionPerformed
        // TODO add your handling code here:
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel the process?  You will be redirected to the previous window", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if(response == JOptionPane.YES_OPTION)
        {
            this.dispose();
        }

        else if(response == JOptionPane.NO_OPTION)
        {
            this.setVisible(true);
        }

    }//GEN-LAST:event_BackBtn1ActionPerformed

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
            java.util.logging.Logger.getLogger(ModifyAsset.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModifyAsset.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModifyAsset.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModifyAsset.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ModifyAsset().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddAssetBtn;
    private javax.swing.JTextField AssetIDTxt;
    private javax.swing.JTextField AssetNameTxt;
    private javax.swing.JComboBox<String> AssetTypeCB;
    private javax.swing.JButton BackBtn1;
    private javax.swing.JLabel ClockLbl;
    private javax.swing.JTextField FloorTxt;
    private javax.swing.JLabel HeadingLbl;
    private javax.swing.JTextField InvoiceNumberTxt;
    private javax.swing.JPanel MainCard;
    private javax.swing.JComboBox<String> MakeCB;
    private javax.swing.JTextField SerialNumTxt;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    // End of variables declaration//GEN-END:variables
}
