package me.iskrone.w2r4s.repository;

import me.iskrone.w2r4s.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Page<Book> findAll(Specification<Book> bookSpec, Pageable pageable, Sort sort);
    
    List<Book> findAll(Specification<Book> bookSpec, Sort sort);

    List<Book> findAll(Specification<Book> bookSpec);
}