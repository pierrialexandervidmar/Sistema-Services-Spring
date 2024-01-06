package com.api.servico.backend.controller;


import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.servico.backend.entity.Servico;
import com.api.servico.backend.service.ServicoService;

/**
 * Controlador para operações relacionadas à entidade Servico.
 */
@RestController
@RequestMapping(value="/api/servicos")
public class ServicoController {
    
    @Autowired
    private ServicoService servicoService;
    
    /**
     * Recupera todos os serviços.
     *
     * @return ResponseEntity contendo a lista de todos os serviços.
     */
    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Page<Servico>> findAll(Pageable pageable) {
        Page<Servico> list = servicoService.buscarTodos(pageable);
        return ResponseEntity.ok().body(list);
    }

    /**
     * Recupera um serviço pelo ID.
     *
     * @param id ID do serviço a ser buscado.
     * @return ResponseEntity contendo o serviço correspondente ao ID fornecido.
     */
    @GetMapping(value = "/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Servico> findById(@PathVariable Long id) {
        Servico obj = servicoService.buscarPorId(id);
        return ResponseEntity.ok().body(obj);
    }

    /**
     * Retorna uma lista de serviços dentro de um determinado período de datas de pagamento.
     * Este endpoint permite a consulta de serviços com base em suas datas de pagamento.
     *
     * @param startDate Data de início do período desejado. Deve ser formatada no padrão ISO DATE (AAAA-MM-DD).
     * @param endDate Data de término do período desejado. Deve ser formatada no padrão ISO DATE (AAAA-MM-DD).
     * @return Uma lista de serviços realizados durante o período de datas de pagamento fornecido.
     * @throws IllegalArgumentException Se as datas fornecidas não estiverem no formato correto ou se startDate for posterior a endDate.
     */
    @GetMapping(value = "/pagospordata")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<Servico> buscarServicosPeriodoDataPagamento(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return servicoService.buscarServicosPeriodoDataPagamento(startDate, endDate);
    }

    /**
     * Retorna uma lista de serviços dentro do período especificado pela data de início e data de término.
     *
     * @param startDate Data de início do período no formato ISO_DATE (AAAA-MM-DD).
     * @param endDate   Data de término do período no formato ISO_DATE (AAAA-MM-DD).
     * @return Uma lista de objetos Servico que estão dentro do período especificado.
     * @throws IllegalArgumentException Se as datas de início e término não estiverem no formato esperado.
     */
    @GetMapping(value = "/iniciopordata")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<Servico> buscarServicosPeriodoDataInicio(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return servicoService.buscarServicosPeriodoDataInicio(startDate, endDate);
    }

    /**
     * Retorna uma lista de serviços dentro do período especificado pela data de início e data de término.
     *
     * @param startDate Data de início do período no formato ISO_DATE (AAAA-MM-DD).
     * @param endDate   Data de término do período no formato ISO_DATE (AAAA-MM-DD).
     * @return Uma lista de objetos Servico que estão dentro do período especificado.
     * @throws IllegalArgumentException Se as datas de início e término não estiverem no formato esperado.
     */
    @GetMapping(value = "/terminopordata")
    @CrossOrigin(origins = "http://localhost:3000")
    public List<Servico> buscarServicosPeriodoDataTermino(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return servicoService.buscarServicosPeriodoDataTermino(startDate, endDate);
    }



    /**
     * Recupera serviços com pagamento pendente.
     *
     * @return ResponseEntity contendo a lista de serviços com pagamento pendente.
     */
    @GetMapping(value = "/pagamentopendente")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Page<Servico>> buscarServicosPagamentoPendente(Pageable pageable) {
        Page<Servico> list = servicoService.buscarServicosPagamentoPendente(pageable);
        return ResponseEntity.ok().body(list);
    }

    /**
     * Recupera serviços cancelados.
     *
     * @return ResponseEntity contendo a lista de serviços cancelados.
     */
    @GetMapping(value = "/cancelados")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Page<Servico>> buscarServicosCancelados(Pageable pageable) {
    	Page<Servico> list = servicoService.buscarServicosCancelados(pageable);
        return ResponseEntity.ok().body(list);
    }

    /**
     * Recupera serviços cancelados.
     *
     * @return ResponseEntity contendo a lista de serviços realizados.
     */
    @GetMapping(value = "/realizados")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Page<Servico>> buscarServicosRealizados(Pageable pageable) {
        Page<Servico> list = servicoService.buscarServicosRealizados(pageable);
        return ResponseEntity.ok().body(list);
    }



    /**
     * Insere um novo serviço.
     *
     * @param obj Serviço a ser inserido.
     * @return ResponseEntity contendo o serviço inserido e a URI para acessar o recurso.
     */
    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Servico> insert(@RequestBody Servico obj) {
        obj = servicoService.inserir(obj);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    /**
     * Atualiza um serviço existente.
     *
     * @param obj Serviço com as alterações a serem aplicadas.
     * @return O serviço atualizado.
     */
    @PutMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public Servico update(@RequestBody Servico obj) {
        return servicoService.alterar(obj);
    }

    /**
     * Exclui um serviço pelo ID.
     *
     * @param id ID do serviço a ser excluído.
     * @return ResponseEntity indicando o sucesso da operação.
     */
    @DeleteMapping(value = "/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        servicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cancela um serviço pelo ID.
     *
     * @param id ID do serviço a ser cancelado.
     * @return ResponseEntity indicando o sucesso da operação.
     */
    @PostMapping(value = "/cancelarservico/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Void> cancelarServico(@PathVariable Long id) {
        servicoService.cancelarServico(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Cancela um serviço pelo ID.
     *
     * @param id ID do serviço a ser cancelado.
     * @return ResponseEntity indicando o sucesso da operação.
     */
    @PostMapping(value = "/reativarservico/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Void> reativarServico(@PathVariable Long id) {
        servicoService.reativarServico(id);
        return ResponseEntity.ok().build();
    }
}
