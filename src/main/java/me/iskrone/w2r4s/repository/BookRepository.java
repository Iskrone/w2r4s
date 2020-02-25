package me.iskrone.w2r4s.repository;

import me.iskrone.w2r4s.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Page<Book> findAll(Specification<Book> bookSpec, Pageable pageable);
    Page<Book> findAll(Pageable pageable);
}