package com.api.servico.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.servico.backend.entity.Servico;
import com.api.servico.backend.repository.ServicoRepository;
import com.api.servico.backend.service.exceptions.DatabaseException;
import com.api.servico.backend.service.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Serviço para operações relacionadas à entidade Servico.
 */
@Service
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    /**
     * Busca todos os serviços.
     *
     * @return Lista de todos os serviços.
     */

    public Page<Servico> buscarTodos(Pageable pageable) {
        return servicoRepository.findAll(pageable);
    }

    /**
     * Busca um serviço pelo ID.
     *
     * @param id ID do serviço a ser buscado.
     * @return Serviço correspondente ao ID fornecido.
     * @throws ResourceNotFoundException Se o serviço não for encontrado.
     */
    public Servico buscarPorId(Long id) {
        Optional<Servico> obj = servicoRepository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    /**
     * Retorna uma lista de serviços dentro do período especificado pela data de pagamento.
     *
     * @param startDate Data de início do período no formato ISO_DATE (AAAA-MM-DD).
     * @param endDate   Data de término do período no formato ISO_DATE (AAAA-MM-DD).
     * @return Uma lista de objetos Servico que estão dentro do período especificado.
     * @throws IllegalArgumentException Se as datas de início e término não estiverem no formato esperado.
     */
    public List<Servico> buscarServicosPeriodoDataPagamento(LocalDate startDate, LocalDate endDate) {
        return servicoRepository.buscarServicosPeriodoDataPagamento(startDate, endDate);
    }

    /**
     * Retorna uma lista de serviços dentro do período especificado pela data de início.
     *
     * @param startDate Data de início do período no formato ISO_DATE (AAAA-MM-DD).
     * @param endDate   Data de término do período no formato ISO_DATE (AAAA-MM-DD).
     * @return Uma lista de objetos Servico que estão dentro do período especificado.
     * @throws IllegalArgumentException Se as datas de início e término não estiverem no formato esperado.
     */
    public List<Servico> buscarServicosPeriodoDataInicio(LocalDate startDate, LocalDate endDate) {
        return servicoRepository.buscarServicosPeriodoDataInicio(startDate, endDate);
    }

    /**
     * Retorna uma lista de serviços dentro do período especificado pela data de término.
     *
     * @param startDate Data de início do período no formato ISO_DATE (AAAA-MM-DD).
     * @param endDate   Data de término do período no formato ISO_DATE (AAAA-MM-DD).
     * @return Uma lista de objetos Servico que estão dentro do período especificado.
     * @throws IllegalArgumentException Se as datas de início e término não estiverem no formato esperado.
     */
    public List<Servico> buscarServicosPeriodoDataTermino(LocalDate startDate, LocalDate endDate) {
        return servicoRepository.buscarServicosPeriodoDataTermino(startDate, endDate);
    }


    /**
     * Busca serviços com pagamento pendente.
     *
     * @return Lista de serviços com pagamento pendente.
     */
    public Page<Servico> buscarServicosPagamentoPendente(Pageable pageable) {
        return servicoRepository.buscarServicosPagamentoPendente(pageable);
    }

    /**
     * Busca serviços cancelados.
     *
     * @return Lista de serviços cancelados.
     */
    public Page<Servico> buscarServicosCancelados(Pageable pageable) {
        return servicoRepository.buscarServicosCancelados(pageable);
    }

    /**
     * Busca serviços com pagamento pendente.
     *
     * @return Lista de serviços com pagamento pendente.
     */
    public Page<Servico> buscarServicosRealizados(Pageable pageable) {
        return servicoRepository.buscarServicosRealizados(pageable);
    }

    /**
     * Insere um novo serviço.
     *
     * @param servico Serviço a ser inserido.
     * @return Serviço inserido.
     */
    public Servico inserir(Servico servico) {
        if ((servico.getValorPago() == null || servico.getValorPago() == 0) && servico.getDataPagamento() == null) {
            servico.setStatus("pendente");
        } else if(servico.getValorPago() > 0 &&  servico.getDataPagamento() == null){
        	servico.setDataPagamento(LocalDate.now());
            servico.setStatus("realizado");
        }
        return servicoRepository.save(servico);
    }

    /**
     * Altera um serviço existente.
     *
     * @param obj Serviço com as alterações a serem aplicadas.
     * @return Serviço alterado.
     */
    public Servico alterar(Servico obj) {
    	 Servico servicoExistente = servicoRepository.findById(obj.getId()).orElse(null);
    	 if (servicoExistente != null) {
	        if (obj.getValorPago() != null && (servicoExistente.getValorPago() == null || !obj.getValorPago().equals(servicoExistente.getValorPago()))) {
	            servicoExistente.setValorPago(obj.getValorPago());
	        }

	        // Verifica se o valor pago é maior que zero e se o status não é "realizado" antes de atualizar
	        if (servicoExistente.getValorPago() != null && servicoExistente.getValorPago() > 0 && !"realizado".equals(servicoExistente.getStatus()) || servicoExistente.getStatus() == null) {
	            obj.setStatus("realizado");
	            obj.setValorPago(servicoExistente.getValorPago());
	            obj.setDataPagamento(LocalDate.now());
	        }
	        
	        if(obj.getValorPago() == obj.getValorServico()) {
	        	obj.setDataPagamento(LocalDate.now());
	        	obj.setStatus("realizado");	        	
	        }
	        
	        // Atualiza o objeto existente no banco de dados
	        return servicoRepository.save(obj);  
    	 }
         return servicoRepository.save(obj);
    }

    /**
     * Cancela um serviço pelo ID.
     *
     * @param id ID do serviço a ser cancelado.
     */
    public void cancelarServico(Long id) {
    	Servico servico = servicoRepository.findById(id).get(); // Recupero os dados do banco
    	servico.setStatus("cancelado");							// Alteramos o Status
    	servicoRepository.save(servico);						// Atualizamos
    }
    
    /**
     * Cancela um serviço pelo ID.
     *
     * @param id ID do serviço a ser alterado para pendente.
     */
    public void reativarServico(Long id) {
    	Servico servico = servicoRepository.findById(id).get(); // Recupero os dados do banco
    	servico.setStatus("pendente");							// Alteramos o Status
    	servico.setDataPagamento(null);
    	servico.setValorPago(0.0);
    	servicoRepository.save(servico);						// Atualizamos
    }

    /**
     * Exclui um serviço pelo ID.
     *
     * @param id ID do serviço a ser excluído.
     * @throws ResourceNotFoundException Se o serviço não for encontrado.
     * @throws DatabaseException         Se ocorrer um erro de integridade no banco de dados.
     */
    public void excluir(Long id) {
        try {
            servicoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

}
