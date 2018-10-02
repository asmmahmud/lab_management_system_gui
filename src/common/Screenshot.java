package common;

public class Screenshot {
    private static volatile byte[] imageByteArray = new byte[0];
    
    public static byte[] getImageByteArray() {
        return imageByteArray;
    }
    
    public static synchronized void setImageByteArray(byte[] byeArr) {
        imageByteArray = byeArr;
    }
}
