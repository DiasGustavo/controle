package br.com.gerentedocumento.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.gerentedocumento.dao.AtosDAO;
import br.com.gerentedocumento.dao.ChecklistDAO;
import br.com.gerentedocumento.dao.DocumentoDAO;
import br.com.gerentedocumento.dao.FuncionarioDAO;
import br.com.gerentedocumento.dao.ParecerDAO;
import br.com.gerentedocumento.domain.Atos;
import br.com.gerentedocumento.domain.Checklist;
import br.com.gerentedocumento.domain.Documento;
import br.com.gerentedocumento.domain.Funcionario;
import br.com.gerentedocumento.domain.Parecer;
import br.com.gerentedocumento.util.FacesUtil;
import br.com.gerentedocumento.util.GeraParecer;
import br.com.gerentedocumento.bean.AutenticacaoBean;
import br.com.gerentedocumento.constante.CategoriaAto;

@ManagedBean
@ViewScoped
public class ParecerBean {

	private Parecer parecerCadastro;

	private List<Parecer> listaPareceres;
	private List<Parecer> listaPareceresFiltrados;
	private List<Funcionario> listaFuncionarios;
	private List<Atos> listaAtos;
	private List<Atos> listaAtosFiltrados;
	private List<Documento> listaDocumentos;
	private List<Checklist> listaChecklists;

	private List<CategoriaAto> listaCategorias;

	private String acao;
	private Long codigo;
	private Date dataCriacao;
	
	@ManagedProperty(value = "#{autenticacaoBean}")
	private AutenticacaoBean autenticacaoBean;

	public Parecer getParecerCadastro() {
		if (parecerCadastro == null) {
			parecerCadastro = new Parecer();
			dataCriacao = new Date();
		}
		return parecerCadastro;
	}

	public void setParecerCadastro(Parecer parecerCadastro) {
		this.parecerCadastro = parecerCadastro;
	}

	public List<Parecer> getListaPareceres() {
		return listaPareceres;
	}

	public void setListaPareceres(List<Parecer> listaPareceres) {
		this.listaPareceres = listaPareceres;
	}

	public List<Parecer> getListaPareceresFiltrados() {
		return listaPareceresFiltrados;
	}

	public void setListaPareceresFiltrados(List<Parecer> listaPareceresFiltrados) {
		this.listaPareceresFiltrados = listaPareceresFiltrados;
	}

	public List<Funcionario> getListaFuncionarios() {
		return listaFuncionarios;
	}

	public void setListaFuncionarios(List<Funcionario> listaFuncionarios) {
		this.listaFuncionarios = listaFuncionarios;
	}

	public List<Atos> getListaAtos() {
		if (listaAtos == null) {
			listaAtos = new ArrayList<>();
		}
		return listaAtos;
	}

	public void setListaAtos(List<Atos> listaAtos) {
		this.listaAtos = listaAtos;
	}

	public List<Atos> getListaAtosFiltrados() {
		return listaAtosFiltrados;
	}

	public void setListaAtosFiltrados(List<Atos> listaAtosFiltrados) {
		this.listaAtosFiltrados = listaAtosFiltrados;
	}

	public List<Documento> getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(List<Documento> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}

	public List<Checklist> getListaChecklists() {
		if (listaChecklists == null) {
			listaChecklists = new ArrayList<>();
		}
		return listaChecklists;
	}

	public void setListaChecklists(List<Checklist> listaChecklists) {
		this.listaChecklists = listaChecklists;
	}

	// retorna o arrayList preenchido
	public List<CategoriaAto> getListaCategorias() {
		listaCategorias = Arrays.asList(CategoriaAto.values());
		return listaCategorias;
	}

	public void setListaCategorias(List<CategoriaAto> listaCategorias) {
		this.listaCategorias = listaCategorias;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	public void novo() {
		parecerCadastro = new Parecer();
	}

	public AutenticacaoBean getAutenticacaoBean() {
		return autenticacaoBean;
	}

	public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
		this.autenticacaoBean = autenticacaoBean;
	}

	public void salvar() {
		try {
			ParecerDAO pdao = new ParecerDAO();
			Long codigoParecer = pdao.salvar(parecerCadastro);
			Parecer parecerFK = pdao.buscarPorCodigo(codigoParecer);			
			
			for (Checklist checklist : listaChecklists) {
				checklist.setParecer(parecerFK);

				ChecklistDAO cdao = new ChecklistDAO();
				cdao.salvar(checklist);
			}
			parecerCadastro = new Parecer();
			listaChecklists = new ArrayList<>();
			
			FacesUtil.addMsgInfo("Parecer Cadastro com Sucesso!");
		} catch (RuntimeException ex) {
			FacesUtil.addMsgErro("Ocorreu um erro ao salvar o Parecer" + ex.getMessage());
		}
	}

	public void listar() {
		try {
			ParecerDAO pdao = new ParecerDAO();
			listaPareceres = pdao.listar();
		} catch (RuntimeException ex) {
			FacesUtil.addMsgErro("Ocorreu um erro ao listar os Pareceres " + ex.getMessage());
		}
	}

