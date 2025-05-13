import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;

public class Magpies {
    public BufferedImage testImage;
    public BufferedImage referenceImage;
    private boolean[][] testArray;
    private boolean[][] referenceArray;

    public final void LoadImage(String image_to_count, String ref_image){
        try{
            testImage = ImageIO.read(new File(image_to_count));
            referenceImage= ImageIO.read(new File(ref_image));
        }catch(IOException e){
            System.out.println("Bład");
        }
    }
    boolean [][] ImageToArray(BufferedImage image){
        int height=image.getHeight();
        int width=image.getWidth();
        boolean [][] array=new boolean[width][height];
        int temp=0;
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                temp=image.getRGB(x,y);
                array[x][y] = temp != -16777216;
            }
        }
        return array;
    }
   public BufferedImage GetMagpies(){
        int ref_height=referenceImage.getHeight();
        int ref_witdh=referenceImage.getWidth();
        int height=testImage.getHeight();
        int witdh=testImage.getWidth();
        int count=0;
        ArrayList<int[]> wspolrzedne=new ArrayList<int[]>();
       for(int x=0;x<witdh-ref_witdh;x++){
           for(int y=0;y<height-ref_height;y++) {
               if (CheckMagpies(x, y)) {
                   count += 1;
                   wspolrzedne.add(new int[]{x, y});
               }
           }
       }
       System.out.println("Liczba znalezionych srok wynosi:"+count);
    return  Clear(wspolrzedne);
    }
    public boolean CheckMagpies(int wspolrzedna_sroki_x, int wspolrzedna_sroki_y) {
        int matched = 0;
        int total = 0;

        for (int x = 0; x < referenceImage.getWidth(); x++) {
            for (int y = 0; y < referenceImage.getHeight(); y++) {
                if (x + wspolrzedna_sroki_x >= testArray.length || y + wspolrzedna_sroki_y >= testArray[0].length)
                    return false;
                if (referenceArray[x][y]) {
                    total++;
                    if (testArray[x + wspolrzedna_sroki_x][y + wspolrzedna_sroki_y] == referenceArray[x][y]) {
                        matched++;
                    }
                }
            }
        }
            return (double) matched / total > 0.95; 
    }
    public BufferedImage Clear(ArrayList<int[]> wspolrzedne){
        var image = new BufferedImage(testImage.getWidth(), testImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Color white = new Color(255, 255, 255);
        int rgb = white.getRGB();

        for (var obszar : wspolrzedne) {
            int startX = obszar[0];
            int startY = obszar[1];

            for (int dx = 0; dx < referenceImage.getWidth(); dx++) {
                for (int dy = 0; dy < referenceImage.getHeight(); dy++) {
                    if (referenceArray[dx][dy]) {
                        int x = startX + dx;
                        int y = startY + dy;
                        if (x < image.getWidth() && y < image.getHeight()) {
                            image.setRGB(x, y, rgb);
                        }
                    }
                }
            }
        }
        return image;
    }

    public void DisplayImage(BufferedImage image, String nazwaPliku) {
        try {
            File outputFile = new File(nazwaPliku + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisu obrazu: " + e.getMessage());
        }
    }
    public void Scan_image(){
        testArray=ImageToArray(testImage);
        referenceArray=ImageToArray(referenceImage);
    }
    public void print_array( boolean[][] array){
        for (int y = 0; y < array[0].length; y++) {
            for (int x=0;x<array.length;x++)
                if (!array[x][y]) {
                    System.out.print("  ");
                } else {
                    System.out.print(" 1 ");
                }
            System.out.println();
        }
    }
    public void Test(){
//        print_array(testArray);
        print_array(referenceArray);
    }
    public static void main(String [] args){
        Magpies obj=new Magpies();
        obj.LoadImage("testImage.tif","refImage.tif");
        obj.Scan_image();
//        obj.Test();
       BufferedImage img= obj.GetMagpies();
        obj.DisplayImage(img,"test");
    }
}



