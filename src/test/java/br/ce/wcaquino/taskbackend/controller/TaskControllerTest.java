package br.ce.wcaquino.taskbackend.controller;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;

public class TaskControllerTest {
	
	@Mock
	private TaskRepo taskRepo;
	
	@InjectMocks
	private TaskController controller;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test(expected = ValidationException.class)	
	public void naoDeveSalvarTarefaSemDescricao() throws ValidationException {
		Task todo =  new Task();
		todo.setDueDate(LocalDate.now());
		controller.save(todo);
	}
	
	@Test(expected = ValidationException.class)
	public void naoDeveSalvarTarefaSemData() throws ValidationException {
		Task todo =  new Task();
		todo.setTask("Concluir Teste unitário");
		controller.save(todo);
	}
	
	@Test(expected = ValidationException.class)
	public void naoDeveSalvarTarefaComDataPassada() throws ValidationException {
		Task todo =  new Task();
		todo.setTask("Concluir Teste unitário");
		todo.setDueDate(LocalDate.of(2000, 1, 1));
		controller.save(todo);
	}
	
	@Test
	public void deveSalvarTarefaComSucesso() throws ValidationException {
		Task todo =  new Task();
		todo.setTask("Concluir Teste unitário");
		todo.setDueDate(LocalDate.now());
		ResponseEntity<Task> response = controller.save(todo);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

}
