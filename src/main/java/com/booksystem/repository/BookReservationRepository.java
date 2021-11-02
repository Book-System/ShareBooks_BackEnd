package com.booksystem.repository;

import java.util.List;

import com.booksystem.entity.BookReservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {
    List<BookReservation> findByMemberId(String memberId);
}
