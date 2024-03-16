package ua.edu.sumdu.nefodov.wisdomvibes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.edu.sumdu.nefodov.wisdomvibes.model.Quote;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    List<Quote> findByAuthor(String author);

    @Query("SELECT DISTINCT q.author FROM Quote q")
    List<String> findDistinctAuthors();
}
