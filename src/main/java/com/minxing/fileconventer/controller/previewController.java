package com.minxing.fileconventer.controller;

import com.alibaba.fastjson.JSONObject;
import com.minxing.fileconventer.util.FileUtils;
import com.minxing.fileconventer.util.Office2PDFUtil;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.asn1.ocsp.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/api/v2/conventer")
public class previewController {
    private Logger log = LoggerFactory.getLogger(previewController.class);
    //获取原始路径
    @Value("${tmp.rootTemp}")
    private String tmp_rootTemp;
    //转化后路径
    @Value("${tmp.root}")
    private String tmp_root;
    @Value("${soffice.home}")
    private String soffice_home;

    /**
     * 文件上传和预览
     *
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        JSONObject jo = new JSONObject();
        //返回请求路径
        String returnUrl = null;
        if (file.isEmpty()) {
            jo.put("message", "上传文件为空");
            jo.put("returnUrl", returnUrl);
            return jo.toJSONString();
        }

        String fileName = file.getOriginalFilename();
        //判断所传文件是否已经存在

        //保存文件
        File saveFive = null;
        //上传路径
        if (fileName.endsWith("pdf")) {
            saveFive = new File(tmp_root + File.separator + fileName);
            //判断是不是重复文件
            if (FileUtils.scanFile(tmp_root,fileName)){
                //重复文件
                jo.put("message","上传文件已存在");
                return jo.toJSONString();
            }


        } else {
            //保存文件路径
            saveFive = new File(tmp_rootTemp + File.separator + fileName);
            //判断是不是重复文件
            if (FileUtils.scanFile(tmp_rootTemp,fileName)){
                //重复文件
                jo.put("message","上传文件已存在");
                return jo.toJSONString();
            }
        }

        // 判断是否存在这个目录
        if (!saveFive.getParentFile().exists()) {
            //如果没有，生成这个目录
            saveFive.getParentFile().mkdirs();
        }
        //输出文件
        try {
            BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(saveFive));
            bo.write(file.getBytes());
            bo.flush();
            bo.close();
            //如果文件是pdf 需要文件转换
            if (!fileName.endsWith("pdf")) {
                File newFile = Office2PDFUtil.openOfficeToPDF(saveFive.getPath(), fileName,soffice_home,tmp_root);
                if (null == newFile) {
                    log.info("-----上传文件失败，请重新上传");
                    jo.put("message", "上传文件失败，请重新上传");
                    jo.put("returnUrl", returnUrl);
                    return jo.toJSONString();
                }
                  fileName=newFile.getName();
                  log.info("转化后文件的名字="+fileName);
            }
            //  request.getScheme()协议类型 request.getServerName() 服务器ip    request.getServerPort()服务器端口号 tomcat 启动需要添加项目名
            returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/file-conventer/conventer/" + fileName;
            jo.put("returnUrl", returnUrl);
            return jo.toJSONString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jo.put("returnUrl", returnUrl);
        return jo.toJSONString();
    }


}
