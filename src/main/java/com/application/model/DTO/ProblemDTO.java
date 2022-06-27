package com.application.model.DTO;

import com.application.model.entity.Problem;
import com.application.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ProblemDTO {
    private Long id;
    private UserDTO userDTO;
    private String title;
    private String description;
    private List<String> tag;
    private String level;
    private Integer num;
    private static final List<String> list = new ArrayList<String>();

    static {
        list.add("概率论与数理统计");
        list.add("高斯消元");
        list.add("立体解析几何");
        list.add("离散化");
        list.add("微分与积分");
        list.add("模拟");
        list.add("字符串");
        list.add("AC自动机");
        list.add("动态规划");
        list.add("KMP");
        list.add("平衡树");
        list.add("记忆化搜索");
        list.add("信息论");
        list.add("图论");
        list.add("最小生成树");
        list.add("拓扑排序");
        list.add("计算几何");
        list.add("凸包");
        list.add("空间加速结构");
        list.add("主席树");
        list.add("博弈论");
        list.add("珂朵莉树");
        list.add("莫队");
        list.add("快速傅里叶变换");
        list.add("矩阵乘法");
        list.add("莫比乌斯反演");
        list.add("中国剩余定律");
        list.add("A*");
        list.add("2-SAT");
        list.add("卡特兰数");
    }

    public Problem toEntity() {
        Problem problem = new Problem();
        problem.setLevel(this.level);
        problem.setTag(changTag(tag));
        problem.setTitle(title);
        problem.setDescription(description);
        if (userDTO != null)
            problem.setUserId(userDTO.getId());
        else
            problem.setUserId(2L);
        problem.setId(id);
        problem.setNum(num);
        return problem;
    }

    public ProblemDTO(Problem problem, User user) {
        id = problem.getId();
        userDTO = new UserDTO(user);
        title = problem.getTitle();
        description = problem.getDescription();
        tag = toTag(problem.getTag());
        level = problem.getLevel();
        num = problem.getNum();
    }

    public ProblemDTO(Problem problem) {
        id = problem.getId();
        userDTO = null;
        title = problem.getTitle();
        description = problem.getDescription();
        tag = toTag(problem.getTag());
        level = problem.getLevel();
        num = problem.getNum();
    }

    public ProblemDTO(User user, Problem problem) {
        id = problem.getId();
        userDTO = new UserDTO(user);
        title = problem.getTitle();
        description = null;
        tag = toTag(problem.getTag());
        level = problem.getLevel();
        num = problem.getNum();
    }

    public static Long changTag(List<String> tag) {
        Long sum = 0L;
        for (String s : tag) {
            int i = list.indexOf(s);
            if (i != -1) sum = sum | (1 << i);
        }
        return sum;
    }

    public static Long changTag(String[] tag) {
        Long sum = 0L;
        for (String s : tag) {
            int i = list.indexOf(s);
            if (i != -1) sum = sum | (1 << i);
        }
        return sum;
    }

    public static List<String> toTag(Long tag) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if ((tag & (1 << i)) > 0) strings.add(list.get(i));
        }
        return strings;
    }

    public static void addTag(String tag) {
        list.add(tag);
    }

    public static List<String> returnTag() {
        return list;
    }

}
