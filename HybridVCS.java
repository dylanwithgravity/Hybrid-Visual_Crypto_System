/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hybridvcs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.imageio.ImageIO;

public class HybridVCS extends Application {
File imageFile = imageFile = new File("C:\\HW45700.bmp");
BufferedImage eImg = null;
static BufferedImage BI;
List<Byte> byte2 = new ArrayList<Byte>();
byte[] bytes; //Byte array to Encrypt
int[][]encIMG; //Encrypted Image Array

    //keys and Cipher
    KeyPair kp1 = DHCRYPT.genDHKeyPair();
    KeyPair kp2 = DHCRYPT.genDHKeyPair();
    SecretKey key1;
    SecretKey key2;               
    Cipher c;
    
    // Buttons and slider
    RadioButton btRSA, btDH,btVEA, btDES;
    Button btClear, btPoly, btEncrypt, btDisplay;
    ImageView img1, img2;
    HBox botBox;
    HBox cenBox;
    VBox leftBox;
    VBox rightBox;
    Slider slider;
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) throws IOException, IOException, IOException, IOException {

  
      
      btRSA = new RadioButton("RSA");
      btDH = new RadioButton("DH");
      btVEA = new RadioButton("VEA");
      btDES = new RadioButton("DES");
      slider = new Slider();
      slider.setMin(0);
      slider.setMax(16);
      slider.setValue(5);
      slider.setShowTickLabels(true);
      slider.setMajorTickUnit(2);
      double rounds = slider.getValue();
      System.out.println("The round number is " + rounds);
      final ToggleGroup keyGen = new ToggleGroup();
      btRSA.setToggleGroup(keyGen);
     // btRSA.setSelected(true);
      btDH.setToggleGroup(keyGen);
      //ADDITIONS
      
      keyGen.selectedToggleProperty().addListener(
      (ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
        if (keyGen.getSelectedToggle() != null) {
           if(btDH.isSelected()== true){
                kp1 = DHCRYPT.genDHKeyPair();
                kp2 = DHCRYPT.genDHKeyPair();
               // create DH public keys
              PublicKey pbk1 = kp1.getPublic();
              PublicKey pbk2 = kp2.getPublic();
               //create private DH
              PrivateKey prk1 = kp1.getPrivate();
              PrivateKey prk2 = kp2.getPrivate();
                try {
   
            key1 = DHCRYPT.agreeSecretKey(prk1, pbk2,
                    true);
            key2 = DHCRYPT.agreeSecretKey(prk2, pbk1,
                    true);
            
           
                }
            catch (Exception e) {
            e.printStackTrace();
        }                        
              System.out.println("DH Keys Generated");
               
           }
           
           
           if(btRSA.isSelected()== true){
           System.out.println("RSA Key Generated");
           }
        }    
    });
      
      final ToggleGroup encScheme = new ToggleGroup();
      btVEA.setToggleGroup(encScheme);
      //btVEA.setSelected(true);
      btDES.setToggleGroup(encScheme);
      
      encScheme.selectedToggleProperty().addListener(
      new ChangeListener<Toggle>() {

          public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
              if (encScheme.getSelectedToggle() != null) {
                  if(btDES.isSelected()==true){
                      try {
                          c = Cipher.getInstance("AES/ECB/PKCS5Padding");
                          c.init(Cipher.ENCRYPT_MODE, key1);
                      } catch (NoSuchAlgorithmException ex) {
                          Logger.getLogger(HybridVCS.class.getName()).log(Level.SEVERE, null, ex);
                      } catch (NoSuchPaddingException ex) {
                          Logger.getLogger(HybridVCS.class.getName()).log(Level.SEVERE, null, ex);
                      } catch (InvalidKeyException ex) {
                          Logger.getLogger(HybridVCS.class.getName()).log(Level.SEVERE, null, ex);
                      }
                      System.out.println("DES Selected");
                  }
              } }
      });
      
      btClear = new Button("Clear");
      btPoly = new Button("Polynomial Key Generator");
      btEncrypt = new Button("Encrypt");
      btDisplay = new Button("Display");
      
      btClear.setOnAction(e->handleButtonAction(e));
      btPoly.setOnAction(e->handleButtonAction(e));
      btEncrypt.setOnAction(e->handleButtonAction(e));
    
      
     
      
      Label Label1 = new Label("Key Exchange");
      Label Label2 = new Label("Encryption Scheme");
      Label Label3 = new Label("Hybrid Encryption Cryptographic System");
      Label3.setFont(Font.font("Cambria", 32));
      
      // Put RSA and DH Buttons in a box
      leftBox = new VBox(Label1, btRSA, btDH);
      leftBox.setSpacing(10);
      leftBox.setPadding(new Insets(20));
      
      // Put VEA and DES Buttons in a box
      rightBox = new VBox(Label2, btVEA, btDES);
      rightBox.setSpacing(10);
      rightBox.setPadding(new Insets (20));
      
      //Put Poly and maybe P2P and Multi in a box
      botBox = new HBox(btPoly, slider);
      botBox.setSpacing(10);
      botBox.setPadding(new Insets(20));
      
      // Put image and button in a box
      cenBox = new HBox();
      cenBox.setSpacing(10);
      cenBox.setPadding(new Insets(20));
      Image inImage = new Image("http://vignette2.wikia.nocookie.net/villains/images/4/4a/Team-rocket.jpg/revision/latest?cb=20130127211302");
      ImageView imageView1 = new ImageView(inImage);
      imageView1.setFitWidth(300);
      imageView1.setPreserveRatio(true);
      imageView1.setSmooth(true);
      imageView1.setCache(true);
      //ImageView imageView1 = new ImageView(new Image("http://vignette3.wikia.nocookie.net/pokemon/images/c/cd/Team_Rocket_Trio_AG.png/revision/latest?cb=20150915073715"));
      //ImageView imageView2 = new ImageView(new Image("http://vignette3.wikia.nocookie.net/pokemon/images/c/cd/Team_Rocket_Trio_AG.png/revision/latest?cb=20150915073715"));
      //cenBox.getChildren().addAll(imageView1, btEncrypt, imageView2);
      
    
    // Create a border pane 
    BorderPane pane = new BorderPane();
    pane.setPadding(new Insets(40));
    pane.setPadding(new Insets(10));
    
    // add components to regions of BorderPane
    pane.setTop(Label3);
    pane.setRight(rightBox);
    pane.setLeft(leftBox);
    pane.setBottom(botBox);
    pane.setCenter(cenBox);
    BorderPane.setAlignment(cenBox, Pos.CENTER_RIGHT);
    BorderPane.setAlignment(Label3, Pos.CENTER);
    BorderPane.setAlignment(botBox, Pos.CENTER_RIGHT);
    
    
    // Picture stuff
 BI = SwingFXUtils.fromFXImage(inImage, null); // Create a Buffered image from orginal
