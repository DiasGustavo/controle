package br.com.gerentedocumento.bean;

import java.io.IOException;
import java.util.Date;
import java.util.List;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.gerentedocumento.dao.CadastraDocumentoDAO;
import br.com.gerentedocumento.dao.DocumentoDAO;
import br.com.gerentedocumento.dao.EmpresaDAO;
import br.com.gerentedocumento.dao.FuncionarioDAO;
import br.com.gerentedocumento.dao.OrgaoDAO;
import br.com.gerentedocumento.domain.CadastraDocumento;
import br.com.gerentedocumento.domain.Documento;
import br.com.gerentedocumento.domain.Empresa;
import br.com.gerentedocumento.domain.Funcionario;
import br.com.gerentedocumento.domain.Orgao;
import br.com.gerentedocumento.util.DownloadArquivoUtil;
import br.com.gerentedocumento.util.FacesUtil;
import br.com.gerentedocumento.util.UploadArquivoUtil;

@ManagedBean
@ViewScoped
public class DocumentoBean {

	private Documento docCadastro;
	private CadastraDocumento cadDocumentoCadastro;

	private List<Documento> listaDocumentos;
	private List<Documento> listaDocumentosFiltrados;
	private List<Funcionario> listaFuncionarios;
	private List<Empresa> listaEmpresas;

	@ManagedProperty(value = "#{autenticacaoBean}")
	private AutenticacaoBean autenticacaoBean;

	private UploadedFile arquivoUpload;
	private StreamedContent arquivoVisualizar;

	private String acao;
	private Long codigo;

	public Documento getDocCadastro() {
		if (docCadastro == null) {
			docCadastro = new Documento();
		}
		return docCadastro;
	}

	public void setDocCadastro(Documento docCadastro) {
		this.docCadastro = docCadastro;
	}

	public CadastraDocumento getCadDocumentoCadastro() {
		return cadDocumentoCadastro;
	}

	public void setCadDocumentoCadastro(CadastraDocumento cadDocumentoCadastro) {
		this.cadDocumentoCadastro = cadDocumentoCadastro;
	}

	public List<Documento> getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(List<Documento> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}

	public List<Documento> getListaDocumentosFiltrados() {
		return listaDocumentosFiltrados;
	}

	public void setListaDocumentosFiltrados(List<Documento> listaDocumentosFiltrados) {
		this.listaDocumentosFiltrados = listaDocumentosFiltrados;
	}

	public List<Funcionario> getListaFuncionarios() {
		return listaFuncionarios;
	}

