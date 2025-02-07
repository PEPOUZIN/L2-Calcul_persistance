package client;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;

import server.Server;
import utils.StatsClient;

public class ClientInterface extends javax.swing.JFrame {

    private Hashtable< Byte , BigInteger > occurences;  // Byte au lieu de Integer car byte => 1 octet alors que int => 4
    
    private ArrayList< String > highestPers; 		// liste des nombres avec la plus grande persistance
    
    private Server server;
    
    private StatsClient stclient;                       // le thread qui va ecire les resultats dans le client
    
    private boolean launched;                          // pour savoir si le serveur a ete lance
    
    public ClientInterface() {
        
        initComponents();
       
        this.server = new Server(); 
        
        this.launched = false;       
       
        // on passe en parametre les composants lies aux statistiques a aficher
        this.stclient = new StatsClient( this , this.jTAOccurences , this.AverageLocation , this.MedianLocation , 
                                            this.highestPerstLocation , this.jTAHighPerst);
              
    }    
        
    public Server getServer(){
        return this.server;
    }  
       
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelSingleRes = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTFSingleRes = new javax.swing.JTextField();
        jBtnSingleRes = new javax.swing.JButton();
        jLabSingleRes = new javax.swing.JLabel();
        jPanelAverage = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        AverageLocation = new javax.swing.JLabel();
        jPanelMedian = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        MedianLocation = new javax.swing.JLabel();
        jPanelHighPerst = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        highestPerstLocation = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanelMultipleRes = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTFMultipleResMin = new javax.swing.JTextField();
        jTFMultipleResMax = new javax.swing.JTextField();
        jBtnMultipleRes = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTAMultipleRes = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTAOccurences = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTAHighPerst = new javax.swing.JTextArea();
        jPanel8 = new javax.swing.JPanel();
        jTBServ = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Interface Client");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(900, 600));

        jPanel1.setLayout(new java.awt.GridLayout(4, 1));

