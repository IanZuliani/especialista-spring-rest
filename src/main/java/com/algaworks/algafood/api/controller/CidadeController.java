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
import org.springframework.hateoas.CollectionModel;
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

    /**
     * No metodo List de cidades vamos alterar ele para retornar um CollectionModel
     * @return  CollectionModel<CidadeModel>
     */
    @GetMapping
    public CollectionModel<CidadeModel> listar() {
        /**
         * Depos a verificamos que a funcao assembler.toCollectionModel retorna um LIst
         * Jogamos para uma lista de cidade
         */
        List<CidadeModel> cidadesModel =  assembler.toCollectionModel(cidadeRepository.findAll());

        cidadesModel.forEach(cidadeModel -> {
            cidadeModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class)
                    .buscar(cidadeModel.getId())).withSelfRel());
            cidadeModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class)
                    .listar()).withRel("cidades"));
            cidadeModel.getEstado().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EstadoController.class)
                    .buscar(cidadeModel.getEstado().getId())).withRel("estado"));
        });



        /**
         * Depos utilzamos a funca estatica CollectionModel.of para transformar nossa lista de cidades model em uma collection de cidadeModel
         */
        CollectionModel<CidadeModel> cidadesCollectionModel = CollectionModel.of(cidadesModel);

        /**
         * Link para o controllador
         */
        cidadesCollectionModel.add(WebMvcLinkBuilder.linkTo(CidadeController.class).withSelfRel());


        return cidadesCollectionModel;
    }

    @Autowired
    private CidadeModelAssembler assembler;

    @Autowired
    private CidadeInputDisassembler disassembler;

    @GetMapping("/{cidadeId}")
    public CidadeModel buscar(@PathVariable Long cidadeId) {
        CidadeModel cidadeModel =  assembler.toModel(cadastroCidade.buscarOuFalhar(cidadeId));


        /**
         * Vamos criar um link passa o controllador/id
         * vamos fazer separado
         * nao vamos montar utilizando o slash, vamos apontar para o metodo utilizando a funcao
         * methodOn → passamos o Controllador que queremos
         * COm isso ele cria um proxy do controllador, e com esse proxy podemos chamar os metodos
         * Mas ele nao executa o metodo, estamos apenas registrando uma chamada nesse metodo para gerar o link
         * 6. estamos chamando o metodo do proxy e nao do controller, ele vai registrar um historico dele
         * 7. Para quando fazermos um linkTO para o metodo que chamamos
         */
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class)
                .buscar(cidadeModel.getId())).withSelfRel();

        cidadeModel.add(link);

        /**
         * Adicionando Link por hypermedia, atraves da heranca de RepresentationModel, dentro do Model CidadeModel
         *
         * WebMvcLinkBuilder que e  uma classe construtor de links dinamicos
         *
         * .slash(cidadeModel.getId()) -> /1
         *  .withSelfRel()); -> "self" na api
         */

      /*  cidadeModel.add(WebMvcLinkBuilder.linkTo(CidadeController.class)
                .slash(cidadeModel.getId())
                .withSelfRel());*/

        /**
         * Fazendo o LInk para /cidades dinamcamente
         */

        cidadeModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class)
                .listar()).withRel("cidades"));
        //cidadeModel.add(WebMvcLinkBuilder.linkTo(CidadeController.class).withRel("cidades"));

        /**
         * Fazendo o link para o Estado e o id dele
         */

        cidadeModel.getEstado().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EstadoController.class)
                .buscar(cidadeModel.getEstado().getId())).withRel("estado"));

        /*cidadeModel.getEstado().add(WebMvcLinkBuilder.linkTo(EstadoController.class)
                        .slash(cidadeModel.getEstado().getId())
                        .withRel("estado"));*/


        return cidadeModel;
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
