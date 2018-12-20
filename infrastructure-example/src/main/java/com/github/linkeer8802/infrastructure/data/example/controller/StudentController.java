package com.github.linkeer8802.infrastructure.data.example.controller;

import com.github.linkeer8802.data.entity.PageRequest;
import com.github.linkeer8802.data.entity.Sort;
import com.github.linkeer8802.infrastructure.data.example.domain.Student;
import com.github.linkeer8802.infrastructure.data.example.service.StudentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: weird
 * @date: 2018/12/4
 */
@RestController
@RequestMapping("/studnets")
public class StudentController {

    @Resource
    private StudentService studentService;

    @GetMapping()
    public Object list() {
        List<Student> students = studentService.getStudents();
        return students;
    }

    @GetMapping("/save")
    public Object save() {
        Student student = new Student(UUID.randomUUID().toString(), "赵六", 25);
        student = studentService.save(student);
        return student;
    }

    @GetMapping("/saveAll")
    public Object saveAll() {
        List<Student> students = new ArrayList<>();

        students.add(new Student(UUID.randomUUID().toString(), "张三", 22));
        students.add(new Student(UUID.randomUUID().toString(), "李四", 23));
        students.add(new Student(UUID.randomUUID().toString(), "王五", 24));
        students = studentService.saveAll(students);

        return students;
    }

    @GetMapping("/{id}/findById")
    public Object findById(@PathVariable String id) {

        return studentService.findById(id);
    }

    @GetMapping("/findByEmail")
    public Object findByEmail(@RequestParam String email) {

        return studentService.findByEmail(email);
    }

    @GetMapping("/findByMobile")
    public Object findByMobile(@RequestParam String mobile) {

        return studentService.findByMobile(mobile);
    }

    @GetMapping("/{id}/existsById")
    public Object existsById(@PathVariable String id) {
        return studentService.existsById(id);
    }

    @GetMapping("/count")
    public Object count() {
        return studentService.count();
    }

    @GetMapping("/{id}/delete")
    public Object delete(@PathVariable String id) {
        studentService.delete(id);
        return id;
    }

    @GetMapping("/delete")
    public void deleteStudent() {
        Student student = new Student("42a5c1af-bb05-414c-9f5c-b383b59ecffc", "张三", 22);
        studentService.delete(student);
    }

    @GetMapping("/delete/all")
    public void deleteStudents() {
        List<Student> students = new ArrayList<>();

        students.add(new Student(UUID.randomUUID().toString(), "张三", 22));
        students.add(new Student("9653cfe7-e5e4-417e-991e-4f7cf5ae4609", "李四", 23));
        students.add(new Student(UUID.randomUUID().toString(), "王五", 24));
        studentService.deleteAll(students);
    }

    @GetMapping("/deleteAll")
    public void deleteAll() {
        studentService.deleteAll();
    }

    @GetMapping("/findAllWithSort")
    public Object findAllWithSort() {
        return studentService.findAllWithSort(Sort.by(Sort.Direction.DESC, "age"));
    }

    @GetMapping("/findAllWithPage")
    public Object findAllWithPage(int page, int size) {

        return studentService.findAllWithPage(new PageRequest(page, size));
    }
}
