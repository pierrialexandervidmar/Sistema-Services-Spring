import '../App.css';
import React, { useEffect, useState } from 'react';
import axios from 'axios'
import CsvDownloadButton from 'react-json-to-csv'
import ReactPaginate from 'react-paginate'
import { toast, ToastContainer } from 'react-toastify';

function Servico() {
    const baseURL = 'http://localhost:8080/api/servicos'

    const [totalPaginas, setTotalPaginas] = useState();
    const [itemsPage, setItemsPage] = useState([]);
    const [paginaAtual, setPaginaAtual] = useState();

    const [servicos, setServicos] = useState([]);
    const [atualizar, setAtualizar] = useState();

    const [tipoPagina, setTipoPagina] = useState('')

    const handlePageClick = (data) => {
        if (tipoPagina === 'Pendentes') {
            handleServicosPendentes(data.selected);
        } else if (tipoPagina === 'Realizados') {
            handleServicosRealizados(data.selected);
        } else if (tipoPagina === 'Cancelados') {
            handleServicosCancelados(data.selected);
        } else if (tipoPagina === 'Todos') {
            handleListarTodos(data.selected);
        }0
    }

    const handleForcePageZero = () => {
        setPaginaAtual(0);
    }

    const [servico, setServico] = useState({
        nomeCliente: '',
        dataInicio: '',
        dataTermino: '',
        descricaoServico: '',
        valorServico: '',
        valorPago: '',
        dataPagamento: ''
    })

    const [datas, setDatas] = useState([{
        startDate: '',
        endDate: '',
        dateOf: ''
    }])

    const handleChange = (event) => {
        setServico({ ...servico, [event.target.name]: event.target.value })
    }

    const handleChangeDateConsult = (event) => {
        setDatas({ ...datas, [event.target.name]: event.target.value })
    }

    /**
     * Manipula o envio do formulário, evitando a recarga da página,
     * e realiza uma solicitação HTTP para atualizar ou inserir um serviço
     * com base nas propriedades do objeto 'servico'. Após a conclusão da
     * solicitação, redefine o estado 'atualizar' e limpa o formulário.
                            * @param {Event} event - Objeto de evento associado ao envio do formulário.
     */
    const handleSubmit = (event) => {
        event.preventDefault()

        if (!servico.nomeCliente || !servico.dataInicio || !servico.valorServico || servico.descricaoServico) {
            toast.error("Os campos Nome, Data de início, Valor do Serviço e Descrição não podem ser vazios.");
            return;
        }

        if (servico.id) {
            console.log("Atualizando")
            axios.put(baseURL, servico)
                .then(result => {
                    setAtualizar(result)
                })
            setServico({
                nomeCliente: '',
                dataInicio: '',
                dataTermino: '',
                descricaoServico: '',
                valorServico: '',
                valorPago: '',
                dataPagamento: ''
            })
        } else {
            console.log("Inserindo")
            axios.post(baseURL, servico)
                .then(result => {
                    setAtualizar(result)
                })
            setServico({
                nomeCliente: '',
                dataInicio: '',
                dataTermino: '',
                descricaoServico: '',
                valorServico: '',
                valorPago: '',
                dataPagamento: ''
            })
        }
    }

    useEffect((paginaAtual) => {
        axios.get(baseURL + "?page=" + paginaAtual + "&size=15") // ?page=0&size=15
            .then(result => {
                setServicos(result.data.content);
                setTotalPaginas(result.data.totalPages)
                setTipoPagina('Todos')
            })
            .catch(error => {
                console.error('Erro ao obter dados:', error);
            });
    }, [atualizar]);


    /**
     * Responsável por formatar a moeda.
     * @param {*} valor 
     * @returns 
     */
    function formatarMoeda(valor) {
        return parseFloat(valor).toLocaleString('pt-BR', {
            style: 'currency',
            currency: 'BRL',
        });
    }

    /**
     * Resposavel por formatar a data para BR.
     * @param {*} data 
     * @returns 
     */
    function formatarData(data) {
        const dataUtc = new Date(data + 'T00:00:00Z'); // Adiciona o UTC para garantir consistência
        return dataUtc.toLocaleDateString('pt-BR', { timeZone: 'UTC' });
    }

    const handleAlterar = (srv) => {
        setServico(srv);
    };

    const handleCancelar = (srv) => {
        axios.post(baseURL + "/cancelarservico/" + srv.id)
            .then(result => {
                setAtualizar(result)
            })
    };

    const handleExcluir = (srv) => {
        axios.delete(baseURL + "/" + srv.id)
            .then(result => {
                setAtualizar(result)
            })
    };

    const handleReativar = (srv) => {
        axios.post(baseURL + "/reativarservico/" + srv.id)
            .then(result => {
                setAtualizar(result)
            })
    };

    const limpar = () => {
        setServico({
            nomeCliente: '',
            dataInicio: '',
            dataTermino: '',
            descricaoServico: '',
            valorServico: '',
            valorPago: '',
            dataPagamento: ''
        })
    }

    const handleServicosPendentes = (paginaAtual) => {
        axios.get(baseURL + "/pagamentopendente" + "?page=" + paginaAtual + "&size=15") // ?size=5&page=2
            .then(result => {
                setServicos(result.data.content);
                setTotalPaginas(result.data.totalPages)
                setTipoPagina('Pendentes')
            })
            .catch(error => {
                console.error('Erro ao obter dados:', error);
            });
    }

    const handleServicosRealizados = (paginaAtual) => {
        axios.get(baseURL + "/realizados" + "?page=" + paginaAtual + "&size=15") // ?size=5&page=2
            .then(result => {
                setServicos(result.data.content);
                setTotalPaginas(result.data.totalPages)
                setTipoPagina('Realizados')
            })
            .catch(error => {
                console.error('Erro ao obter dados:', error);
            });
    }

    const handleServicosCancelados = (paginaAtual) => {
        axios.get(baseURL + "/cancelados" + "?page=" + paginaAtual + "&size=15") // ?size=5&page=2
            .then(result => {
                setServicos(result.data.content);
                setTotalPaginas(result.data.totalPages)
                setTipoPagina('Cancelados')
            })
            .catch(error => {
                console.error('Erro ao obter dados:', error);
            });
    }

    const handleListarTodos = (paginaAtual) => {
        axios.get(baseURL + "?page=" + paginaAtual + "&size=15") // ?size=5&page=2
            .then(result => {
                setServicos(result.data.content);
                setTotalPaginas(result.data.totalPages)
                setTipoPagina('Todos')
            })
            .catch(error => {
                console.error('Erro ao obter dados:', error);
            });
    }

    const hanbleConsultaPagosData = (event) => {
        event.preventDefault()

        if (datas.dateOf == 'dataInicio') {
            axios.get(baseURL + "/iniciopordata?startDate=" + datas.startDate + "&endDate=" + datas.endDate)
                .then(result => {
                    setServicos(result.data);
                })
                .catch(error => {
                    console.error('Erro ao obter dados:', error);
                });
        }
        else if (datas.dateOf == 'dataTermino') {
            axios.get(baseURL + "/terminopordata?startDate=" + datas.startDate + "&endDate=" + datas.endDate)
                .then(result => {
                    setServicos(result.data);
                })
                .catch(error => {
                    console.error('Erro ao obter dados:', error);
                });
        }
        else if (datas.dateOf == 'dataPagamento') {
            axios.get(baseURL + "/pagospordata?startDate=" + datas.startDate + "&endDate=" + datas.endDate)
                .then(result => {
                    setServicos(result.data);
                })
                .catch(error => {
                    console.error('Erro ao obter dados:', error);
                });
        }
        else {
            handleListarTodos();
        }

        setDatas({
            startDate: '',
            endDate: '',
            dateOf: ''
        })
    }

    // ============================================================================================

    return (
        <div className="container">
            <h1 className='titulo'>Cadastro de Serviços</h1>
            <br />
            <form onSubmit={handleSubmit}>
                <div className='col-12'>

                    <div class="row mb-3">
                        <label htmlFor="nome" className='col-sm-2 col-form-label'>Nome do Cliente</label>
                        <div class="col-sm-10">
                            <input onChange={handleChange} value={servico.nomeCliente} name='nomeCliente' type="text" className='form-control input-style' id='nome' />
                        </div>
                    </div>


                    <div class="row mb-3">
                        <label htmlFor="datainicio" className='col-sm-2 col-form-label'>Data de Início</label>
                        <div class="col-sm-4">
                            <input onChange={handleChange} value={servico.dataInicio} name='dataInicio' type="date" className='form-control input-style' id='datainicio' />
                        </div>

                        <label htmlFor="datatermino" className='col-sm-1 col-form-label'>Término</label>
                        <div class="col-sm-5">
                            <input onChange={handleChange} value={servico.dataTermino} name='dataTermino' type="date" className='form-control input-style' id='datatermino' />
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label htmlFor="descricao" className='col-sm-2 col-form-label'>Descrição do Serviço</label>
                        <div class="col-sm-10">
                            <input onChange={handleChange} value={servico.descricaoServico || ''} name='descricaoServico' type="text" className='form-control input-style' id='descricao' />
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label htmlFor="valorservico" className='col-sm-2 col-form-label'>Valor do Serviço</label>
                        <div class="col-sm-4">
                            <input onChange={handleChange} value={servico.valorServico || ''} name="valorServico" type="text" className='form-control input-style' id='valorservico' />
                        </div>

                        <label htmlFor="valorpago" className='col-sm-1 col-form-label'>Vlr Pago</label>
                        <div class="col-sm-5">
                            <input onChange={handleChange} value={servico.valorPago || ''} name="valorPago" type="text" className='form-control input-style' id='valorpago' />
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label htmlFor="datapagamento" className='col-sm-2 col-form-label'>Data de Pagamento</label>
                        <div class="col-sm-4 mb-3">
                            <input onChange={handleChange} value={servico.dataPagamento || ''} name='dataPagamento' type="date" className='form-control input-style' id='datapagamento' />
                        </div>
                        <div className="col-sm-6 text-center"> {/* Utilize ml-auto para a margem à esquerda automática */}
                            <input type="submit" className='btn btn-primary col-sm-6' value="Gravar" /> {/* Utilize col-sm-12 para ocupar toda a largura da coluna */}
                        </div>
                    </div>
                </div>
            </form>


            <div className="col-12 mt-3">
                <button onClick={() => limpar()} className='btn btn-secondary m-2 rounded-0 p-2'>Limpar</button>
                <button onClick={() => handleServicosPendentesRecarregar()} className='btn btn-secondary m-2 rounded-0 p-2'>Pendentes de Pagamento</button>
                <button onClick={() => handleServicosRealizados()} className='btn btn-secondary m-2 rounded-0 p-2'>Realizados</button>
                <button onClick={() => handleServicosCancelados()} className='btn btn-secondary m-2 rounded-0 p-2'>Cancelados</button>
                <button onClick={() => handleListarTodos()} className='btn btn-secondary m-2 rounded-0 p-2'>Listar Todos</button>

            </div>
            <hr />

            {/* Formulário de consulta por data */}
            <form onSubmit={hanbleConsultaPagosData}>
                <div className="row">
                    <div className="col-sm-3">
                        <select
                            className="form-select input-style"
                            aria-label=".form-select-sm example"
                            onChange={handleChangeDateConsult}
                            value={datas.dateOf}
                            name="dateOf"
                        >
                            <option value="">Consultar datas por</option>
                            <option value="dataInicio">Data de Início</option>
                            <option value="dataTermino">Data de Término</option>
                            <option value="dataPagamento">Data de Pagamento</option>
                        </select>
                    </div>
                    <div className="col-sm-3">
                        <input onChange={handleChangeDateConsult} value={datas.startDate || ''} name='startDate' type="date" className='form-control input-style' id='datapagamento' />
                    </div>
                    <div className="col-sm-3">
                        <input onChange={handleChangeDateConsult} value={datas.endDate || ''} name='endDate' type="date" className='form-control input-style' id='datapagamento' />
                    </div>
                    <div className="col-sm-2">
                        <input type="submit" className="btn btn-primary" value="Buscar" />
                    </div>
                </div>
            </form>


            <hr />
            <table class="table table-dark">
                <thead>
                    <tr>
                        <th scope="col">Nome</th>
                        <th scope="col">Descrição</th>
                        <th scope="col">Valor</th>
                        <th scope="col">Data de Início</th>
                        <th scope="col">Status</th>
                        <th scope="col">Ações &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Baixar CSV:&nbsp;&nbsp;&nbsp;
                            <CsvDownloadButton data={servicos} filename="Dados" className='btn btn-secondary rounded-0 botao-csv' />
                        </th>
                    </tr>
                </thead>
                <tbody>
                    {
                        servicos.map(srv => (
                            <tr key={srv.id}> {/* Adicione uma chave única para cada elemento */}
                                <td>{srv.nomeCliente}</td>
                                <td>{srv.descricaoServico}</td>
                                <td>{formatarMoeda(srv.valorServico)}</td>
                                <td>{formatarData(srv.dataInicio)}</td>
                                <td>{srv.status}</td>
                                <td>
                                    <button onClick={() => handleAlterar(srv)} className='btn tamanho-botao botao-alterar' disabled={srv.status === 'cancelado' || srv.status === 'realizado'}>Alterar</button>&nbsp;&nbsp;
                                    <button onClick={() => handleExcluir(srv)} className='btn tamanho-botao botao-excluir' disabled={srv.status === 'realizado'}>Excluir</button>&nbsp;&nbsp;
                                    <button onClick={() => handleCancelar(srv)} className='btn tamanho-botao botao-cancelar' disabled={srv.status === 'cancelado' || srv.status === 'realizado'}>Cancelar</button>&nbsp;&nbsp;
                                    <button onClick={() => handleReativar(srv)} className='btn tamanho-botao botao-reativar' disabled={srv.status === 'pendente'}>Reativar</button>&nbsp;&nbsp;
                                </td>
                            </tr>
                        ))
                    }
                </tbody>
            </table>

            <ReactPaginate
                previousLabel={'< Voltar'}
                nextLabel={'Avançar >'}
                breakLabel={'...'}
                pageCount={totalPaginas}
                marginPagesDisplayed={2}
                pageRangeDisplayed={3}
                onPageChange={handlePageClick}
                containerClassName={'pagination justify-content-center'}
                pageClassName={'page-item'}
                pageLinkClassName={'page-link'}
                previousClassName={'page-item'}
                previousLinkClassName={'page-link'}
                nextClassName={'page-item'}
                nextLinkClassName={'page-link'}
                breakClassName={'page-item'}
                breakLinkClassName={'page-link'}
                activeClassName={'active'}
            />

        </div>
    );
}

export default Servico;