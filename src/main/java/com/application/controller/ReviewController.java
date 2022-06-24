package com.application.controller;

import cn.hutool.core.lang.Assert;
import com.application.model.DTO.NoticeDTO;
import com.application.model.DTO.UserDTO;
import com.application.model.ResultJson;
import com.application.model.entity.Notice;
import com.application.model.entity.User;
import com.application.service.NoticeService;
import com.application.service.ReviewService;
import com.application.service.UserService;
import com.application.utils.UserTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ReviewController {

}
