package me.iskrone.w2r4s.service.specs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import me.iskrone.w2r4s.entity.Book;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by Iskander on 19.01.2020
 */
public class BookSpecification {
    
    public static Specification<Book> withBookNameIn(final String bookName) {
        return (Specification<Book>) (root, criteriaQuery, criteriaBuilder) -> 
                criteriaBuilder.like(root.get("name"), "%" + bookName + "%");
    }

    public static Specification<Book> withBookName(final String bookName) {
        return (Specification<Book>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), bookName);
    }

    public static Specification<Book> withAuthorIn(final String authorName) {
        return (Specification<Book>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("author"), "%" + authorName + "%");
    }

    public static Specification<Book> withAuthor(final String authorName) {
        return (Specification<Book>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("author"), authorName);
    }

    public static Specification<Book> withFinishingDate(final String finishingDate) {
        return (Specification<Book>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("finishingDate"), "%" + finishingDate + "%");
    }

    public static Specification<Book> withIsDone(final Boolean isDone) {
        return (Specification<Book>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isDone"), isDone);
    }

    public static Specification<Book> withIsAudio(final Boolean isAudio) {
        return (Specification<Book>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isAudio"), isAudio);
    }

    public static Specification<Book> withHasPaperBook(final Boolean hasPaperBook) {
        return (Specification<Book>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("hasPaperBook"), hasPaperBook);
    }
}
