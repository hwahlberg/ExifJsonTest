/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExifJson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author bbnthwa
 * Just a small test parsing an EXIF-file and store
 * it as a JSON datatype i Postgres
 */
public class ExifJson {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();

        try {

            System.out.println("Reading JSON from a file");
            System.out.println("----------------------------");

            BufferedReader br2 = new BufferedReader(new FileReader(args[0]));
            BufferedReader br = new BufferedReader(new FileReader(args[0]));

            JsonElement je = new JsonParser().parse(br2);
            JsonObject jo = je.getAsJsonObject();
            //System.out.println(jo);

            PostgresDB pgdb = new PostgresDB();
            pgdb.open("localhost", "bbnthwa", "bbnthwa", "230volt");
            pgdb.setWriteflag(true);
            pgdb.writeJsonObject(jo);

            //convert the json string back to object  
            Camera camera = gson.fromJson(br, Camera.class);
            System.out.println("SourceFile=    " + camera.SourceFile);
//             System.out.println("Warning=       " + camera.ExifTool.getWarning());

            System.out.println("filename=         " + camera.File.getFileName());
            System.out.println("directory=        " + camera.File.getDirectory());
            if (camera.PNG == null) {
                System.out.println("ImageWidth=       " + camera.File.getImageWidth());
                System.out.println("ImageHeght=       " + camera.File.getImageHeight());
            }
            if (camera.EXIF != null) {
                System.out.println("Make=             " + camera.EXIF.getMake());
                System.out.println("ExposureTime=     " + camera.EXIF.getExposureTime());
                System.out.println("ExposureProgram=  " + camera.EXIF.getExposureProgram());
            }

            if (camera.PNG != null) {
                System.out.println("ImageWidth=      " + camera.PNG.getImageWidth());
                System.out.println("ImageHeight=     " + camera.PNG.getImageHeight());
                System.out.println("ModifyDate=      " + camera.PNG.getModifyDate());
            }

            // print out whole EXIF-file
            System.out.println("-> " + gson.toJson(camera));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Camera {

        private String SourceFile;
        private Filedata File;
        private Exiftool ExifTool;
        private Exif EXIF;
        private Png PNG;

    }

    /**
     *
     */
    class Filedata {

        private String FileName;
        private String Directory;
        private String FileType;
        private int ImageWidth;
        private int ImageHeight;

        public Filedata() {

        }

        public String getFileName() {
            return FileName;
        }

        public void setFileName(String FileName) {
            this.FileName = FileName;
        }

        public String getDirectory() {
            return Directory;
        }

        public void setDirectory(String Directory) {
            this.Directory = Directory;
        }

        public int getImageWidth() {
            return ImageWidth;
        }

        public void setImageWidth(int ImageWidth) {
            this.ImageWidth = ImageWidth;
        }

        public int getImageHeight() {
            return ImageHeight;
        }

        public void setImageHeight(int ImageHeight) {
            this.ImageHeight = ImageHeight;
        }

        public String getFileType() {
            return FileType;
        }

    }

    /**
     *
     */
    class Exiftool {

        private String ExifToolVersion;
        private String Warning;

        public Exiftool() {
        }

        public String getExifToolVersion() {
            return ExifToolVersion;
        }

        public void setExifToolVersion(String ExifToolVersion) {
            this.ExifToolVersion = ExifToolVersion;
        }

        public String getWarning() {
            return Warning;
        }

        public void setWarning(String Warning) {
            this.Warning = Warning;
        }

    }

    /**
     *
     */
    class Png {

        private int ImageWidth;
        private int ImageHeight;
        private String ModifyDate;

        public Png() {
        }

        public int getImageWidth() {
            return ImageWidth;
        }

        public int getImageHeight() {
            return ImageHeight;
        }

        public String getModifyDate() {
            return ModifyDate;
        }

    }

}
