import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ConnectAndSearchDB extends JFrame implements ActionListener
{
    private JTextField fieldSearch,fieldAdd;
    private JButton searchB,addB;
    private Connection connection;
    private String name;
    private ResultSet rs,rs1;

    public ConnectAndSearchDB() throws SQLException,ClassNotFoundException
    {
        setLocationRelativeTo(null);
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2,2));

        fieldSearch = new JTextField(20);
        searchB = new JButton("Search");
        fieldAdd = new JTextField(20);
        addB = new JButton("Add");

        add(searchB);
        add(fieldSearch);
        add(addB);
        add(fieldAdd);

        searchB.addActionListener(this);
        addB.addActionListener(this);

        establishConnection();

        pack();

        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        Object act = e.getSource();

        if(act.equals(searchB))
        {
            name = fieldSearch.getText();
            searchData();
        }else if(act.equals(addB))
        {
            try {
                addData();
            } catch (ClassNotFoundException e1)
            {
                e1.printStackTrace();
                System.out.println("ClassNotFound");
            } catch (SQLException e1)
            {   
                e1.printStackTrace();
                System.out.println("SQLError");
            }
        }   
    }

    public void establishConnection() throws SQLException , ClassNotFoundException
    {
        Class.forName("com.ibm.db2.jcc.DB2Driver");
        connection = DriverManager.getConnection("jdbc:db2://localhost:50000/COLINN", "Colinn","ezioauditore");     
    }


    private void searchData()
    {
        try
        {
            PreparedStatement s = null;
            String query;
            query = "SELECT * from NAMES";

            s=connection.prepareStatement(query);
            rs = s.executeQuery();

            boolean matchfound = false;

            while(rs.next())
            {
                if(rs.getString(1).equals(name))
                {
                    matchfound = true;
                    System.out.println("The name "+name+" is found in the Database");
                    break;
                }
            }

            if(matchfound == false)
            {
                System.out.println("Match Not Found");
            }   
        }
        catch(SQLException e)
        {
            e.printStackTrace();

        }
    }

    public void addData() throws ClassNotFoundException,SQLException
    {
        PreparedStatement ps = null;
        String query;
        query = "INSERT INTO NAMES VALUES('"+fieldAdd.getText()+"')";

        ps = connection.prepareStatement(query);
        rs1 = ps.executeQuery();

        System.out.println("Written Successfully");
    }

    public static void main (String args[]) throws SQLException,ClassNotFoundException
    {
        EventQueue.invokeLater(new Runnable() 
        {
            public void run()
            {
                try 
                {
                    new ConnectAndSearchDB();

                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
}