	public void carregarDados() {

		try {
			if (codigo != null) {
				ParecerDAO pdao = new ParecerDAO();
				parecerCadastro = pdao.buscarPorCodigo(codigo);

				ChecklistDAO cdao = new ChecklistDAO();
				listaChecklists = cdao.buscarPorParecer(codigo);

				AtosDAO adao = new AtosDAO();
				listaAtos = adao.listar();

				DocumentoDAO ddao = new DocumentoDAO();
				listaDocumentos = ddao.listar();

			} else {
				parecerCadastro = new Parecer();
				// dataCriacao = new Date();

				FuncionarioDAO fdao = new FuncionarioDAO();
				listaFuncionarios = fdao.listar();
				Funcionario funTemp = fdao.buscarPorCodigo(autenticacaoBean.getFuncionarioLogado().getId());
				parecerCadastro.setFuncionario(funTemp);

				DocumentoDAO ddao = new DocumentoDAO();
				listaDocumentos = ddao.listar();

				AtosDAO adao = new AtosDAO();
				listaAtos = adao.listar();

			}
		} catch (RuntimeException ex) {
			FacesUtil.addMsgErro("Erro ao carregar os dados do parecer " + ex.getMessage());
		}
	}

	public void adicionarAtos(Atos ato) {
		int posicaoEncontrada = -1;
		// percorre na lista de atos para verificar se o ato já existe
		for (int pos = 0; pos < listaChecklists.size() && posicaoEncontrada < 0; pos++) {
			Checklist cheklistTemp = listaChecklists.get(pos);

			if (cheklistTemp.getAtos().equals(ato)) {
				posicaoEncontrada = pos;
			}
		}

		Checklist checklist = new Checklist();
		checklist.setAtos(ato);
		ChecklistDAO cdao = new ChecklistDAO();

		if (posicaoEncontrada < 0) {
			checklist.setCateoria(ato.getCategoria());
			listaChecklists.add(checklist);
			if (parecerCadastro.getId() != null) {
				Checklist checkTemp = cdao.buscarPorParecerAto(parecerCadastro.getId(), checklist.getAtos().getId());
				if (checkTemp == null) {
					checklist.setParecer(parecerCadastro);
					cdao.salvar(checklist);
				}
			}

		} else {

			checklist.setCateoria(ato.getCategoria());
			listaChecklists.set(posicaoEncontrada, checklist);

		}

	}

	public void removerAtos(Checklist checklist) {
		int posicaoEncontrada = -1;

		for (int pos = 0; pos < listaChecklists.size() && posicaoEncontrada < 0; pos++) {
			Checklist checklistTemp = listaChecklists.get(pos);

			if (checklistTemp.getAtos().equals(checklist.getAtos())) {
				posicaoEncontrada = pos;
			}
		}
		ChecklistDAO cdao = new ChecklistDAO();
		
		if (posicaoEncontrada > -1) {
			listaChecklists.remove(posicaoEncontrada);
			
			if (checklist.getParecer() != null) {
				Checklist checkTemp = cdao.buscarPorParecerAto(checklist.getParecer().getId(),
						checklist.getAtos().getId());
				if (checkTemp != null) {
					cdao.excluir(checkTemp);
				}
			}
		}
	}

	public void editar() {
		try {
			ParecerDAO pdao = new ParecerDAO();
			pdao.editar(parecerCadastro);

			for (Checklist checklist : listaChecklists) {
				ChecklistDAO cdao = new ChecklistDAO();
				checklist.setParecer(parecerCadastro);
				Checklist checkTemp = cdao.buscarPorParecerAto(checklist.getParecer().getId(),
						checklist.getAtos().getId());
				if (checkTemp != null) {
					checkTemp.setParecer(parecerCadastro);
					checkTemp.setAtos(checklist.getAtos());

					cdao.editar(checkTemp);
				} else {
					cdao.salvar(checklist);
				}
			}

			FacesUtil.addMsgInfo("Parecer editado com Sucesso!");
		} catch (RuntimeException ex) {
			FacesUtil.addMsgErro("Ocorreu um erro ao editar o Parecer " + ex.getMessage());
		}
	}

	public String excluir() {
		try {
			for (Checklist checklist : listaChecklists) {
				checklist.setParecer(parecerCadastro);

				ChecklistDAO cdao = new ChecklistDAO();
				cdao.excluir(checklist);
			}

			ParecerDAO pdao = new ParecerDAO();
			pdao.excluir(parecerCadastro);

			FacesUtil.addMsgInfo("Parecer excluído com Sucesso!");
			return "/pages/parecer/parecerPesquisa.xhtml?faces-redirect=true";
		} catch (RuntimeException ex) {
			FacesUtil.addMsgErro("Ocorreu um erro ao excluir o Parecer " + ex.getMessage());
			return null;
		}
	}

	public void gerarParecer() {
		String caminho = "/parecer/parecer.jasper";
		
		Map<String, Object> parametros = new HashMap<>();
		
		if (this.parecerCadastro.getStatus().equals("conformidade")) {
			parametros.put("DESFECHO", "possuindo as");
		}
		if (this.parecerCadastro.getStatus().equals("Conformidade parcial")) {
			parametros.put("DESFECHO", "possuindo parte das");
		}
		if (this.parecerCadastro.getStatus().equals("desconformidade")) {
			parametros.put("DESFECHO", "não possuindo as");
		}
		parametros.put("NUM_PARECER", this.parecerCadastro.getNumero());
		
		GeraParecer geraParecer = new GeraParecer();
		geraParecer.geradorDeParecer(caminho, parametros);
	}
}