        jPanelSingleRes.setLayout(new java.awt.GridLayout(2, 1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Résultat pour un seul nombre :");
        jPanelSingleRes.add(jLabel4);

        jPanel2.setLayout(new java.awt.GridLayout(1, 3));

        jTFSingleRes.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTFSingleRes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFSingleRes.setText("Entrez un nombre");
        jTFSingleRes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFSingleResFocusGained(evt);
            }
        });
        jPanel2.add(jTFSingleRes);

        jBtnSingleRes.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jBtnSingleRes.setText("Résultat");
        jBtnSingleRes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBtnSingleRes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnSingleResActionPerformed(evt);
            }
        });
        jPanel2.add(jBtnSingleRes);

        jLabSingleRes.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabSingleRes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jLabSingleRes);

        jPanelSingleRes.add(jPanel2);

        jPanel1.add(jPanelSingleRes);

        jPanelAverage.setLayout(new java.awt.GridLayout(1, 2));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setLabelFor(AverageLocation);
        jLabel1.setText("Moyenne des persistances :");
        jLabel1.setMinimumSize(new java.awt.Dimension(233, 15));
        jPanelAverage.add(jLabel1);

        AverageLocation.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        AverageLocation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AverageLocation.setPreferredSize(new java.awt.Dimension(15, 15));
        jPanelAverage.add(AverageLocation);

        jPanel1.add(jPanelAverage);

        jPanelMedian.setLayout(new java.awt.GridLayout(1, 2));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setLabelFor(MedianLocation);
        jLabel2.setText("Médiane des persistances :");
        jPanelMedian.add(jLabel2);

        MedianLocation.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        MedianLocation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MedianLocation.setPreferredSize(new java.awt.Dimension(15, 15));
        jPanelMedian.add(MedianLocation);

        jPanel1.add(jPanelMedian);

        jPanelHighPerst.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanelHighPerst.setLayout(new java.awt.GridLayout(1, 2));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setLabelFor(highestPerstLocation);
        jLabel3.setText("Plus grande persistance trouvée :");
        jPanelHighPerst.add(jLabel3);

        highestPerstLocation.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        highestPerstLocation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        highestPerstLocation.setPreferredSize(new java.awt.Dimension(15, 15));
        jPanelHighPerst.add(highestPerstLocation);

        jPanel1.add(jPanelHighPerst);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel7.setLayout(new java.awt.GridLayout(2, 2));

        jPanel5.setLayout(new java.awt.GridLayout(2, 1));

        jPanelMultipleRes.setLayout(new java.awt.GridLayout(1, 2));

        jPanel6.setLayout(new java.awt.GridLayout(2, 1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Résultat pour un intervalle de nombres :");
        jPanel6.add(jLabel5);

        jPanel3.setLayout(new java.awt.GridLayout(1, 3));

        jTFMultipleResMin.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTFMultipleResMin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMultipleResMin.setText("Entrez le minimum");
        jTFMultipleResMin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFMultipleResMinFocusGained(evt);
            }
        });
        jPanel3.add(jTFMultipleResMin);

        jTFMultipleResMax.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTFMultipleResMax.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMultipleResMax.setText("Entrez le maximum");
        jTFMultipleResMax.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFMultipleResMaxFocusGained(evt);
            }
        });
        jPanel3.add(jTFMultipleResMax);

        jBtnMultipleRes.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jBtnMultipleRes.setText("Résultats");
        jBtnMultipleRes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBtnMultipleRes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnMultipleResActionPerformed(evt);
            }
        });
        jPanel3.add(jBtnMultipleRes);

        jPanel6.add(jPanel3);

        jPanelMultipleRes.add(jPanel6);

        jPanel5.add(jPanelMultipleRes);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setLabelFor(jTAOccurences);
        jLabel6.setText("Nombre d'occurences par persistance :");
        jPanel5.add(jLabel6);

        jPanel7.add(jPanel5);

        jPanel4.setLayout(new java.awt.GridLayout(2, 1));

        jTAMultipleRes.setColumns(20);
        jTAMultipleRes.setRows(5);
        jScrollPane1.setViewportView(jTAMultipleRes);

        jPanel4.add(jScrollPane1);

        jTAOccurences.setColumns(20);
        jTAOccurences.setRows(5);
        jScrollPane2.setViewportView(jTAOccurences);

        jPanel4.add(jScrollPane2);

        jPanel7.add(jPanel4);

        jPanel9.setLayout(new java.awt.GridLayout(1, 1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setLabelFor(jTAHighPerst);
        jLabel7.setText("Nombres ayant la plus haute persistance :");
        jPanel9.add(jLabel7);

        jPanel7.add(jPanel9);

        jTAHighPerst.setColumns(20);
        jTAHighPerst.setRows(5);
        jScrollPane3.setViewportView(jTAHighPerst);

        jPanel7.add(jScrollPane3);

        getContentPane().add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel8.setLayout(new java.awt.GridLayout(1, 2));

        jTBServ.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTBServ.setText("Lancer le serveur");
        jTBServ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTBServActionPerformed(evt);
            }
        });
        jPanel8.add(jTBServ);

        getContentPane().add(jPanel8, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnSingleResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnSingleResActionPerformed
        
        // si le serveur n'est pas lancé et qu'un nombre est demande
        if( !launched && this.jTFSingleRes.getText().matches("[0-9]+") ){
            
             this.jLabSingleRes.setText( "Le serveur n'est pas lancé" ); 
             
         // si rien n'est renseigne     
        }else if( this.jTFSingleRes.getText().equals("Entrez un nombre") || this.jTFSingleRes.getText().equals("") ) {
            
            this.jLabSingleRes.setText( "Veuillez entrer un nombre" );
        
        // sinon si il est lance et qu'on a demande un nombre
        }else if( launched && this.jTFSingleRes.getText().matches("[0-9]+")){
            
            // on ecrit le resultat dans jLabSingleRes
            this.jLabSingleRes.setText( this.server.getPerstNb( new BigInteger ( this.jTFSingleRes.getText()  ) ) + "" );  
        }   
       
    }//GEN-LAST:event_jBtnSingleResActionPerformed

    private void jTBServActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTBServActionPerformed
        // on force le focus sur la zone de texte vide, comme ca les texte des autres zone n'est pas efface
        this.jTAMultipleRes.requestFocus();
        
        this.launched = true;
        
        // pour empecher de lancer plusieurs serveurs...
        this.jTBServ.setEnabled(false);
        
        this.server.start();
        this.stclient.start();
    }//GEN-LAST:event_jTBServActionPerformed

    private void jTFSingleResFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFSingleResFocusGained
         // on vide la zone de texte au focus, cela evite de devoir supprimer manuellement le texte...
        jTFSingleRes.setText("");
    }//GEN-LAST:event_jTFSingleResFocusGained

    private void jTFMultipleResMinFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFMultipleResMinFocusGained
         // on vide la zone de texte au focus, cela evite de devoir supprimer manuellement le texte...
        jTFMultipleResMin.setText("");
    }//GEN-LAST:event_jTFMultipleResMinFocusGained

    private void jTFMultipleResMaxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFMultipleResMaxFocusGained
        // on vide la zone de texte au focus, cela evite de devoir supprimer manuellement le texte...
        jTFMultipleResMax.setText("");
    }//GEN-LAST:event_jTFMultipleResMaxFocusGained

    private void jBtnMultipleResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnMultipleResActionPerformed
             
        // si le serveur n'a pas demarre
        if(!launched){
            
            this.jTAMultipleRes.setText( "Le serveur n'a pas démarré." );
            return ;
            
        // si on n'a pas renseigne de nombres    
        }else if( !( this.jTFMultipleResMin.getText().matches("[0-9]+") && this.jTFMultipleResMin.getText().matches("[0-9]+") ) ){
            
            this.jTAMultipleRes.setText( "Entrez des nombres." );
            return ;
        
        }else{      
               
            BigInteger min = new BigInteger(this.jTFMultipleResMin.getText());
            BigInteger max = new BigInteger(this.jTFMultipleResMax.getText());
            
            // si l'intervalle ne contient qu'un nombre
            if( min.equals(max) ){
            
                this.jTAMultipleRes.setText( min.toString() + ":" + this.server.getPerstNb( min ) );
                return ;
                
            // si l'intervalle demande est plus grand que la taille max des ArrayList ==> erreur    
            } else if( max.subtract( min ).compareTo( BigInteger.valueOf( 2147483647 ) ) == 1 ) {
            
                 this.jTAMultipleRes.setText("L'intervalle demandé est trop grand, il doit être inférieur à 2 147 483 647 .");
                 return ;
                 
            // sinon si max < min ==> erreur
            }else if( max.compareTo(min) == -1 ) {

                this.jTAMultipleRes.setText("Le minimum ne peut être plus grand que le maximum.");
                return ;
                
            }   
            
                // sinon on ecrit les resultats dans la zone prevue
                printMultipleRes( this.server.getResultsInterv( min, max ) );      
            
        }   
        
    }//GEN-LAST:event_jBtnMultipleResActionPerformed

    private void printMultipleRes( ArrayList< String > p_res ){
        
        this.jTAMultipleRes.setText("");
        
        for( String s : p_res ) {
            this.jTAMultipleRes.append(s+"\n");
        } 
        
    }
    
    public static void main(String args[]) {
          
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("FlatLaf Dark".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                ClientInterface ci = new ClientInterface();
                ci.setVisible(true);
                ci.setSize(1300,850);              
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AverageLocation;
    private javax.swing.JLabel MedianLocation;
    private javax.swing.JLabel highestPerstLocation;
    private javax.swing.JButton jBtnMultipleRes;
    private javax.swing.JButton jBtnSingleRes;
    private javax.swing.JLabel jLabSingleRes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelAverage;
    private javax.swing.JPanel jPanelHighPerst;
    private javax.swing.JPanel jPanelMedian;
    private javax.swing.JPanel jPanelMultipleRes;
    private javax.swing.JPanel jPanelSingleRes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTAHighPerst;
    private javax.swing.JTextArea jTAMultipleRes;
    private javax.swing.JTextArea jTAOccurences;
    private javax.swing.JToggleButton jTBServ;
    private javax.swing.JTextField jTFMultipleResMax;
    private javax.swing.JTextField jTFMultipleResMin;
    private javax.swing.JTextField jTFSingleRes;
    // End of variables declaration//GEN-END:variables
}
