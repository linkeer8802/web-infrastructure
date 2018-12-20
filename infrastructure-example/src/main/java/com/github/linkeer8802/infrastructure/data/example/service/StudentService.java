package com.github.linkeer8802.infrastructure.data.example.service;

import com.github.linkeer8802.data.entity.Page;
import com.github.linkeer8802.data.entity.PageRequest;
import com.github.linkeer8802.data.entity.Sort;
import com.github.linkeer8802.infrastructure.data.example.domain.Student;

import java.util.List;

/**
 * @author: weird
 * @date: 2018/12/4
 */
public interface StudentService {
    List<Student>  getStudents();

    Student save(Student student);

    void delete(String id);

    void deleteAll();

    List<Student> saveAll(List<Student> students);

    Student findById(String id);

    Student findByEmail(String email);

    Student findByMobile(String mobile);

    Boolean existsById(String id);

    long count();

    void delete(Student student);

    void deleteAll(List<Student> students);

    List<Student> findAllWithSort(Sort age);

    Page<Student> findAllWithPage(PageRequest pageRequest);
}
