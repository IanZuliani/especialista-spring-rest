package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.algaworks.algafood.domain.service.CadastroCidadeService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CadastroCidadeService cadastroCidade;
    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CidadeModelAssembler assembler;

    @Autowired
    private CidadeInputDisassembler disassembler;

    @GetMapping("/{cidadeId}")
    public CidadeModel buscar(@PathVariable Long cidadeId) {
        CidadeModel cidadeModel =  assembler.toModel(cadastroCidade.buscarOuFalhar(cidadeId));

        /**
         * Adicionando Link por hypermedia, atraves da heranca de RepresentationModel, dentro do Model CidadeModel
         *
         * WebMvcLinkBuilder que e  uma classe construtor de links dinamicos
         *
         * .slash(cidadeModel.getId()) -> /1
         *  .withSelfRel()); -> "self" na api
         */

        cidadeModel.add(WebMvcLinkBuilder.linkTo(CidadeController.class)
                .slash(cidadeModel.getId())
                .withSelfRel());

        /**
         * Fazendo o LInk para /cidades dinamcamente
         */
        cidadeModel.add(WebMvcLinkBuilder.linkTo(CidadeController.class).withRel("cidades"));

        /**
         * Fazendo o link para o Estado e o id dele
         */
        cidadeModel.getEstado().add(WebMvcLinkBuilder.linkTo(EstadoController.class)
                        .slash(cidadeModel.getEstado().getId())
                        .withRel("estado"));


        return cidadeModel;
    }

    @ApiOperation(value = "Lista as cidades")
    @GetMapping
    public List<CidadeModel> listar() {
        return assembler.toCollectionModel(cidadeRepository.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
        try {
            var cidade = disassembler.toDomainObject(cidadeInput);

            cidade = cadastroCidade.salvar(cidade);

            CidadeModel cidadeModel =  assembler.toModel(cidade);

            ResourceUriHelper.addUriInResponseHeader(cidadeModel.getId());

            return cidadeModel;

        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }

    }

    @PutMapping("/{id}")
    public CidadeModel atualizar(@PathVariable Long id, @RequestBody @Valid CidadeInput cidade) {

        try {
            var cidadeAtual = cadastroCidade.buscarOuFalhar(id);

            disassembler.copyToDomainObject(cidade, cidadeAtual);

           // BeanUtils.copyProperties(cidade, cidadeAtual, "id");

            return assembler.toModel(cadastroCidade.salvar(cidadeAtual));
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }

    }

    @DeleteMapping("/{cidadeAId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cidadeAId) {
        cadastroCidade.excluir(cidadeAId);

    }

}
