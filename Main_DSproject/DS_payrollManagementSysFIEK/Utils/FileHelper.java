package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileHelper {
    private static FileHelper instance;

    private String rootDir;

    private FileHelper() {
    }

    public static FileHelper get() {
        if (instance == null) {
            instance = new FileHelper();
            instance.rootDir = instance.getClass().getClassLoader().getResource(".").getPath();
            instance.rootDir = instance.rootDir.substring(0, instance.rootDir.length() - 1);
        }
        return instance;
    }

    public String getRootDir() {
        return rootDir;
    }

    public String getMediaDir() {
        return (rootDir + "/resources/audio").replace("%20"," ");  // C:\Users\Erlis Lushtaku\Desktop
    }

//    public void loadImageDir() throws Exception {
//        File file = new File(getImageDir());
//        if (!file.isDirectory() && !file.mkdirs()) {
//            throw new Exception("Could not create image directory");
//        }
//    }

    public String fileExt(File path) {
        String[] chunks = path.getName().split("\\.");
        return chunks.length > 0 ? chunks[chunks.length - 1] : "";
    }

    public void copyFile(File src, File dest) throws Exception {
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dest, false);
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        out.close();
        in.close();
    }
}
