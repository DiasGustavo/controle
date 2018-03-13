package br.com.gerentedocumento.test;

import org.junit.Test;

import br.com.gerentedocumento.dao.ChecklistDAO;
import br.com.gerentedocumento.domain.Checklist;

public class ChecklistDAOTest {

	@Test
	public void editar(){
		Checklist check = new Checklist();
		ChecklistDAO cdao = new ChecklistDAO();
		
		check = cdao.buscarPorCodigo(33L);
		check.setCateoria("obra");
		
		cdao.editar(check);
	}
}
