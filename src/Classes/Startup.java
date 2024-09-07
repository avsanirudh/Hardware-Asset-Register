/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author Anirudh
 */
public class Startup {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {    
        ProgressBar p = new ProgressBar();
        p.setVisible(true);
    
        for(int i = 0; i<=100; i++)
        {
            p.pb.setValue(p.pb.getValue()+1);            
            try
            {
                Thread.sleep(20);
                if(i==100)
                {
                    new Home().setVisible(true);
                    p.dispose();
                }
            }
            catch(InterruptedException e)
            {
                System.out.println(e.getMessage());
            }
        } 
    }
    
}
