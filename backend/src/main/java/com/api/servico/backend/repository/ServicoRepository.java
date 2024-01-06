package com.api.servico.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.servico.backend.entity.Servico;
import org.springframework.data.repository.query.Param;

public interface ServicoRepository extends  JpaRepository<Servico, Long> {
    
    @Query(value = """
            SELECT s 
              FROM Servico s 
             WHERE s.valorPago IS NULL
             	OR s.valorPago = 0
               AND s.status <> 'cancelado'
            """)
    Page<Servico> buscarServicosPagamentoPendente(Pageable pageable);

    @Query(value = """
            SELECT s 
              FROM Servico s 
             WHERE s.status = 'cancelado'
            """)
    Page<Servico> buscarServicosCancelados(Pageable pageable);

    @Query(value = """
            SELECT s 
              FROM Servico s 
             WHERE s.status = 'realizado'
            """)
    Page<Servico> buscarServicosRealizados(Pageable pageable);

    @Query(value = """
            SELECT s 
              FROM Servico s 
             WHERE s.dataPagamento 
           BETWEEN :startDate 
               AND :endDate
            """)
    List<Servico> buscarServicosPeriodoDataPagamento(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = """
            SELECT s 
              FROM Servico s 
             WHERE s.dataInicio 
           BETWEEN :startDate 
               AND :endDate
            """)
    List<Servico> buscarServicosPeriodoDataInicio(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = """
            SELECT s 
              FROM Servico s 
             WHERE s.dataTermino 
           BETWEEN :startDate 
               AND :endDate
            """)
    List<Servico> buscarServicosPeriodoDataTermino(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}