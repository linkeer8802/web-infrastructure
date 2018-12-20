package com.github.linkeer8802.infrastructure.data.example.repository;

import com.github.linkeer8802.data.cache.annotation.*;
import com.github.linkeer8802.data.entity.Page;
import com.github.linkeer8802.data.entity.Pageable;
import com.github.linkeer8802.data.entity.Sort;
import com.github.linkeer8802.data.repository.PagingAndSortingRepository;
import com.github.linkeer8802.infrastructure.data.example.domain.Student;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author: weird
 * @date: 2018/12/14
 */
@Repository
@CacheConfig("student")
public class StudentRepositoryImpl implements StudentRepository, PagingAndSortingRepository<Student, String> {

    @Override
    @CachePut
    public <S extends Student> S save(S entity) {
        return entity;
    }

    @Override
    @CachePut
    public <S extends Student, U extends Collection<S>> U saveAll(U entities) {
        return entities;
    }

    @Override
    @Cacheable(firstArgValueAsKey = true)
    public Student findById(String id) {
        return null;
    }

    @Override
    @Cacheable(firstArgValueAsKey = true)
    public Student findByEmail(String email) {
        return null;
    }

    @Override
    @Cacheable(firstArgValueAsKey = true)
    public Student findByMobile(String mobile) {
        return null;
    }

    @Override
    @Cacheable(cacheType = CacheType.VALUE)
    public boolean existsById(String id) {
        return false;
    }

    @Override
    @Cacheable(key = "all")
    public Collection<Student> findAll() {
        return null;
    }

    @Override
    @Cacheable
    public <R extends Collection<Student>, U extends Collection<String>> R findAllById(U ids) {
        return null;
    }

    @Override
    @Cacheable(cacheType = CacheType.VALUE)
    public long count() {
        return 0;
    }

    @Override
    @CacheEvict
    public void deleteById(String id) {
        System.out.println(id);
    }

    @Override
    @CacheEvict
    public void delete(Student entity) {
        System.out.println(entity);
    }

    @Override
    @CacheEvict
    public void deleteAll(Collection<? extends Student> entities) {

    }

    @Override
    @CacheEvictAll
    public void deleteAll() {
        System.out.println("deleteAll");
    }

    @Override
    public List<Student> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Student> findAll(Pageable pageable) {
        return null;
    }
}
