/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;
import com.itextpdf.text.Document;
import java.io.File;
import static java.lang.Thread.sleep;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Anirudh
 */
public class SearchAssets extends javax.swing.JFrame {
Connection con;
Statement stmt;
Statement stmt1;
PreparedStatement pstmt;
Statement stmt2;
ResultSet RS;
ResultSet rs;

    /**
     * Creates new form SearchHardware
     */
    public SearchAssets() {
        initComponents();
        
        
        
        clock();
        ViewTable();
        cellfiller();       
        //setBounds(0,0,Screensize.height/2, Screensize.width/2);
    }

//Dimension Screensize = Toolkit.getDefaultToolkit().getScreenSize();
    static String[][] TABLE;
    
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
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        
        };
        clock.start();
    }
        
    public void ViewTable()
    {   
        try
        {
            String SortParam = OrderCB.getItemAt(OrderCB.getSelectedIndex());
            String AssetTypeParam = GroupCB.getItemAt(GroupCB.getSelectedIndex());

            System.out.println("Establishing Connection");
            con = new DesignHome().dbconn();
            con.setAutoCommit(false);
            System.out.println("Connection Established");

            String query;
            
            int count = 0;
            
            if(SortParam.equals("None") && AssetTypeParam.equals("All"))
            {
                query = "SELECT a.AssetID, a.AssetName, a.InvoiceNumber, m.description AssetType, n.description Make, a.SerialNumber, a.Floor, "
                        + "(SELECT username FROM users WHERE id = a.AddedBy) AddedBy, a.AddedOn, (SELECT username FROM users WHERE id = a.LastModifiedBy)"
                        + "LastModifiedBy, a.LastModifiedOn FROM assets a, masterdata m, masterdata n WHERE a.assettype=m.code AND m.category='A' "
                        + "AND a.make = n.code AND n.category = 'M';";
                
                stmt1 = con.createStatement();
                String query1 = "select count(assetname) from assets";
                RS = stmt1.executeQuery(query1);

                if(RS.next())
                {
                    count = RS.getInt(1);
                }
                CountLbl.setText("Total number of assets = " + count);
                TypeCountLbl.setText("");

                stmt = con.createStatement();

                RS = stmt.executeQuery(query);
                t1.setModel(DbUtils.resultSetToTableModel(RS));
            }

            else
            {
                query = "\"SELECT a.AssetID, a.AssetName, a.InvoiceNumber, m.description AssetType, n.description Make, a.SerialNumber, a.Floor, \"\n" +
"                        + \"(SELECT username FROM users WHERE id = a.AddedBy) AddedBy, a.AddedOn, (SELECT username FROM users WHERE id = a.LastModifiedBy)\"\n" +
"                        + \"LastModifiedBy, a.LastModifiedOn FROM assets a, masterdata m, masterdata n WHERE a.assettype=m.code AND m.category='A' \"\n" +
"                        + \"AND a.make = n.code AND n.category = 'M' and assettype = '" + AssetTypeParam + "' order by " + SortParam;
                stmt1 = con.createStatement();
                    String query1 = "select count(assetname) from assets";
                    RS = stmt1.executeQuery(query1);

                    if(RS.next())
                    {
                        count = RS.getInt(1);
                    }
                    CountLbl.setText("Total number of assets = " + count);
                    TypeCountLbl.setText("");
                    
                    stmt = con.createStatement();
                    RS = stmt.executeQuery(query);
                    t1.setModel(DbUtils.resultSetToTableModel(RS));
            }
            
            stmt.close();
            stmt1.close();
            RS.close();
            con.close();
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Error : " + e.getMessage());
        }
    }
    
    public void tableset()
    {
        try
        {
            con = new DesignHome().dbconn();
            String query = "SELECT a.AssetID, a.AssetName, a.InvoiceNumber, m.description AssetType, n.description Make, a.SerialNumber, a.Floor, "
                        + "(SELECT username FROM users WHERE id = a.AddedBy) AddedBy, a.AddedOn, (SELECT username FROM users WHERE id = a.LastModifiedBy)"
                        + "LastModifiedBy, a.LastModifiedOn FROM assets a, masterdata m, masterdata n WHERE a.assettype=m.code AND m.category='A' "
                        + "AND a.make = n.code AND n.category = 'M';";
                
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
    

    
    public String getdet()
    {   
        int row = t1.getSelectedRow();
        //int col = t1.getSelectedColumn();
        String assetdetails = t1.getValueAt(row, 0).toString();
        return assetdetails;
    }
    
    public void cellfiller()
    {
        int rows = t1.getRowCount();
        int col = t1.getColumnCount();
        System.out.println("Rows = " + rows + " Columns = " + col);
        String val = "";
        for(int i = 0; i<rows; i++)
        {            
            for(int j = 0; j<col; j++)
            {
                try
                {
                    val = t1.getValueAt(i, j).toString();             
                }
                catch(NullPointerException e)
                {
                    t1.setValueAt("N/A", i, j);
                }
            }
        }
        ConvertToArray();
    }

    public void deleteasset()
    {   
        int row = t1.getSelectedRow();        
        String assetname = t1.getValueAt(row, 1).toString();
        String assetdetails = t1.getValueAt(row, 0).toString();
        System.out.println("Selected = " + assetdetails);
        try
            {
                Pattern patt1 = Pattern.compile("[^a-z0-9]",Pattern.CASE_INSENSITIVE);//for assetdet
                Matcher Massetdet = patt1.matcher(assetdetails);
                Boolean b = Massetdet.find();
                
                
                if(b)
                {
                    JOptionPane.showMessageDialog(null, "Asset Name or ID can't contain special characters");
                }
                
                else
                {
                System.out.println("Establishing Connection");
                con = new DesignHome().dbconn();
                con.setAutoCommit(false);
                System.out.println("Connection Established");
                
                
                    stmt = con.createStatement();
                    String query = "Delete from assets where assetname = '" + assetdetails + "' or assetid = '" + assetdetails + "'";

                    int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected asset with name '" + assetname + "' ?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if(response == JOptionPane.YES_OPTION)
                    {
                        if(stmt.executeUpdate(query)>0)
                        {
                            con.commit();
                            JOptionPane.showMessageDialog(null, "Asset deleted successfully!");                            
                        }
                        else
                            JOptionPane.showMessageDialog(null, "Asset with entered name or ID does not exist");
                    }

                    else if(response == JOptionPane.NO_OPTION)
                    {
                        JOptionPane.showMessageDialog(null, "Action Cancelled");
                    }
                
                String query1 = "Select a.AssetID, a.AssetName, a.InvoiceNumber, m.description AssetType, n.description Make, a.SerialNumber, a.Floor, (select username from users where id = a.AddedBy) AddedBy, a.AddedOn, a.LastModifiedBy, a.LastModifiedOn from assets a, masterdata m, masterdata n where a.assettype=m.code and m.category='A' and a.make = n.code and n.category = 'M' order by assetid asc ";
                rs = stmt.executeQuery(query1);
                t1.setModel(DbUtils.resultSetToTableModel(rs));
                cellfiller();
                 try
                {
                    stmt.close();
                    con.close();
                }
                catch(SQLException e)
                {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
            }
         catch(SQLException e)
            {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }        
    }
    
    public void hidebtn()
    {      
        RefreshBtn.setVisible(false);
        EditAssetBtn.setVisible(false);
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
    
    public void ComboBoxHandler()
    {
        String AssetTypeCode = Integer.toString(GroupCB.getSelectedIndex());
        String AssetTypeParam = GroupCB.getItemAt(GroupCB.getSelectedIndex()).replaceAll("\\s", "");
        //String OrderParam = OrderCB.getItemAt(OrderCB.getSelectedIndex()).replaceAll("\\s", "");
        
        System.out.println("Assettypecode = " + AssetTypeCode + " Assettypeparam = " + AssetTypeParam /*+ " Order Param = " + OrderParam*/);
        
        int count = 0;
        int rscount = 0;
        try
        {
            con = new DesignHome().dbconn();
             
            if(AssetTypeParam.equals("All"))
            {
                stmt1 = con.createStatement();
                String query1 = "select count(assetname) from assets";
                RS = stmt1.executeQuery(query1);
                if(RS.next())
                {
                    count = RS.getInt(1);
                }
                CountLbl.setText("Total number of assets = " + count);
                TypeCountLbl.setText("");

                String query = "select a.AssetID, a.AssetName, a.InvoiceNumber, m.description AssetType, n.description Make, a.SerialNumber, a.Floor, (select username from users where id = a.AddedBy) AddedBy, a.AddedOn, (select username from users where id = a.LastModifiedBy) LastModifiedBy, a.LastModifiedOn from assets a, masterdata m, masterdata n where a.assettype=m.code and m.category='A' and a.make = n.code and n.category = 'M';";
                stmt = con.createStatement();

                RS = stmt.executeQuery(query);
                t1.setModel(DbUtils.resultSetToTableModel(RS));

                cellfiller();
                try
                {
                    RS.close();
                    stmt1.close();
                    stmt.close();
                    con.close();
                }

                catch(SQLException e)
                {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
            else
            {
                stmt1 = con.createStatement();
                String query1 = "select count(assetname) from assets";
                RS = stmt1.executeQuery(query1);
                if(RS.next())
                {
                    count = RS.getInt(1);
                }
                CountLbl.setText("Total number of assets = " + count);
                TypeCountLbl.setText("");
                stmt2 = con.createStatement();
                String query2 = "Select count(assetname) from assets where assettype = '" + AssetTypeCode +"'";
                RS = stmt2.executeQuery(query2);
                if(RS.next())
                {
                    rscount = RS.getInt(1);
                }
                TypeCountLbl.setText("Number of " + AssetTypeParam + "s = " + rscount);

                String query = "select a.AssetID, a.AssetName, a.InvoiceNumber, m.description AssetType, n.description Make, a.SerialNumber, a.Floor, (select username from users where id = a.AddedBy) AddedBy, a.AddedOn,(select username from users where id = a.LastModifiedBy) LastModifiedBy, a.LastModifiedOn from assets a, masterdata m, masterdata n where a.assettype=m.code and m.category='A' and a.make = n.code and n.category = 'M' and a.assettype = '" + AssetTypeCode + "'";
                stmt = con.createStatement();

                RS = stmt.executeQuery(query);
                t1.setModel(DbUtils.resultSetToTableModel(RS));

                cellfiller();

                try
                {
                    RS.close();
                    stmt1.close();
                    stmt.close();
                    con.close();
                }

                catch(SQLException e)
                {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
            
        }
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public String[][] ConvertToArray()
    {                       
        int r = t1.getRowCount();
        String[][] table = new String[r][11];
        
        for(int rows = 0; rows<r; rows++)
        {
            for(int col = 0; col<11; col++)
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
    
    public void export(int rows, String path) throws FileNotFoundException, DocumentException
    {
        Document document = new Document(PageSize.A4.rotate(),0,0,0,0);
        
        PdfWriter.getInstance(document, new FileOutputStream(path + "\\SummaryOfAssets.pdf"));
        document.open();
        
        PdfPTable table = new PdfPTable(11);
        
        PdfPCell c1 = new PdfPCell(new Phrase("Asset ID"));
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Asset Name"));
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Invoice Number"));
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Asset Type"));
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Make"));
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Serial Number"));
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Floor"));
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Added By"));
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Added On"));
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Last Modified By"));
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Last Modified On"));
        table.addCell(c1);
        
        table.setHeaderRows(1);
        
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j<11; j++)
            {
                table.addCell(t1.getValueAt(i, j).toString());
            }
        }
        document.add(table);
        
        document.close();
        
        JOptionPane.showMessageDialog(null, "Data Exported Successfully. File location: " + path + "\\SummaryOfAssets.pdf");
    }
    
    public void search(String[][] arr)
    {
        int rows = arr.length;
        int count = 0;                
        
        String x = AssetDetTxt.getText();
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
            JOptionPane.showMessageDialog(null, "No Such Asset Found");
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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainCard = new javax.swing.JPanel();
        SearchLbl = new javax.swing.JLabel();
        SearchBtn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        t1 = new javax.swing.JTable();
        ExportDetBtn = new javax.swing.JButton();
        BackBtn = new javax.swing.JButton();
        AssetDetTxt = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        CountLbl = new javax.swing.JLabel();
        TypeCountLbl = new javax.swing.JLabel();
        ClockLbl = new javax.swing.JLabel();
        ClockLbl1 = new javax.swing.JLabel();
        AssetTypeLbl = new javax.swing.JLabel();
        GroupCB = new javax.swing.JComboBox<>();
        GroupLbl = new javax.swing.JLabel();
        OrderCB = new javax.swing.JComboBox<>();
        SortLbl = new javax.swing.JLabel();
        EditAssetBtn = new javax.swing.JButton();
        RefreshBtn = new javax.swing.JButton();
        HeadingLbl = new javax.swing.JLabel();
        LogOutBtn = new javax.swing.JButton();
        DeleteAssetBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(966, 700));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        MainCard.setBackground(new java.awt.Color(51, 51, 51));
        MainCard.setMaximumSize(new java.awt.Dimension(800, 600));
        MainCard.setLayout(null);

        SearchLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        SearchLbl.setForeground(new java.awt.Color(255, 255, 255));
        SearchLbl.setText("Enter Asset Name or Asset ID To Search For:");
        MainCard.add(SearchLbl);
        SearchLbl.setBounds(30, 570, 311, 17);

        SearchBtn.setForeground(new java.awt.Color(255, 255, 255));
        SearchBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/search_32px.png"))); // NOI18N
        SearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchBtnActionPerformed(evt);
            }
        });
        MainCard.add(SearchBtn);
        SearchBtn.setBounds(320, 590, 40, 40);

        t1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "AssetID", "AssetName", "InvoiceNumber", "AssetType", "Make", "SerialNumber", "Floor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t1.setCellSelectionEnabled(true);
        t1.setMinimumSize(new java.awt.Dimension(75, 0));
        t1.getTableHeader().setReorderingAllowed(false);
        t1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                none(evt);
            }
        });
        t1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(t1);
        t1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (t1.getColumnModel().getColumnCount() > 0) {
            t1.getColumnModel().getColumn(0).setResizable(false);
            t1.getColumnModel().getColumn(1).setResizable(false);
            t1.getColumnModel().getColumn(2).setResizable(false);
            t1.getColumnModel().getColumn(3).setResizable(false);
            t1.getColumnModel().getColumn(4).setResizable(false);
            t1.getColumnModel().getColumn(5).setResizable(false);
            t1.getColumnModel().getColumn(6).setResizable(false);
        }

        MainCard.add(jScrollPane3);
        jScrollPane3.setBounds(10, 50, 950, 500);

        ExportDetBtn.setBackground(new java.awt.Color(0, 102, 204));
        ExportDetBtn.setForeground(new java.awt.Color(255, 255, 255));
        ExportDetBtn.setText("Export Details");
        ExportDetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportDetBtnActionPerformed(evt);
            }
        });
        MainCard.add(ExportDetBtn);
        ExportDetBtn.setBounds(440, 560, 130, 40);

        BackBtn.setBackground(new java.awt.Color(0, 102, 204));
        BackBtn.setForeground(new java.awt.Color(255, 255, 255));
        BackBtn.setText("Main Menu");
        BackBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackBtnActionPerformed(evt);
            }
        });
        MainCard.add(BackBtn);
        BackBtn.setBounds(840, 610, 101, 40);

        AssetDetTxt.setBackground(new java.awt.Color(51, 51, 51));
        AssetDetTxt.setForeground(new java.awt.Color(255, 255, 255));
        AssetDetTxt.setBorder(null);
        AssetDetTxt.setCaretColor(new java.awt.Color(255, 255, 255));
        MainCard.add(AssetDetTxt);
        AssetDetTxt.setBounds(30, 600, 280, 30);

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        MainCard.add(jSeparator1);
        jSeparator1.setBounds(30, 630, 280, 3);

        CountLbl.setForeground(new java.awt.Color(255, 255, 255));
        MainCard.add(CountLbl);
        CountLbl.setBounds(440, 630, 250, 20);

        TypeCountLbl.setForeground(new java.awt.Color(255, 255, 255));
        MainCard.add(TypeCountLbl);
        TypeCountLbl.setBounds(440, 660, 250, 20);

        ClockLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ClockLbl.setForeground(new java.awt.Color(255, 255, 255));
        MainCard.add(ClockLbl);
        ClockLbl.setBounds(0, 0, 0, 0);

        ClockLbl1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ClockLbl1.setForeground(new java.awt.Color(255, 255, 255));
        MainCard.add(ClockLbl1);
        ClockLbl1.setBounds(0, 0, 0, 0);

        AssetTypeLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        AssetTypeLbl.setForeground(new java.awt.Color(255, 255, 255));
        MainCard.add(AssetTypeLbl);
        AssetTypeLbl.setBounds(620, 10, 80, 20);

        GroupCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Laptop", "PC", "Server", "Printer", "Router", "Hard Drive" }));
        GroupCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupCBActionPerformed(evt);
            }
        });
        MainCard.add(GroupCB);
        GroupCB.setBounds(710, 10, 92, 22);

        GroupLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        GroupLbl.setForeground(new java.awt.Color(255, 255, 255));
        GroupLbl.setText("Asset Type");
        MainCard.add(GroupLbl);
        GroupLbl.setBounds(620, 10, 90, 17);

        OrderCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "Asset ID", "Asset Name", "Make", "Asset Type", "Floor" }));
        OrderCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrderCBActionPerformed(evt);
            }
        });
        MainCard.add(OrderCB);
        OrderCB.setBounds(480, 10, 99, 22);

        SortLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        SortLbl.setForeground(new java.awt.Color(255, 255, 255));
        SortLbl.setText("Sort by");
        MainCard.add(SortLbl);
        SortLbl.setBounds(420, 10, 51, 17);

        EditAssetBtn.setBackground(new java.awt.Color(0, 102, 204));
        EditAssetBtn.setForeground(new java.awt.Color(255, 255, 255));
        EditAssetBtn.setText("Edit");
        EditAssetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditAssetBtnActionPerformed(evt);
            }
        });
        MainCard.add(EditAssetBtn);
        EditAssetBtn.setBounds(720, 560, 101, 40);

        RefreshBtn.setBackground(new java.awt.Color(0, 102, 204));
        RefreshBtn.setForeground(new java.awt.Color(255, 255, 255));
        RefreshBtn.setText("Refresh");
        RefreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshBtnActionPerformed(evt);
            }
        });
        MainCard.add(RefreshBtn);
        RefreshBtn.setBounds(590, 560, 101, 40);

        HeadingLbl.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 24)); // NOI18N
        HeadingLbl.setForeground(new java.awt.Color(255, 255, 255));
        HeadingLbl.setText("View & Search Assets");
        MainCard.add(HeadingLbl);
        HeadingLbl.setBounds(20, 10, 260, 32);

        LogOutBtn.setBackground(new java.awt.Color(0, 102, 204));
        LogOutBtn.setForeground(new java.awt.Color(255, 255, 255));
        LogOutBtn.setText("Log Out");
        LogOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogOutBtnActionPerformed(evt);
            }
        });
        MainCard.add(LogOutBtn);
        LogOutBtn.setBounds(840, 560, 100, 40);

        DeleteAssetBtn.setBackground(new java.awt.Color(0, 102, 204));
        DeleteAssetBtn.setForeground(new java.awt.Color(255, 255, 255));
        DeleteAssetBtn.setText("Delete");
        DeleteAssetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteAssetBtnActionPerformed(evt);
            }
        });
        MainCard.add(DeleteAssetBtn);
        DeleteAssetBtn.setBounds(720, 610, 101, 40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(MainCard, javax.swing.GroupLayout.PREFERRED_SIZE, 966, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainCard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void SearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchBtnActionPerformed
        // TODO add your handling code here:
        String[][] arr; 
        int r = t1.getRowCount();
        if(AssetDetTxt.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null, "Enter Asset ID or Asset Name");
        }        
        else
        {
            tableset();
            arr = ConvertToArray();             
            search(arr);
        }
    }//GEN-LAST:event_SearchBtnActionPerformed

    private void EditAssetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditAssetBtnActionPerformed
        // TODO add your handling code here:
        if(t1.getSelectionModel().isSelectionEmpty())
            {
                JOptionPane.showMessageDialog(null, "Select an asset");            
            }
            else
        {
        String s = getdet();
        System.out.println("s = " + s);
        ModifyAsset myInst = new ModifyAsset();
        myInst.setVisible(true);
        myInst.getdet(s);
        }

        
    }//GEN-LAST:event_EditAssetBtnActionPerformed

    private void RefreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshBtnActionPerformed
            // TODO add your handling code here:
        ViewTable();
        cellfiller();
    }//GEN-LAST:event_RefreshBtnActionPerformed

    private void ExportDetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportDetBtnActionPerformed
        /*Filechooser fc = new Filechooser();
        //fc.setVisible(true);
        String path = fc.getselfile();
        System.out.println(path);
        if(path != null)
        exportsummary(path);        */
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
          System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
          System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
        }
        else 
        {
          System.out.println("No Selection ");
        }
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
    }//GEN-LAST:event_ExportDetBtnActionPerformed

    private void BackBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackBtnActionPerformed
        // TODO add your handling code here:
        new MainMenu().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BackBtnActionPerformed

    private void OrderCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OrderCBActionPerformed
        int index = OrderCB.getSelectedIndex();
        int rows = t1.getRowCount();
        System.out.println("Rows = " + rows);
        if(index == 1)
        {
            String[][] arr = ConvertToArray();
            Sorter(arr, 0);
        }
        
        if(index == 2)
        {
            String[][] arr = ConvertToArray();
            Sorter(arr, 1);
        }
        
        if(index == 3)
        {
            String[][] arr = ConvertToArray();
            Sorter(arr, 4);
        }
        
        if(index == 4)
        {
            String[][] arr = ConvertToArray();
            Sorter(arr, 3);
        }
        
        if(index == 5)
        {
            String[][] arr = ConvertToArray();
            Sorter(arr, 6);
        }                                      
    }//GEN-LAST:event_OrderCBActionPerformed
    
    private void t1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t1MouseClicked
        // TODO add your handling code here:
        EditAssetBtn.setVisible(true);
        RefreshBtn.setVisible(true);
    }//GEN-LAST:event_t1MouseClicked

    private void none(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_none
        // TODO add your handling code here:
        EditAssetBtn.setVisible(true);
    }//GEN-LAST:event_none

    private void GroupCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupCBActionPerformed
        // TODO add your handling code here:
        ComboBoxHandler();                   
    }//GEN-LAST:event_GroupCBActionPerformed

    private void LogOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogOutBtnActionPerformed
        logout();
    }//GEN-LAST:event_LogOutBtnActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        logout();
    }//GEN-LAST:event_formWindowClosing

    private void DeleteAssetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteAssetBtnActionPerformed
        // TODO add your handling code here:
                    // TODO add your handling code here:
            if(t1.getSelectionModel().isSelectionEmpty())
            {
                JOptionPane.showMessageDialog(null, "Select an asset");            
            }
            else
            deleteasset();

    }//GEN-LAST:event_DeleteAssetBtnActionPerformed

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
            java.util.logging.Logger.getLogger(SearchAssets.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SearchAssets.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SearchAssets.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SearchAssets.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SearchAssets().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AssetDetTxt;
    private javax.swing.JLabel AssetTypeLbl;
    private javax.swing.JButton BackBtn;
    private javax.swing.JLabel ClockLbl;
    private javax.swing.JLabel ClockLbl1;
    private javax.swing.JLabel CountLbl;
    private javax.swing.JButton DeleteAssetBtn;
    private javax.swing.JButton EditAssetBtn;
    private javax.swing.JButton ExportDetBtn;
    private javax.swing.JComboBox<String> GroupCB;
    private javax.swing.JLabel GroupLbl;
    private javax.swing.JLabel HeadingLbl;
    private javax.swing.JButton LogOutBtn;
    private javax.swing.JPanel MainCard;
    private javax.swing.JComboBox<String> OrderCB;
    private javax.swing.JButton RefreshBtn;
    private javax.swing.JButton SearchBtn;
    private javax.swing.JLabel SearchLbl;
    private javax.swing.JLabel SortLbl;
    private javax.swing.JLabel TypeCountLbl;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JTable t1;
    // End of variables declaration//GEN-END:variables
}
