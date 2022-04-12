package cn.wskweb.reggie_take_out.controller;


import cn.wskweb.reggie_take_out.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @创建人: wsk
 * @描述: 上传图片
 * @className（类名称）: commonController
 * @创建时间: 15:15 2022/4/5
 */
@RestController
@RequestMapping("/common")
public class commonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * @方法名: upload
     * @描述: 文件上传
     * @param: file 需要和提交表单名 name一致
     * @return:
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename(); // 获得原始文件名
        assert originalFilename != null;
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用UUID重新生成文件名
        String fileName = UUID.randomUUID() + substring;

        // 判断文件是否存在不存在创建
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        file.transferTo(new File(basePath + fileName));     // 转存至指定路径
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws Exception{
        // 输入流，读取文件
        FileInputStream fileInputStream = new FileInputStream(basePath + name);


        // 输入流，通过输出流将文件写回浏览器，在浏览器展示图片
        ServletOutputStream outputStream = response.getOutputStream();

//        response.setContentType("image/jpeg");      // 设定写入流类型

        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
            outputStream.flush();
        }

        outputStream.close();
        fileInputStream.close();
    }

}
