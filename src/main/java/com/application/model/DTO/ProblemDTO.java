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
    private static final List<String> list=new  ArrayList<String>();
    static{
        list.add("dp");
        list.add("dfs");
        list.add("bfs");
        list.add("递归");
        list.add("二叉树");

    }
    public Problem toEntity(){
        Problem problem=new Problem();
        problem.setLevel(this.level);
        problem.setTag(changTag(tag));
        problem.setTitle(title);
        problem.setDescription(description);
        problem.setUserId(userDTO.getId());
        problem.setId(id);
        return problem;
    }
    public ProblemDTO(Problem problem,User user){
        id=problem.getId();
        userDTO=new UserDTO(user);
        title=problem.getTitle();
        description=problem.getDescription();
        tag=toTag(problem.getTag());
        level=problem.getLevel();
    }
    public static Long changTag(List<String> tag){
        Long sum=0L;
        for (String s : tag) {
            int i = list.indexOf(s);
            if(i!=-1) sum=sum|(1<<i);
        }
        return sum;
    }
    public static List<String> toTag(Long tag) {
        List<String> strings=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if((tag&(1<<i))>0) strings.add(list.get(i));
        }
        return strings;
    }
    public static void addTag(String tag){
        list.add(tag);
    }
}
