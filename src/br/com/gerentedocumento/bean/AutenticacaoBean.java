package br.com.gerentedocumento.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.codec.digest.DigestUtils;

import br.com.gerentedocumento.dao.FuncionarioDAO;
import br.com.gerentedocumento.domain.Funcionario;
import br.com.gerentedocumento.util.FacesUtil;

@ManagedBean
@SessionScoped
public class AutenticacaoBean {

	private Funcionario funcionarioLogado;
	
	public Funcionario getFuncionarioLogado() {
		if(funcionarioLogado == null){
			funcionarioLogado = new Funcionario();
		}
		return funcionarioLogado;
	}

	public void setFuncionarioLogado(Funcionario funcionarioLogado) {
		this.funcionarioLogado = funcionarioLogado;
	}



	public String autenticar(){
		try{
			FuncionarioDAO fdao = new FuncionarioDAO();
			funcionarioLogado = fdao.autenticar(funcionarioLogado.getLogin(), DigestUtils.md5Hex(funcionarioLogado.getSenha()));
			
			if (funcionarioLogado == null){
				FacesUtil.addMsgErro("login e/ou senha inválidos!");
				return null;
			}else{
				FacesUtil.addMsgInfo("Funcionário Logado com Sucesso!");
				return "/pages/principal.xhtml?faces-redirect=true";
			}
		}catch(RuntimeException ex){
			FacesUtil.addMsgErro("Erro ao autenticar no sistema: " + ex.getMessage());
			return null;
		}
	}
	
	public String sair(){
		funcionarioLogado = null;
		return "/pages/autenticacao.xhtml?faces-redirect=true";
	}
}