//Create Int Array to be encrypted.

int w = BI.getWidth();
int h = BI.getHeight();

int[][] IMG = new int[w][h];
for (int j = 0; j < w; j++) {
    for (int k = 0; k < h; k++) {
        IMG[j][k] = BI.getRGB(j, k); 
    }
}

//Additions

int index = 0;
int[] oneDImage = twoDToOne(IMG,w,h);
//IMG to byte Array


for (int i = 0; i < oneDImage.length; i++){
  byte[] byteTemp = intToByteArray(oneDImage[i]);
 for(int j = 0; j <byteTemp.length; j++){
  byte2.add(byteTemp[j]);
 }
}
//Bytes array will be encrypted
bytes = toByteArray(byte2);
//Create Byte Buffer to convert bytes into Integers
ByteBuffer bb = ByteBuffer.wrap(bytes);   
btEncrypt.setOnAction(e -> {
          try {
              encIMG = encrypt(c,bytes);
          } catch (IllegalBlockSizeException ex) {
              Logger.getLogger(HybridVCS.class.getName()).log(Level.SEVERE, null, ex);
          } catch (BadPaddingException ex) {
              Logger.getLogger(HybridVCS.class.getName()).log(Level.SEVERE, null, ex);
          }
          
    });

btDisplay.setOnAction(new EventHandler<ActionEvent>() {

          public void handle(ActionEvent e) {
              System.out.println("Displaying Image");
              eImg = createImage(encIMG,w,h);
              try {
                  ImageIO.write(eImg, "bmp", imageFile);
                  BufferedImage bImage = ImageIO.read(imageFile);
                  Image encryptedImage = SwingFXUtils.toFXImage(bImage, null);
                  ImageView imageView2 = new ImageView(encryptedImage);
                  imageView2.setFitWidth(300);
                  imageView2.setPreserveRatio(true);
                  imageView2.setCache(true);
                  
                  cenBox.getChildren().addAll(imageView2);
              } catch (IOException ex) {
                  Logger.getLogger(HybridVCS.class.getName()).log(Level.SEVERE, null, ex);
              }
          }
      });

