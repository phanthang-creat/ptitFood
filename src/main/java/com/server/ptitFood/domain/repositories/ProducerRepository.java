package com.server.ptitFood.domain.repositories;

import com.server.ptitFood.domain.entities.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
//    @Procedure(procedureName = "insert_producer")
//    void insertProducer(
//            @Param("name_in") String name,
//            @Param("code_in") String code,
//            @Param("keywork_in") String keywork,
//            @Param("created_by_in") Integer created_by,
//            @Param("updated_by_in") Integer updated_by,
//            @Param("status_in") Integer status,
//            @Param("trash_in") Integer trash
//    );
//
//    @Procedure(procedureName = "select_producer_decryption")
//    List<Producer> selectProducerDecryption();
//
//    @Procedure(procedureName = "select_producer_decryption_by_id")
//    Producer selectProducerDecryptionById(@Param("id_in") Integer id);

//    @Procedure(procedureName = "update_producer_by_id")
//    void updateProducerById(
//            @Param("id_in") Integer id,
//            @Param("name_in") String name,
//            @Param("code_in") String code,
//            @Param("keywork_in") String keywork,
//            @Param("updated_by_in") Integer updated_by,
//            @Param("status_in") Integer status
//    );

    void deleteById(Integer id);

//    @Procedure(procedureName = "select_id_name_producer_decryption")
//    List<Producer> selectIdNameProducerDecryptionById();
}
