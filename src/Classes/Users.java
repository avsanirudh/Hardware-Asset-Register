/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Anirudh
 */
public class Users extends javax.swing.JFrame
{
    Connection con;
    Statement stmt;
    PreparedStatement pstmt;
    Statement stmt1;
    Statement stmt2;
    ResultSet rs;
    /**
     * Creates new form Users
     */
    public Users() {
        initComponents();       
        clock();
        viewtable();
        cellfiller();
    }
    
    public void cellfiller()
    {
        int rows = t1.getRowCount();
        int col = t1.getColumnCount();
        System.out.println("Rows = " + rows + " Columns = " + col);        
        for(int i = 0; i<rows; i++)
        {            
            for(int j = 0; j<col; j++)
            {
                try
                {
                    t1.getValueAt(i, j).toString();             
                }
                catch(NullPointerException e)
                {
                    t1.setValueAt("N/A", i, j);
                }
            }
        }
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
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        java.util.Date date = new java.util.Date();
                        ClockLbl.setText(sdf.format(date)); 
                        sleep(1000);
                    }
                }
                catch (InterruptedException ex) 
                {
                    System.out.println(ex.getMessage());
                }
            }
        
        };
        clock.start();
    }
    
    public String[][] ConvertToArray()
    {               
        
        int r = t1.getRowCount();
        String[][] table = new String[r][8];
        
        for(int rows = 0; rows<r; rows++)
        {
            for(int col = 0; col<8; col++)
            {
                try
                {
                    table[rows][col] = t1.getValueAt(rows,col).toString();                    
                }
                catch(NullPointerException e)
                {
                    table[rows][col] = "N/A";
                }
            }
        }
        System.out.println("Table intitial" + Arrays.deepToString(table));
        return table;
    }
    
    public void search(String[][] arr)
    {
        int rows = arr.length;
        int count = 0;
        
        String[] temp = null;
        
        String x = UserDetTxt.getText();
        System.out.println("x = " + x);   
        
        DefaultTableModel model = (DefaultTableModel)t1.getModel();
        model.setRowCount(0);       
        
        for(int j = 0; j<rows; j++)
        {
            if(arr[j][0].equals(x) || arr[j][1].contains(x.toLowerCase()) || arr[j][1].contains(x.toUpperCase()))
            {                   
                model.addRow(arr[j]);    
                count++;
            }
        }      
        if(count == 0)
            JOptionPane.showMessageDialog(null, "No Such User Found");
    }
    
    public void logout()
    {
        try
        {
        String uid = "";
            con = new DesignHome().dbconn();
            String que = "Select userid, sno from ActivityLog order by Sno desc limit 1";
            stmt = con.createStatement();
            rs = stmt.executeQuery(que);
            int sno = 0;
            while(rs.next())
            {
                uid = rs.getString(1);
                sno = rs.getInt(2);
            }

            System.out.println("UserID = " + uid);
            
            String query = "update ActivityLog set logout = ? where sno = '" + sno + "';";
            pstmt = con.prepareStatement(query);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            java.util.Date date = new java.util.Date();
            sdf.format(date);
            String datetime = date.toString();

            pstmt.setString(1, datetime);
            pstmt.executeUpdate();
            
            stmt.close();
            pstmt.close();
            rs.close();
            con.close();
            
            new Home().setVisible(true);
            this.dispose();
        }
    
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public String Usergetdet()
    {
        int row = t1.getSelectedRow();
        String userdet = t1.getValueAt(row, 2).toString();
        return userdet;
    }
    
    public int getIsAdmin()
    {
        int row = t1.getSelectedRow();
        String IsAdmin = t1.getValueAt(row,3).toString();        
        if(IsAdmin.equals("True"))
        {
            return 1;     
        }
        else
        return 0;
    }
    
    public void hidebtn()
    {
        if(t1.getSelectionModel().isSelectionEmpty())
        {
        DeleteUserBtn.setVisible(false);
        EditBtn.setVisible(false);
        }
    }
    
    public void Sorter(String[][] arr, int column)
    {        
        String[] temp = null;
        int rows;
        rows = arr.length;
        
        if(column == 0 || column == 6)
        {
            for(int i = 0; i<rows; i++)
            {           
                for(int j = 1; j<rows; j++)
                {                                          
                    if (Integer.parseInt(arr[j - 1][column]) > Integer.parseInt(arr[j][column])) 
                    {
                        temp = arr[j - 1];
                        arr[j - 1] = arr[j];
                        arr[j] = temp;
                    }
                }
            }
        }
        else
        {
            String x;
            String y;
            for(int i = 0; i<rows; i++)
            {           
                for(int j = 1; j<rows; j++)
                {                         
                    x = arr[j - 1][column].toLowerCase();
                    y = arr[j][column].toLowerCase();
                    if (x.compareTo(y)>0) 
                    {
                        temp = arr[j - 1];
                        arr[j - 1] = arr[j];
                        arr[j] = temp;
                    }
                }
            }
        }
        DefaultTableModel model = (DefaultTableModel)t1.getModel();
        model.setRowCount(0);
        
        for(int i = 0; i<arr.length; i++)
        {
            String[] row = new String[arr[i].length];
            
            for(int j = 0; j<arr[i].length; j++)
            {
                row[j] = arr[i][j];
            }
            model.addRow(row);
        }
        System.out.println("Table final" + Arrays.deepToString(arr));
    }
   
    public void deleteuser()
    {
        int row = t1.getSelectedRow();
        String userdetails = t1.getValueAt(row, 0).toString();

        try
        {
            String query2 = "Select userid from activitylog order by sno desc limit 1";
            String dbdet = "";
            con = new DesignHome().dbconn();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query2);
            if(rs.next())
                dbdet = rs.getString(1);
            
            
            stmt.close();
            rs.close();
            
            if(!dbdet.equals(userdetails))
            {
                Pattern patt1 = Pattern.compile("[^a-z0-9]",Pattern.CASE_INSENSITIVE);//for assetdet
                Matcher Massetdet = patt1.matcher(userdetails);
                Boolean b = Massetdet.find();

                if(b)
                {
                    JOptionPane.showMessageDialog(null, "User Name or ID can't contain special characters");
                }

                else
                {
                    System.out.println("Establishing Connection");
                    con = new DesignHome().dbconn();
                    con.setAutoCommit(false);
                    System.out.println("Connection Established");

                    stmt = con.createStatement();
                    String query = "update users set active = 'No' where id = '" + userdetails + "'";
                    System.out.println(query);

                    int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected user?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if(response == JOptionPane.YES_OPTION)
                    {
                        stmt.executeUpdate(query);
                        con.commit();
                        JOptionPane.showMessageDialog(null, "User deleted successfully! (Set as Deactivated)");
                    }

                    else if(response == JOptionPane.NO_OPTION)
                    {
                        JOptionPane.showMessageDialog(null, "Action Cancelled");
                    }

                    String query1 = "select * from users";
                    rs = stmt.executeQuery(query1);
                    t1.setModel(DbUtils.resultSetToTableModel(rs));

                    try
                    {
                        stmt.close();
                        con.close();
                        rs.close();
                    }
                    catch(SQLException e)
                    {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }   
                }
            }
            else
                JOptionPane.showMessageDialog(null, "The Currently Logged In User Cannot Be Deleted.");
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void viewtable()
    {
        if(GroupCB.getItemAt(GroupCB.getSelectedIndex()).equals("All"))
        {
            try
            {
                System.out.println("Establishing Connection");
                con = new DesignHome().dbconn();
                con.setAutoCommit(false);
                System.out.println("Connection Established");

                stmt = con.createStatement();
                String query = "Select * from users where activated = 'true'";
                rs = stmt.executeQuery(query);
                t1.setModel(DbUtils.resultSetToTableModel(rs));

                
                stmt1= con.createStatement();
                String query1 = "select count(username) from users where activated = 'True'";
                rs = stmt1.executeQuery(query1);
                if(rs.next())
                {
                    UserCountLbl.setText("Total number of users = " + rs.getInt(1));
                    AdminCountLbl.setText("");
                }

                 try
                {
                    rs.close();
                    stmt1.close();
                    stmt.close();
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
        
        else if(GroupCB.getItemAt(GroupCB.getSelectedIndex()).equals("Admins"))
        {
            try
            {
                System.out.println("Establishing Connection");
                con = new DesignHome().dbconn();
                con.setAutoCommit(false);
                System.out.println("Connection Established");

                
                stmt1= con.createStatement();
                String query1 = "select count(username) from users where activated = 'True'";
                rs = stmt1.executeQuery(query1);
                if(rs.next())
                {
                    UserCountLbl.setText("Total number of users = " + rs.getInt(1));
                }

                stmt2 = con.createStatement();
                String query2 = "Select count(username) from users where IsAdmin = 'true' and activated = 'True'";
                rs = stmt2.executeQuery(query2);
                if(rs.next())
                {
                    AdminCountLbl.setText("Number of " + GroupCB.getItemAt(GroupCB.getSelectedIndex()) + " = " + rs.getInt(1));
                }
                
                stmt = con.createStatement();
                String query = "Select * from users where IsAdmin = 'true' activated = 'True'";
                rs = stmt.executeQuery(query);
                t1.setModel(DbUtils.resultSetToTableModel(rs));

                 try
                {
                    rs.close();
                    stmt2.close();
                    stmt1.close();
                    stmt.close();
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
        
        else if(GroupCB.getItemAt(GroupCB.getSelectedIndex()).equals("Non-Admins"))
        {
            try
            {
                System.out.println("Establishing Connection");
                con = new DesignHome().dbconn();
                con.setAutoCommit(false);
                System.out.println("Connection Established");

                
                stmt1= con.createStatement();
                String query1 = "select count(username) from users where activated = 'True'";
                rs = stmt1.executeQuery(query1);
                if(rs.next())
                {
                    UserCountLbl.setText("Total number of users = " + rs.getInt(1));
                }

                stmt2 = con.createStatement();
                String query2 = "Select count(username) from users where IsAdmin = 'false' and activated = 'True'";
                rs = stmt2.executeQuery(query2);
                if(rs.next())
                {
                    AdminCountLbl.setText("Number of " + GroupCB.getItemAt(GroupCB.getSelectedIndex()) + " = " + rs.getInt(1));
                }
                
                stmt = con.createStatement();
                String query = "Select * from users where IsAdmin = 'false' activated = 'True'";
                rs = stmt.executeQuery(query);
                t1.setModel(DbUtils.resultSetToTableModel(rs));

                 try
                {
                    rs.close();
                    stmt2.close();
                    stmt1.close();
                    stmt.close();
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
        ExportDetBtn.setVisible(true);
    }                              
    
    public void tableset()
    {
        try
        {
            con = new DesignHome().dbconn();
            String query = "Select * from users";
                
            stmt1 = con.createStatement();
            rs = stmt1.executeQuery(query);
            t1.setModel(DbUtils.resultSetToTableModel(rs));
            
            stmt1.close();
            rs.close();
            con.close();
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
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

        BackBtn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        NameTxt1 = new javax.swing.JTextField();
        MainCard = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        SearchUsersBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        t1 = new javax.swing.JTable();
        DeleteUserBtn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        GroupCB = new javax.swing.JComboBox<>();
        UserCountLbl = new javax.swing.JLabel();
        AdminCountLbl = new javax.swing.JLabel();
        EditBtn = new javax.swing.JButton();
        BackBtn1 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        UserDetTxt = new javax.swing.JTextField();
        ClockLbl = new javax.swing.JLabel();
        ClockLbl1 = new javax.swing.JLabel();
        ExportDetBtn = new javax.swing.JButton();
        HeadingLbl = new javax.swing.JLabel();
        LogOutBtn = new javax.swing.JButton();
        BackBtn2 = new javax.swing.JButton();
        RefreshBtn = new javax.swing.JButton();
        SortCB = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();

        BackBtn.setBackground(new java.awt.Color(0, 102, 204));
        BackBtn.setForeground(new java.awt.Color(255, 255, 255));
        BackBtn.setText("Main Menu");
        BackBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackBtnActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        NameTxt1.setBackground(new java.awt.Color(51, 51, 51));
        NameTxt1.setForeground(new java.awt.Color(255, 255, 255));
        NameTxt1.setBorder(null);
        NameTxt1.setCaretColor(new java.awt.Color(255, 255, 255));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(940, 700));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        MainCard.setBackground(new java.awt.Color(51, 51, 51));
        MainCard.setForeground(new java.awt.Color(51, 51, 51));
        MainCard.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Enter UserName or UserID To Search");
        MainCard.add(jLabel2);
        jLabel2.setBounds(10, 577, 260, 20);

        SearchUsersBtn.setForeground(new java.awt.Color(255, 255, 255));
        SearchUsersBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_32px.png"))); // NOI18N
        SearchUsersBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchUsersBtnActionPerformed(evt);
            }
        });
        MainCard.add(SearchUsersBtn);
        SearchUsersBtn.setBounds(250, 600, 40, 40);

        t1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UserID", "Password", "Name", "UserName", "IsAdmin", "Email ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t1.setColumnSelectionAllowed(true);
        t1.getTableHeader().setReorderingAllowed(false);
        t1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t1);
        t1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        MainCard.add(jScrollPane1);
        jScrollPane1.setBounds(20, 50, 900, 470);

        DeleteUserBtn.setBackground(new java.awt.Color(0, 102, 204));
        DeleteUserBtn.setForeground(new java.awt.Color(255, 255, 255));
        DeleteUserBtn.setText("Delete");
        DeleteUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteUserBtnActionPerformed(evt);
            }
        });
        DeleteUserBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DeleteUserBtnKeyPressed(evt);
            }
        });
        MainCard.add(DeleteUserBtn);
        DeleteUserBtn.setBounds(840, 590, 80, 40);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("User Type");
        MainCard.add(jLabel6);
        jLabel6.setBounds(530, 10, 70, 17);

        GroupCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Admins", "Non-Admins", "Activated", "Deactivated" }));
        GroupCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupCBActionPerformed(evt);
            }
        });
        MainCard.add(GroupCB);
        GroupCB.setBounds(610, 10, 105, 22);

        UserCountLbl.setForeground(new java.awt.Color(255, 255, 255));
        MainCard.add(UserCountLbl);
        UserCountLbl.setBounds(360, 620, 270, 20);

        AdminCountLbl.setForeground(new java.awt.Color(255, 255, 255));
        MainCard.add(AdminCountLbl);
        AdminCountLbl.setBounds(350, 660, 280, 20);

        EditBtn.setBackground(new java.awt.Color(0, 102, 204));
        EditBtn.setForeground(new java.awt.Color(255, 255, 255));
        EditBtn.setText("Edit");
        EditBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditBtnActionPerformed(evt);
            }
        });
        MainCard.add(EditBtn);
        EditBtn.setBounds(730, 590, 100, 40);

        BackBtn1.setBackground(new java.awt.Color(0, 102, 204));
        BackBtn1.setForeground(new java.awt.Color(255, 255, 255));
        BackBtn1.setText("Main Menu");
        BackBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackBtn1ActionPerformed(evt);
            }
        });
        MainCard.add(BackBtn1);
        BackBtn1.setBounds(730, 640, 100, 40);

        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));
        MainCard.add(jSeparator2);
        jSeparator2.setBounds(10, 640, 230, 3);

        UserDetTxt.setBackground(new java.awt.Color(51, 51, 51));
        UserDetTxt.setForeground(new java.awt.Color(255, 255, 255));
        UserDetTxt.setBorder(null);
        UserDetTxt.setCaretColor(new java.awt.Color(255, 255, 255));
        MainCard.add(UserDetTxt);
        UserDetTxt.setBounds(10, 610, 230, 27);

        ClockLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ClockLbl.setForeground(new java.awt.Color(255, 255, 255));
        MainCard.add(ClockLbl);
        ClockLbl.setBounds(0, 0, 0, 0);

        ClockLbl1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ClockLbl1.setForeground(new java.awt.Color(255, 255, 255));
        MainCard.add(ClockLbl1);
        ClockLbl1.setBounds(330, 10, 160, 20);

        ExportDetBtn.setBackground(new java.awt.Color(0, 102, 204));
        ExportDetBtn.setForeground(new java.awt.Color(255, 255, 255));
        ExportDetBtn.setText("Export Details");
        ExportDetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportDetBtnActionPerformed(evt);
            }
        });
        MainCard.add(ExportDetBtn);
        ExportDetBtn.setBounds(350, 550, 130, 40);

        HeadingLbl.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 24)); // NOI18N
        HeadingLbl.setForeground(new java.awt.Color(255, 255, 255));
        HeadingLbl.setText("View And Search Users");
        MainCard.add(HeadingLbl);
        HeadingLbl.setBounds(20, 10, 280, 32);

        LogOutBtn.setBackground(new java.awt.Color(0, 102, 204));
        LogOutBtn.setForeground(new java.awt.Color(255, 255, 255));
        LogOutBtn.setText("Log Out");
        LogOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogOutBtnActionPerformed(evt);
            }
        });
        MainCard.add(LogOutBtn);
        LogOutBtn.setBounds(840, 640, 80, 40);

        BackBtn2.setBackground(new java.awt.Color(0, 102, 204));
        BackBtn2.setForeground(new java.awt.Color(255, 255, 255));
        BackBtn2.setText("Admin Main Menu");
        BackBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackBtn2ActionPerformed(evt);
            }
        });
        MainCard.add(BackBtn2);
        BackBtn2.setBounds(750, 540, 150, 40);

        RefreshBtn.setBackground(new java.awt.Color(0, 102, 204));
        RefreshBtn.setForeground(new java.awt.Color(255, 255, 255));
        RefreshBtn.setText("Refresh");
        RefreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshBtnActionPerformed(evt);
            }
        });
        MainCard.add(RefreshBtn);
        RefreshBtn.setBounds(500, 550, 130, 40);

        SortCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "UserID", "Name", "Username", "Employee ID", "Email ID" }));
        SortCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SortCBActionPerformed(evt);
            }
        });
        MainCard.add(SortCB);
        SortCB.setBounds(800, 10, 105, 22);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Sort By");
        MainCard.add(jLabel7);
        jLabel7.setBounds(740, 10, 70, 17);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(MainCard, javax.swing.GroupLayout.PREFERRED_SIZE, 940, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainCard, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void SearchUsersBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchUsersBtnActionPerformed
        // TODO add your handling code here:
        String[][] arr; 
        int r = t1.getRowCount();
        if(UserDetTxt.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null, "Enter User ID or User Name");
        }        
        else
        {
            tableset();
            arr = ConvertToArray();             
            search(arr);
        }

    }//GEN-LAST:event_SearchUsersBtnActionPerformed

    private void DeleteUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteUserBtnActionPerformed
        // TODO add your handling code here:
        if(t1.getSelectionModel().isSelectionEmpty())
        {
            JOptionPane.showMessageDialog(null, "Select a user");            
        }
        else
        deleteuser();
    }//GEN-LAST:event_DeleteUserBtnActionPerformed

    public void exportsummary(String path)
    {
        try
        {
            
            File file = new File(path);
            if(!file.exists())
            {
                file.createNewFile();
            }
            
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(String.format("%-10s | %-35s | %-15s | %-15s | %-8s | %-40s |", "User ID" , "Password" ,"Name" ,"Username" ,"Admin" ,"Email ID"));
            bw.write("\n___________________________________________________________________________________________________________________________________________\n");    
            for(int i = 0; i<t1.getRowCount(); i++)
            {
                for(int j = 0; j < t1.getColumnCount(); j++)
                {
                    if(j == 0)
                        bw.write(String.format("%-10s | ", t1.getValueAt(i,j).toString()));
                    else if(j == 1)
                        bw.write(String.format("%-35s | ", t1.getValueAt(i,j).toString()));
                    else if(j == 2)
                        bw.write(String.format("%-15s | ", t1.getValueAt(i,j).toString()));
                    else if(j == 3)
                        bw.write(String.format("%-15s | ", t1.getValueAt(i,j).toString()));
                    else if(j == 4)
                        bw.write(String.format("%-8s | ", t1.getValueAt(i,j).toString()));
                    else if(j == 5)
                        bw.write(String.format("%-40s | ", t1.getValueAt(i,j).toString()));                        
                }
                bw.write("\n___________________________________________________________________________________________________________________________________________\n");    
            }
            bw.close();
            fw.close();
                
            JOptionPane.showMessageDialog(null, "Data Exported Successfully. File location: " + path);
        }       
        catch(HeadlessException | IOException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    public void export(int rows, String path) throws FileNotFoundException, DocumentException
    {
        try
        {
            Document document = new Document(PageSize.A4.rotate(),0,0,0,0);

            PdfWriter.getInstance(document, new FileOutputStream(path + "\\SummaryOfUsers.pdf"));
            document.open();

            PdfPTable table = new PdfPTable(7);

            PdfPCell c1 = new PdfPCell(new Phrase("ID"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Password"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Username"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Admin"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Email"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Activated"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Employee ID"));
            table.addCell(c1);

            table.setHeaderRows(1);

            for(int i = 0; i < rows; i++)
            {
                for(int j = 0; j<7; j++)
                {
                    table.addCell(t1.getValueAt(i, j).toString());
                }
            }
            document.add(table);

            document.close();

            JOptionPane.showMessageDialog(null, "Data Exported Successfully. File location: " + path + "\\SummaryOfUsers.pdf");
        }
        catch(FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, "Please Select A FIle");
        }
    }
    public String getActive()
    {
        String active = t1.getValueAt(t1.getSelectedRow(), 8).toString();
        return active;
    }
    private void EditBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditBtnActionPerformed
        // TODO add your handling code here:
        if(t1.getSelectionModel().isSelectionEmpty())
        {
            JOptionPane.showMessageDialog(null, "Select a row to edit");
        }
        else
        {
            String active = getActive();
            String userdet = Usergetdet();
            String approved = getapproved();
            int IsAdmin = getIsAdmin();
            EditUser eu = new EditUser();
            eu.setVisible(true);
            eu.getdet(userdet, IsAdmin, approved, active);
            cellfiller();
        }
    }//GEN-LAST:event_EditBtnActionPerformed
    public String getapproved()
    {
        int row = t1.getSelectedRow();
        String approved = t1.getValueAt(row, 5).toString();
        return approved;
    }
    private void t1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t1MouseClicked
        // TODO add your handling code here:
        DeleteUserBtn.setVisible(true);
        EditBtn.setVisible(true);
    }//GEN-LAST:event_t1MouseClicked

    private void BackBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackBtnActionPerformed
        // TODO add your handling code here:
        new MainMenu().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BackBtnActionPerformed

    private void BackBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackBtn1ActionPerformed
        // TODO add your handling code here:
        new MainMenu().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BackBtn1ActionPerformed

    private void DeleteUserBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DeleteUserBtnKeyPressed
        // TODO add your handling code here:
        deleteuser();
    }//GEN-LAST:event_DeleteUserBtnKeyPressed

    private void ExportDetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportDetBtnActionPerformed
        File dir = null;
        String path = "";
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("choosertitle");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
        {
            dir = chooser.getSelectedFile();
            path = dir.getAbsolutePath();   
            int r = t1.getRowCount();

            try 
            {
                export(r, path);
            }
            catch (FileNotFoundException ex) 
            {
                Logger.getLogger(SearchAssets.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (DocumentException ex) 
            {
                Logger.getLogger(SearchAssets.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else 
        {
          JOptionPane.showMessageDialog(null, "No Selection");
        }
    }//GEN-LAST:event_ExportDetBtnActionPerformed

    private void GroupCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupCBActionPerformed
        // TODO add your handling code here:
        if(GroupCB.getItemAt(GroupCB.getSelectedIndex()).equals("All"))
        {
            try
            {
                System.out.println("Establishing Connection");
                con = new DesignHome().dbconn();
                con.setAutoCommit(false);
                System.out.println("Connection Established");

                stmt = con.createStatement();
                String query = "Select * from users";
                rs = stmt.executeQuery(query);
                t1.setModel(DbUtils.resultSetToTableModel(rs));

                
                stmt1= con.createStatement();
                String query1 = "select count(username) from users";
                rs = stmt1.executeQuery(query1);
                if(rs.next())
                {
                    UserCountLbl.setText("Total number of users = " + rs.getInt(1));
                    AdminCountLbl.setText("");
                }

                 try
                {
                    rs.close();
                    stmt1.close();
                    stmt.close();
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
        
        else if(GroupCB.getItemAt(GroupCB.getSelectedIndex()).equals("Admins"))
        {
            try
            {
                System.out.println("Establishing Connection");
                con = new DesignHome().dbconn();
                con.setAutoCommit(false);
                System.out.println("Connection Established");

                
                stmt1= con.createStatement();
                String query1 = "select count(username) from users";
                rs = stmt1.executeQuery(query1);
                if(rs.next())
                {
                    UserCountLbl.setText("Total number of users = " + rs.getInt(1));
                }

                stmt2 = con.createStatement();
                String query2 = "Select count(username) from users where IsAdmin = 'true'";
                rs = stmt2.executeQuery(query2);
                if(rs.next())
                {
                    AdminCountLbl.setText("Number of " + GroupCB.getItemAt(GroupCB.getSelectedIndex()) + " = " + rs.getInt(1));
                }
                
                stmt = con.createStatement();
                String query = "Select * from users where IsAdmin = 'true'";
                rs = stmt.executeQuery(query);
                t1.setModel(DbUtils.resultSetToTableModel(rs));

                 try
                {
                    rs.close();
                    stmt2.close();
                    stmt1.close();
                    stmt.close();
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
        
        else if(GroupCB.getItemAt(GroupCB.getSelectedIndex()).equals("Non-Admins"))
        {
            try
            {
                System.out.println("Establishing Connection");
                con = new DesignHome().dbconn();
                con.setAutoCommit(false);
                System.out.println("Connection Established");

                
                stmt1= con.createStatement();
                String query1 = "select count(username) from users";
                rs = stmt1.executeQuery(query1);
                if(rs.next())
                {
                    UserCountLbl.setText("Total number of users = " + rs.getInt(1));
                }

                stmt2 = con.createStatement();
                String query2 = "Select count(username) from users where IsAdmin = 'false'";
                rs = stmt2.executeQuery(query2);
                if(rs.next())
                {
                    AdminCountLbl.setText("Number of " + GroupCB.getItemAt(GroupCB.getSelectedIndex()) + " = " + rs.getInt(1));
                }
                
                stmt = con.createStatement();
                String query = "Select * from users where IsAdmin = 'false'";
                rs = stmt.executeQuery(query);
                t1.setModel(DbUtils.resultSetToTableModel(rs));

                 try
                {
                    rs.close();
                    stmt2.close();
                    stmt1.close();
                    stmt.close();
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
        else if(GroupCB.getItemAt(GroupCB.getSelectedIndex()).equals("Activated"))
        {
            try
            {
                System.out.println("Establishing Connection");
                con = new DesignHome().dbconn();
                con.setAutoCommit(false);
                System.out.println("Connection Established");

                stmt = con.createStatement();
                String query = "Select * from users users where Active = 'Yes'";
                rs = stmt.executeQuery(query);
                t1.setModel(DbUtils.resultSetToTableModel(rs));

                
                stmt1= con.createStatement();
                String query1 = "select count(username) from users where Active = 'Yes'";
                rs = stmt1.executeQuery(query1);
                if(rs.next())
                {
                    UserCountLbl.setText("Total number of active users = " + rs.getInt(1));
                    AdminCountLbl.setText("");
                }

                 try
                {
                    rs.close();
                    stmt1.close();
                    stmt.close();
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
        else if(GroupCB.getItemAt(GroupCB.getSelectedIndex()).equals("Deactivated"))
        {
            try
            {
                System.out.println("Establishing Connection");
                con = new DesignHome().dbconn();
                con.setAutoCommit(false);
                System.out.println("Connection Established");

                stmt = con.createStatement();
                String query = "Select * from users users where Active = 'No'";
                rs = stmt.executeQuery(query);
                t1.setModel(DbUtils.resultSetToTableModel(rs));

                
                stmt1= con.createStatement();
                String query1 = "select count(username) from users where Active = 'No'";
                rs = stmt1.executeQuery(query1);
                if(rs.next())
                {
                    UserCountLbl.setText("Total number of deactivated users = " + rs.getInt(1));
                    AdminCountLbl.setText("");
                }

                 try
                {
                    rs.close();
                    stmt1.close();
                    stmt.close();
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
        ExportDetBtn.setVisible(true);
    }//GEN-LAST:event_GroupCBActionPerformed

    private void LogOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogOutBtnActionPerformed
        // TODO add your handling code here:
        logout();
    }//GEN-LAST:event_LogOutBtnActionPerformed

    private void BackBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackBtn2ActionPerformed
        // TODO add your handling code here:
        new AdminMainMenu().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BackBtn2ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        logout();
    }//GEN-LAST:event_formWindowClosing

    private void RefreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshBtnActionPerformed
        // TODO add your handling code here:
        viewtable();
        cellfiller();
    }//GEN-LAST:event_RefreshBtnActionPerformed

    private void SortCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SortCBActionPerformed
        // TODO add your handling code here:
        int index = SortCB.getSelectedIndex();
        String[][] arr = ConvertToArray();
        if(index == 1)
            Sorter(arr,0);
        if(index == 2)
            Sorter(arr,7);
        if(index ==3)
            Sorter(arr, 2);
        if(index == 4)
            Sorter(arr, 6);
        if(index == 5)
            Sorter(arr, 4);
    }//GEN-LAST:event_SortCBActionPerformed

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
            java.util.logging.Logger.getLogger(Users.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Users.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Users.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Users.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Users().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AdminCountLbl;
    private javax.swing.JButton BackBtn;
    private javax.swing.JButton BackBtn1;
    private javax.swing.JButton BackBtn2;
    private javax.swing.JLabel ClockLbl;
    private javax.swing.JLabel ClockLbl1;
    private javax.swing.JButton DeleteUserBtn;
    private javax.swing.JButton EditBtn;
    private javax.swing.JButton ExportDetBtn;
    private javax.swing.JComboBox<String> GroupCB;
    private javax.swing.JLabel HeadingLbl;
    private javax.swing.JButton LogOutBtn;
    private javax.swing.JPanel MainCard;
    private javax.swing.JTextField NameTxt1;
    private javax.swing.JButton RefreshBtn;
    private javax.swing.JButton SearchUsersBtn;
    private javax.swing.JComboBox<String> SortCB;
    private javax.swing.JLabel UserCountLbl;
    private javax.swing.JTextField UserDetTxt;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable t1;
    // End of variables declaration//GEN-END:variables
}
