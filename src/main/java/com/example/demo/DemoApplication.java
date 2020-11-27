package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

@RestController
@RequestMapping("api/v1/students")
class Controller {

    public static final String FAILED = "failed";
    List<Student> students;

    Controller() {
        this.students = Arrays.asList(new Student("001"), new Student("002"));
    }

    // 使用Get方式请求 http://localhost:8080/api/v1/students
    @GetMapping
    public List<Student> getAllStudents() {
        return this.students;
    }

    // 使用Delete方式请求 http://localhost:8080/api/v1/students/?name=001&username=admin&password=123456
    @DeleteMapping
    public Map<String, String> deleteStudent(@RequestParam("name") String name,
                                             @RequestParam("username") String username,
                                             @RequestParam("password") String password) {
        Map<String, String> result = new HashMap<>();
        String status = "";
        String msg = "";
        if (!"admin".equals(username) || !"123456".equals(password)) {
            status = FAILED;
            msg = "没有权限操作！";
        } else {
            try {
                AtomicBoolean b = new AtomicBoolean(false);
                this.students.forEach(student -> {
                    if (student.getName().equals(name)) {
                        b.set(true);
                    }
                });

                if (b.get()) {
                    status = "success";
                    msg = "删除成功！";
                } else {
                    status = FAILED;
                    msg = "删除失败，" + name + "不存在。";
                }
            } catch (Exception e) {
                e.printStackTrace();
                status = FAILED;
                msg = "服务异常！";
            }

        }
        result.put("status", status);
        result.put("message", msg);
        return result;
    }

    @PostMapping(produces = "application/json")
    public String getAllStudents2() {

        return "{\"method\":\"post\"}";
    }

}

class Student {
    private String name;

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}