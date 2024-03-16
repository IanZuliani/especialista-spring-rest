package com.algaworks.algafood.api.exceptionHandler;

import com.algaworks.algafood.domain.exception.EndidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.NegocioExceptional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        var problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisicao esta invalido, verifique erro de sintaxe";

        Problem problem = createProblemBuilder(status, problemType, detail).build();

       // return super.handleHttpMessageNotReadable(ex, headers, status, request);
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EndidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEstadoNaoEncontradoException(EndidadeNaoEncontradaException e, WebRequest request){

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;
        String detail = e.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail).build();




     /*   Problem problem = Problem.builder()
                .status(status.value())
                .type("http://algafood.com.br/entidade-nao-encontrada")
                .title("Entidade Nao Encontrada")
                .detail(e.getMessage())
                .build();*/

        return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);

       /* var problema = Problema.builder()
                .dataHora(LocalDateTime.now())
                .mensagem(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problema);*/
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest request){

        HttpStatus status = HttpStatus.CONFLICT;
        var problemType = ProblemType.ENTIDADE_EM_USO;
        String detail = e.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(NegocioExceptional.class)
    public ResponseEntity<?> handleNegocioExceptional(NegocioExceptional e, WebRequest request){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        var problemType = ProblemType.ERRO_NEGOCIO;
        String detail = e.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {

        if(body == null){
            body = Problem.builder()
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .title((String) body)
                    .status(status.value())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail){
        return Problem.builder()
                .status(status.value())
                .type(problemType.getUri())
                .type(problemType.getTitle())
                .detail(detail);

    }
}