	public void setListaFuncionarios(List<Funcionario> listaFuncionarios) {
		this.listaFuncionarios = listaFuncionarios;
	}
	
	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}

	public void setListaEmpresas(List<Empresa> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}

	public AutenticacaoBean getAutenticacaoBean() {
		return autenticacaoBean;
	}

	public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
		this.autenticacaoBean = autenticacaoBean;
	}

	public UploadedFile getArquivoCarregado() {
		return arquivoUpload;
	}

	public void setArquivoCarregado(UploadedFile arquivoCarregado) {
		this.arquivoUpload = arquivoCarregado;
	}

	public StreamedContent getArquivoVisualizar() {
		return arquivoVisualizar;
	}

	public void setArquivoVisualizar(StreamedContent arquivoVisualizar) {
		this.arquivoVisualizar = arquivoVisualizar;
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

	public void novo() {
		docCadastro = new Documento();
	}

	public void salvar() {
		try {
			DocumentoDAO ddao = new DocumentoDAO();
			if (!(arquivoUpload.getFileName().isEmpty())) {
				upload();
				ddao.salvar(docCadastro);

				// captura as informações do documento cadastrado
				Documento doc = ddao.buscarPorProcessoSecretaria(docCadastro.getProcesso(),
						docCadastro.getSecretaria());
				CadastraDocumento cadastra = new CadastraDocumento();
				cadastra.setCodDocumento(doc.getId());
				cadastra.setCodFuncionario(autenticacaoBean.getFuncionarioLogado().getId());
				cadastra.setDataCadastro(new Date());

				// cadastra o log do cadastro
				CadastraDocumentoDAO cddao = new CadastraDocumentoDAO();
				cddao.salvar(cadastra);

				docCadastro = new Documento();

				FacesUtil.addMsgInfo("Documento cadastrado com Sucesso!");
			} else {
				ddao.salvar(docCadastro);

				// captura as informações do documento cadastrado
				Documento doc = ddao.buscarPorProcessoSecretaria(docCadastro.getProcesso(),
						docCadastro.getSecretaria());
				CadastraDocumento cadastra = new CadastraDocumento();
				cadastra.setCodDocumento(doc.getId());
				cadastra.setCodFuncionario(autenticacaoBean.getFuncionarioLogado().getId());
				cadastra.setDataCadastro(new Date());

				// cadastra o log do cadastro
				CadastraDocumentoDAO cddao = new CadastraDocumentoDAO();
				cddao.salvar(cadastra);

				docCadastro = new Documento();

				FacesUtil.addMsgInfo("Documento cadastrado com Sucesso!");
			}
		} catch (RuntimeException ex) {
			FacesUtil.addMsgErro("Erro ao cadastrar o Documento! " + ex.getMessage());
		}
	}

	public void listar() {
		try {
			DocumentoDAO ddao = new DocumentoDAO();
			listaDocumentos = ddao.listar();
		} catch (RuntimeException ex) {
			FacesUtil.addMsgErro("Erro ao listar os Documentos " + ex.getMessage());
		}
	}

	public void carregarDados() {
		try {
			if (codigo != null) {
				DocumentoDAO ddao = new DocumentoDAO();
				docCadastro = ddao.buscarPorCodigo(codigo);
				// carregar o arquivo armazenado
				if (!(docCadastro.getDocArquivo()== null)) {
					DownloadArquivoUtil downloadArquivo = new DownloadArquivoUtil();
					arquivoVisualizar = downloadArquivo.visualizarArquivo(docCadastro.getDocArquivo(),
							"application/pdf");
				}
			} else {
				docCadastro = new Documento();
			}
			DocumentoDAO ddao = new DocumentoDAO();
			listaDocumentos = ddao.listar();

			FuncionarioDAO fdao = new FuncionarioDAO();
			listaFuncionarios = fdao.listar();
			
			EmpresaDAO edao = new EmpresaDAO();
			listaEmpresas = edao.listar();

		} catch (RuntimeException ex) {
			FacesUtil.addMsgErro("Erro ao carregar os dados do Documento! " + ex.getMessage());
		}
	}

	public void editar() {
		try {
			DocumentoDAO ddao = new DocumentoDAO();

			if (arquivoUpload != null && docCadastro.getDocArquivo() == null) {
				upload();
				ddao.editar(docCadastro);

				// captura o log correto e atualiza
				CadastraDocumentoDAO cddao = new CadastraDocumentoDAO();
				CadastraDocumento cadastra = cddao.buscarPorCodigoDocumento(docCadastro.getId());
				cadastra.setCodFuncionario(autenticacaoBean.getFuncionarioLogado().getId());
				cadastra.setDataCadastro(new Date());
				cddao.editar(cadastra);

				docCadastro = new Documento();

				FacesUtil.addMsgInfo("Documento editado com Sucesso!");
			} else {
				ddao.editar(docCadastro);

				// captura o log correto e atualiza
				CadastraDocumentoDAO cddao = new CadastraDocumentoDAO();
				CadastraDocumento cadastra = cddao.buscarPorCodigoDocumento(docCadastro.getId());
				cadastra.setCodFuncionario(autenticacaoBean.getFuncionarioLogado().getId());
				cadastra.setDataCadastro(new Date());
				cddao.editar(cadastra);

				docCadastro = new Documento();

				FacesUtil.addMsgInfo("Documento editado com Sucesso!");
			}			
		} catch (RuntimeException ex) {
			FacesUtil.addMsgErro("Erro ao editar o Documento! " + ex.getMessage());
		}
	}

	public void excluir() {
		try {
			DocumentoDAO ddao = new DocumentoDAO();
			ddao.excluir(docCadastro);

			docCadastro = new Documento();

			FacesUtil.addMsgInfo("Documento excluído com Sucesso!");
		} catch (RuntimeException ex) {
			FacesUtil.addMsgErro("Erro ao excluir o Documento! " + ex.getMessage());
		}
	}	

	public void upload() {
		UploadArquivoUtil upload = new UploadArquivoUtil();
		OrgaoDAO odao = new OrgaoDAO();
		Orgao orgao = odao.buscarPorCodigo(1L);
		try {
			docCadastro.setDocArquivo(upload.upload(orgao.getRepositorio(), arquivoUpload.getFileName(),
					arquivoUpload.getInputstream()));

		} catch (IOException e) {
			FacesUtil.addMsgErro("Erro ao fazer upload do arquivo! " + e.getMessage());
		}
	}
	
	public void deletarArquivo(){
		try{
			UploadArquivoUtil upload = new UploadArquivoUtil();
			upload.removerArquivo("D:\\tmp\\Bloqueando Facebook Zentyal -Tutorial.pdf");
			this.docCadastro.setDocArquivo(null);
			DocumentoDAO ddao = new DocumentoDAO();
			arquivoVisualizar = null;
			ddao.editar(docCadastro);
						
		}catch(RuntimeException ex){
			FacesUtil.addMsgErro("Erro ao excluir o arquivo! " + ex.getMessage());
		}
	}

}
