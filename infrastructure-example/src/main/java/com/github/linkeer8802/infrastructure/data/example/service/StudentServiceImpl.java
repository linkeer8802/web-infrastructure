package com.github.linkeer8802.infrastructure.data.example.service;

import com.github.linkeer8802.data.datasource.DataSource;
import com.github.linkeer8802.data.datasource.DataSourceType;
import com.github.linkeer8802.data.entity.Page;
import com.github.linkeer8802.data.entity.PageRequest;
import com.github.linkeer8802.data.entity.Sort;
import com.github.linkeer8802.infrastructure.data.example.domain.Student;
import com.github.linkeer8802.infrastructure.data.example.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: weird
 * @date: 2018/12/4
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentRepository studentRepository;

    @Override
    @DataSource(DataSourceType.SLAVE)
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public void delete(String id) {
        studentRepository.deleteById(id);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public void deleteAll() {
        studentRepository.deleteAll();
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public List<Student> saveAll(List<Student> students) {
        return studentRepository.saveAll(students);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public Student findById(String id) {
        return studentRepository.findById(id);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public Student findByMobile(String mobile) {
        return studentRepository.findByMobile(mobile);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public Boolean existsById(String id) {
        return studentRepository.existsById(id);
    }

    @Override
    public long count() {
        return studentRepository.count();
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public void delete(Student student) {
        studentRepository.delete(student);
    }

    @Override
    @DataSource(DataSourceType.MASTER)
    public void deleteAll(List<Student> students) {
        studentRepository.deleteAll(students);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public List<Student> findAllWithSort(Sort sort) {
        return studentRepository.findAll(sort);
    }

    @Override
    @DataSource(DataSourceType.SLAVE)
    public Page<Student> findAllWithPage(PageRequest pageRequest) {
        return studentRepository.findAll(pageRequest);
    }
}
