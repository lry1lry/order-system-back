package icu.lry.ordersystem.controller;

import com.aliyuncs.exceptions.ClientException;
import icu.lry.ordersystem.utils.AlyOSSUtils;
import icu.lry.ordersystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@RestController
@RequestMapping("/upload")
@Slf4j
public class uploadController {

    @Autowired
    private AlyOSSUtils alyOSSUtils;

    @PostMapping("/changeAvatar")
    public String wenjian(MultipartFile image) throws IOException, ClientException {
        String url = alyOSSUtils.upload(image);
        log.info(url);
        return url;
    }
}
