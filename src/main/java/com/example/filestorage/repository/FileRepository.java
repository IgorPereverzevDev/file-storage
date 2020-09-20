package com.example.filestorage.repository;

import com.example.filestorage.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface FileRepository extends PagingAndSortingRepository<Record, Long> {

    @Query(value = "from Record r where r.updatedTimeStamp BETWEEN ?1 AND ?2")
    Page<Record> findAllRecordsByUpdateTimeStampBetween(@Param("timeFrom") Timestamp timeFrom,
                                                        @Param("timeTo") Timestamp timeTo, Pageable pageable);

    @Modifying
    @Query("delete from Record r where r.key = ?1")
    void deleteById(Long id);
}
