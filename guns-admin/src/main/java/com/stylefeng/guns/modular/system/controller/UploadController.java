package com.stylefeng.guns.modular.system.controller;


import com.stylefeng.guns.config.properties.GunsProperties;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.common.JsonResult;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController {

    private static String PREFIX = "/upload/user/";

    @Autowired
    private GunsProperties gunsProperties;

    /**
     * 上传图片(上传到项目的webapp/static/img)
     */
    @RequestMapping(method = RequestMethod.POST, path = "/img")
    @ResponseBody
    public Object img(@RequestPart("file") MultipartFile picture) {
        String pictureName = UUID.randomUUID().toString() + ".jpg";
        try {
            String fileSavePath = gunsProperties.getFileUploadPath();
            picture.transferTo(new File(fileSavePath + pictureName));
        } catch (Exception e) {
            throw new GunsException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return new JsonResult(200, "上传成功", pictureName);
    }

    /**
     * 上传文件
     */
    @RequestMapping(method = RequestMethod.POST, path = "/file")
    @ResponseBody
    public Object file(@RequestPart("file") MultipartFile multipartFile) {
        String ext = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String filePath = "upload";//UUID.randomUUID().toString();
        String fileName = DateUtil.getAllTime() + "_" + multipartFile.getOriginalFilename();

        File file = new File(gunsProperties.getFileUploadPath() + "/" + filePath);
        //如果文件夹不存在则创建
        if  (!file .exists()  && !file .isDirectory()) {
            file.mkdir();
        }
        System.out.println(file.getPath());
        try {
            multipartFile.transferTo(new File(file.getPath() + "/" +  fileName));
        } catch (Exception e) {
            throw new GunsException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return new JsonResult(200, "上传成功", filePath + "/" + fileName);
    }

    /**
     * 下载已上传文件
     *
     * @param fileName
     * @param response
     * @return
     */
    @RequestMapping("/download")
    public String downloadFile(@RequestParam("fileName") String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        if (fileName != null) {
            File file = new File(gunsProperties.getFileUploadPath() + "/" +fileName);
            if (file.exists()) {
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                // 设置文件名
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes(), "ISO-8859-1"));
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 下载附件
     * @param fileName
     * @param request
     * @param response
     */
    @RequestMapping(value = "/download-file")
    public void downloadFileAction(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        try {
            //设置文件路径
            File file = new File(gunsProperties.getFileUploadPath() + fileName);
            fis = new FileInputStream(file);
//            response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
            /*如果文件名有中文的话，进行URL编码，让中文正常显示*/
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

            IOUtils.copy(fis,response.getOutputStream());
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
