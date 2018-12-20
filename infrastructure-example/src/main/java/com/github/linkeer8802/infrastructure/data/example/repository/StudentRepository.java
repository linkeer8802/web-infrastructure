package com.github.linkeer8802.infrastructure.data.example.repository;

import com.github.linkeer8802.data.repository.PagingAndSortingRepository;
import com.github.linkeer8802.infrastructure.data.example.domain.Student;

/**
 * @author: weird
 * @date: 2018/12/4
 */
public interface StudentRepository extends PagingAndSortingRepository<Student, String> {

    Student findByEmail(String email);

    Student findByMobile(String mobile);
}
