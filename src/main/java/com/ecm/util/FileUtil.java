package com.ecm.util;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

    /**上传文件
     *
     * @param saveFilePath 上传文件保存的路径
     * @param saveFileName 上传文件保存的文件名
     * @param uploadFile 上传的文件
     * @return 上传文件保存是否成功
     */
    public static boolean uploadFile(String saveFilePath, String saveFileName, MultipartFile uploadFile){
        File saveFile = new File(saveFilePath, saveFileName);
        if (!saveFile.getParentFile().exists()) {  //判断要保存文件的路径是否存在，如果不存在就创建一个
            saveFile.getParentFile().mkdirs();
        }
        try {
            uploadFile.transferTo(new File(saveFilePath + File.separator + saveFileName)); //将上传文件保存到一个目标文件当中
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**解压RAR文件
     *
     * @param srcRarPath 要解压的源RAR文件路径
     * @param destDirectoryPath 解压的目标文件夹路径
     * @return 是否解压成功
     */
    public static boolean unrarFile(String srcRarPath, String destDirectoryPath) {
        Archive archive = null;
        FileOutputStream fos = null;

        try {
            File file = new File(srcRarPath);
            if(file != null) {
                archive = new Archive(file);
                if (archive != null) {
                    archive.getMainHeader().print(); // 打印文件信息.
                    FileHeader fh = archive.nextFileHeader();
                    int count = 0;
                    File destFileName = null;
                    while (fh != null) {
                        String compressFileName = "";
                        System.out.println(fh.isUnicode());
                        // 判断文件路径是否有中文
                        if (existChinese(fh.getFileNameW())) { //如果是中文路径，调用getFileNameW()方法，否则调用getFileNameString()方法，还可以使用if(fh.isUnicode())
                            System.out.println((++count) + ") " + fh.getFileNameW());
                            compressFileName = fh.getFileNameW().trim();
                        } else {
                            System.out.println((++count) + ") " + fh.getFileNameString());
                            compressFileName = fh.getFileNameString().trim();
                        }
                        destFileName = new File(destDirectoryPath + "/" + compressFileName);
                        if (fh.isDirectory()) { //如果是文件夹，则在目标解压路径中新建文件夹
                            if (!destFileName.exists()) {
                                destFileName.mkdirs();
                            }
                            fh = archive.nextFileHeader();
                            continue;
                        }
                        if (!destFileName.getParentFile().exists()) {
                            destFileName.getParentFile().mkdirs();
                        }
                        fos = new FileOutputStream(destFileName);
                        archive.extractFile(fh, fos);
                        fos.close();
                        fh = archive.nextFileHeader();
                    }
                    archive.close();
                    return true;
                }
            }
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**解压ZIP文件
     *
     * @param srcRarPath 要解压的源ZIP文件路径
     * @param destDirectoryPath 解压的目标文件夹路径
     * @return 是否解压成功
     */
    public static boolean unzipFile(String srcRarPath, String destDirectoryPath) {
        File destFile = new File(destDirectoryPath);
        if(!destFile.exists()){
            destFile.mkdirs();
        }
        ZipFile zip = null;
        try {
            zip = new ZipFile(new File(srcRarPath),"UTF-8");
            for(Enumeration entries = zip.getEntries(); entries.hasMoreElements();){
                ZipEntry entry =(ZipEntry)entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in =zip.getInputStream(entry);
                String outPath = destDirectoryPath + zipEntryName;
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
                if(!file.exists()){
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if(new File(outPath).isDirectory()){
                    continue;
                }
                OutputStream out = new FileOutputStream(outPath);

                byte[] buf1 = new byte[1024];
                int len;
                while((len=in.read(buf1))>0){
                    out.write(buf1,0,len);
                }

                in.close();
                out.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     *
     * @param directoryPath 要删除的文件夹名称
     * @return boolean 是否删除成功
     */
    public static boolean deletefile(String directoryPath) {
        try {
            File file = new File(directoryPath);
            if (!file.isDirectory()) { //如果不是目录，删除该文件
                file.delete();
            }
            else {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(directoryPath + File.separator + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    }
                    else {
                        deletefile(directoryPath + File.separator + filelist[i]);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**判断文件是否为某个特定的文件类型
     *
     * @param filename 文件名
     * @param typeSuffix 文件类型后缀
     * @return 文件是否为该文件类型
     */
    public static boolean checkFileIsCertainType(String filename, String typeSuffix) {
        String filenameSuffix = filename.substring(filename.lastIndexOf(".")+1, filename.length()); // 获取文件后缀
        if (filenameSuffix.toLowerCase().equals(typeSuffix)) { //文件类型名称统一转换为小写，然后比较
            return true;
        }
        return false;
    }

    /**判断字符串中是否含有中文
     *
     * @param str 字符串
     * @return 是否含有中文
     */
    public static boolean existChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            return true;
        }
        return false;
    }

}