//BufferedImage bImage = ImageIO.read(imageFile);
//Image encryptedImage = SwingFXUtils.toFXImage(bImage, null);
//ImageView imageView2 = new ImageView(encryptedImage);
cenBox.getChildren().addAll(imageView1, btEncrypt, btDisplay);


// Create a scene and place it in the stage
    BorderPane.setAlignment(cenBox, Pos.CENTER);
    BorderPane.setAlignment(Label3, Pos.CENTER);
    BorderPane.setAlignment(btPoly, Pos.CENTER);
    // Create a scene and place it in the stage
    Scene scene = new Scene(pane, 1050, 400);
    primaryStage.setTitle("Final Project"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
  }
    private void handleButtonAction(ActionEvent event)  {
        
      }

  
  public static int[][] encrypt(Cipher c, byte[] bytes) throws IllegalBlockSizeException, BadPaddingException{
  System.out.println("Generating Ciphertext");
  byte[] ciphertext = c.doFinal(bytes);  
  ByteBuffer bb = ByteBuffer.wrap(ciphertext);
  List<Integer> test = new ArrayList<Integer>();   
                       while(bb.hasRemaining()){
                     test.add(bb.getInt());
                 }
   
int w = BI.getWidth();
int h = BI.getHeight();
int[][]encImage = new int[w][h];
int index = 0;
    for(int i = 0; i <w; i++){
    for (int j = 0; j <h; j++){
    encImage[i][j] = test.get(index);
    index++;
                   } 
               } 
   
  return encImage;
  }
  
    public static List<Integer>dhDecrypt(Cipher c, byte[]ciphertext, SecretKey key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException{
         System.out.println("Beginning Decryption");  
        c.init(Cipher.DECRYPT_MODE, key);
      List<Integer> test = new ArrayList<Integer>();                                       
                  byte[] plainText = c.doFinal(ciphertext);
               
                ByteBuffer bb  = ByteBuffer.wrap(plainText);
                 while(bb.hasRemaining()){
                    test.add(bb.getInt());
                 }
                 return test;
  }
  
  public static BufferedImage createImage(int[][]a, int w, int h) {
  BufferedImage img = new BufferedImage(
    a.length, a[0].length, BufferedImage.TYPE_INT_RGB);  
    for(int x = 0; x < a.length; x++){
    for(int y = 0; y<a[x].length; y++){
        img.setRGB(x, y, (int)Math.round(a[x][y]));
    }
}
    return img;
}
  
  
public static int[] twoDToOne(int[][]a, int w, int h) {
int[]b = new int[h*w];
int index = 0;
for(int i = 0; i < w;i++){
    for (int j = 0; j < h; j++){
        b[index]=a[i][j];
        index++;
    }
}
    return b;
}
  
    public static int[][] imageToArray(BufferedImage image, int w, int h) {
        int[][]array = new int[w][h];
for (int j = 0; j < w; j++) {
    for (int k = 0; k < h; k++) {
        array[j][k] = image.getRGB(j, k);    
    }
}
return array;
}
  
  
  public static byte[] toByteArray(List<Byte> in) {
    final int n = in.size();
    byte ret[] = new byte[n];
    for (int i = 0; i < n; i++) {
        ret[i] = in.get(i);
    }
    return ret;
}
int fromByteArray(byte[] b) {
     if (b.length == 4)
      return b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8
          | (b[3] & 0xff);
    else if (b.length == 2)
      return 0x00 << 24 | 0x00 << 16 | (b[0] & 0xff) << 8 | (b[1] & 0xff);

    return 0;
}
public static int[] convertToIntArray(byte[] input)
{
    int[] ret = new int[input.length];
    for (int i = 0; i < input.length; i++)
    {
        ret[i] = input[i] & 0xff; // Range 0 to 255, not -128 to 127
    }
    return ret;
}
private byte[] intToByteArray (final int integer) throws IOException {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      DataOutputStream dos = new DataOutputStream(bos);
      dos.writeInt(integer);
      dos.flush();
      return bos.toByteArray();
}
  
  
    public static void main(String[] args) {
 
    launch(args);
  }
} 



  