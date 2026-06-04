
package mx.com.ebcon.utils;

import java.util.List;
import java.util.Base64;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

public class Tools
{
    private static final String slash = File.separator;

    public static boolean saveFileBase64(String filepath, String filename, String filetype, String base64)
    {
        if( filepath == null || filename == null || filetype == null || base64 == null ){ return false; }
        if( filepath.isBlank() || filename.isBlank() || filetype.isBlank() || base64.isBlank() ){ return false; }

        File workspace = new File(filepath);
        if( !workspace.exists() ){ if( !workspace.mkdirs() ){ return false; } }
        if( !filepath.endsWith(slash) ){ filepath += slash; }
        File file = new File(filepath + filename + filetype);
        if( file.exists() ){ if( !file.delete() ){ return false; } }

        try
        {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            stream.write(Base64.getDecoder().decode(base64)); // Must check the file at the end
            stream.flush();
            stream.close();
            return true;
        }   catch(IOException e)
        {
            return false;
        }
    }

    public static String unzipFile(String filepath, String filename)
    {
        if( filepath == null || filename == null || filepath.isBlank() || filename.isBlank() )
        {
            return "ERROR\nInputs strings musn't be null or empty.";
        }

        if(!filepath.endsWith(slash))
        {
            filepath += slash;
        }

        File zpath = new File(filepath);
        File zfile = new File(filepath + filename);

        if(!zpath.isDirectory())
        {
            return "ERROR\nPath: " + filepath + " not found or isn't a directory";
        }

        if(!zfile.isFile())
        {
            return "ERROR\nFile: " + filename + " not found or isn't a file.";
        }

        try
        {
            FileInputStream fis = new FileInputStream(filepath + filename);
            ZipInputStream zip = new ZipInputStream(fis);
            ZipEntry entry = zip.getNextEntry();
            byte buffer[] = new byte[8192];
            int blockSize = 8192;
            String lines = "OK";
            zpath = null;

            while( entry != null )
            {
                filename = entry.getName();
                blockSize = zip.read(buffer);
                File file = new File(filepath + filename);
                FileOutputStream fos = new FileOutputStream(file);
                lines += "\n" + filename;

                while( blockSize > 0 )
                {
                    fos.write(buffer, 0, blockSize);
                    blockSize = zip.read(buffer);
                }

                fos.close();
                zip.closeEntry();
                entry = zip.getNextEntry();
            }

            zip.close();
            fis.close();
            zfile.delete();
            return lines;
        }   catch(IOException e)
        {
            return "ERROR\n" + e.getMessage();
        }
    }

    public static List<CFDI> getMetadata(String filename)
    {
        List<CFDI> datalist = new ArrayList<CFDI>();
        if( filename == null || filename.isBlank() ){ return datalist; }

        try
        {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            line = br.readLine();

            while( line != null )
            {
                String[] items = line.split("~");
                line = br.readLine();

                if( items.length == 12 )
                {
                    CFDI cfdi = new CFDI();
                    cfdi.uuid           = items[0];
                    cfdi.rfce           = items[1];
                    cfdi.rfcename       = items[2];
                    cfdi.rfcr           = items[3];
                    cfdi.rfcrname       = items[4];
                    cfdi.rfcProvCertif  = items[5];
                    cfdi.date           = items[6];
                    cfdi.stampdate      = items[7];
                    cfdi.type           = items[9];
                    cfdi.cancelled      = items[11];
                    cfdi.total =analyzer(items[8]);
                    cfdi.iscancelled    = true;
                    cfdi.currency       = "XX";
                    datalist.add(cfdi);
                }
            }

            fr.close();
            br.close();
        }   catch(IOException e){}

        return datalist;
    }

    public static float analyzer(String value)
    {
        value = (value == null || value.isBlank()) ? "0" : value;
        try { return Float.parseFloat(value); } catch(NumberFormatException e){ return 0; }
    }
}
