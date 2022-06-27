package com.application.controller;

import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import com.application.mapper.UserMapper;
import com.application.model.DTO.ProblemDTO;
import com.application.model.DTO.SearchDTO;
import com.application.model.DTO.UserDTO;
import com.application.model.ResultJson;
import com.application.model.entity.Problem;
import com.application.model.entity.User;
import com.application.model.subentity.Page;
import com.application.service.ProblemService;
import com.application.service.RecordService;
import com.application.utils.UserTokenUtils;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
public class ProblemController {
    @Autowired
    private ProblemService problemService;
    @Autowired
    private UserMapper userMapper;
    @Value("${output}")
    private String outPath;

    @PutMapping("/problem")
    public ResultJson createProblem(@RequestBody ProblemDTO problemDTO, HttpServletRequest request) {
        //用户验证
        String s = UserTokenUtils.checkUser(request.getCookies());
        Assert.isTrue(s.equals("Admin"), "管理员用户验证失败");
        Problem problem = problemDTO.toEntity();
        Assert.isTrue(problem.getUserId() != null, "请设置上传者");
        problem = problemService.create(problem);
        ResultJson json = new ResultJson();
        json.ok("创建问题成功", problem);
        return json;
    }

    @GetMapping("problem/{pageSize}/{pageIndex}")
    public ResultJson getProblemList(@PathVariable("pageSize") Integer pageSize, @PathVariable("pageIndex") Integer pageIndex) {
        PageInfo<Problem> pageInfo = problemService.allList(pageSize, pageIndex);
        List<Problem> list = pageInfo.getList();
        List<ProblemDTO> collect = list.stream()
                .map(item -> new ProblemDTO(item, userMapper.selectByPrimaryKey(item.getUserId())))
                .collect(Collectors.toList());
        ResultJson json = new ResultJson();
        return json.ok("查询成功",
                new Page(pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum()),
                collect);
    }

    @GetMapping("/problem/{id}")
    public ResultJson getProblem(@PathVariable("id") Long id) {
//        ProblemDTO problem=problemService.getProblem(id);
        List<Problem> problems = problemService.getProblem(id);
        Problem problem = problems.get(0);
        User user = userMapper.selectByPrimaryKey(problem.getUserId());
        return new ResultJson().ok("查询成功", new ProblemDTO(problem, user));
    }

    @PostMapping("/search/{pageSize}/{pageIndex}")
    public ResultJson search(@RequestBody SearchDTO searchDTO, @PathVariable("pageSize") Integer pageSize, @PathVariable("pageIndex") Integer pageIndex) {
        PageInfo<ProblemDTO> pageInfo = problemService.searchProbelm(searchDTO.getName(), searchDTO.getTag(), searchDTO.getLevel(), pageSize, pageIndex);
        Page page = new Page(pageInfo.getTotal(), pageSize, pageIndex);
        return new ResultJson().ok("查询成功", page, pageInfo.getList());
    }

    @GetMapping("/tag")
    public ResultJson getTag() {
        return new ResultJson().ok("查询TAG", null, ProblemDTO.returnTag());
    }

    @GetMapping("problem")
    public ResultJson getProblemList() {
        List<ProblemDTO> problemDTOS = problemService.allListNone();
        return new ResultJson().ok("所有问题", null, problemDTOS);
    }

    @PostMapping("/private/set_library")
    public void inputProblemList() {
        final String root = "C:\\Users\\ksgfk\\Desktop\\problem";
        final File file = new File(root);
        final String[] level = new String[]{
                "大学期末", "入门", "普及", "提高", "省选", "NOI", "ICPC", "CTSC", "蓝桥", "天梯"
        };
        for (File i : Objects.requireNonNull(file.listFiles())) {
            StringBuilder sb = new StringBuilder();
            try (FileReader reader = new FileReader(i)) {
                int tem;
                while ((tem = reader.read()) != -1) {
                    sb.append((char) tem);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            final String content = sb.toString();

            int titleStart = content.indexOf("# ");
            int titleEnd = content.indexOf('\n', titleStart);
            String title = content.substring(titleStart + 2, titleEnd);
//            System.out.println(title);

            int testInputStart = content.indexOf("#### 输入样例 #1");
            int testInputEnd = content.indexOf("#### 输出样例 #1");
            String input = content.substring(testInputStart + 12, testInputEnd - 1).trim();

            int testOutputEnd = content.indexOf("## 说明", testInputEnd);
            int testOutputEnd2 = content.indexOf("#### 输入样例 #2");
            String output;
            if (testOutputEnd == -1 && testOutputEnd2 == -1) {
                output = content.substring(testInputEnd + 12).trim();
            } else if (testOutputEnd != -1 && testOutputEnd2 == -1) {
                output = content.substring(testInputEnd + 12, testOutputEnd - 1).trim();
            } else if (testOutputEnd == -1) {
                output = content.substring(testInputEnd + 12, testOutputEnd2 - 1).trim();
            } else {
                output = content.substring(testInputEnd + 12, testOutputEnd2 - 1).trim();
            }

            final Problem p = new Problem();
            p.setUserId(2L);
            p.setDescription(content);
            p.setTitle(title);
            int a1 = RandomUtil.randomInt(33);
            int a2 = RandomUtil.randomInt(33);
            int a3 = RandomUtil.randomInt(33);
            p.setTag((1L<<a1) + (1L<<a2) + (1L<<a3));//114514
            p.setLevel(level[RandomUtil.randomInt(level.length)]);
            p.setNum(1);
            Problem pro = problemService.create(p);
            Long id = pro.getId();

            final String problemTestRootPath = outPath + File.separator + id;
            String problemTestInput = problemTestRootPath + File.separator + "0.in";
            String problemTestOutput = problemTestRootPath + File.separator + "0.out";
            File rt = new File(problemTestRootPath);
            if (!rt.exists()) {
                rt.mkdirs();
            }
            {
                File inputFile = new File(problemTestInput);
                if (!inputFile.exists()) {
                    try {
                        inputFile.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try (FileWriter writer = new FileWriter(inputFile, false)) {
                    writer.write(input);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            {
                File outputFile = new File(problemTestOutput);
                if (!outputFile.exists()) {
                    try {
                        outputFile.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try (FileWriter writer = new FileWriter(outputFile, false)) {
                    writer.write(output);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
