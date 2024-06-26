package br.com.lucas.contatos.service;

import br.com.lucas.contatos.dto.ContatoCadastroDto;
import br.com.lucas.contatos.dto.ContatoExibicaoDto;
import br.com.lucas.contatos.exception.ContatoNaoEncontradoException;
import br.com.lucas.contatos.model.Contato;
import br.com.lucas.contatos.repository.ContatoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContatoService{

    @Autowired
    private ContatoRepository contatoRepository;

    public ContatoExibicaoDto gravar(ContatoCadastroDto contatoCadastroDto){
        Contato contato = new Contato();
        //Copiando os atributos de uma classe para outra
        BeanUtils.copyProperties(contatoCadastroDto, contato);
        return new ContatoExibicaoDto(contatoRepository.save(contato));
    }

    public ContatoExibicaoDto buscarPeloId(UUID id){
        Optional<Contato> contato = contatoRepository.findById(id);
        if(contato.isPresent()){
            return new ContatoExibicaoDto(contato.get());
        }
        else{
            throw new ContatoNaoEncontradoException("O Contato não existe");
        }
    }

    public Page<ContatoExibicaoDto> buscarTodos(Pageable paginacao){
       return contatoRepository.findAll(paginacao).map(ContatoExibicaoDto::new);
    }

    public ContatoExibicaoDto atualizar(Contato contato){
        Optional<Contato> contatoOptional = contatoRepository.findById(contato.getId());
        if(contatoOptional.isPresent()){
            var novoVontato = contatoRepository.save(contato);
            return new ContatoExibicaoDto(novoVontato);
        }
        else{
            throw new RuntimeException("Contato não encontrado");
        }
    }

    public void remover(UUID id){
        Optional<Contato> contatoOptional = contatoRepository.findById(id);
        if(contatoOptional.isPresent()){
            contatoRepository.deleteById(id);
        }
        else{
            throw new RuntimeException("Contato não encontrado");
        }
    }

    public ContatoExibicaoDto buscarPorNome(String nome){
        Optional<Contato> contatoOptional = contatoRepository.findByNome(nome);
        if(contatoOptional.isPresent()){
            return new ContatoExibicaoDto(contatoOptional.get());
        }
        else{
            throw new ContatoNaoEncontradoException("Contato não encontrado");
        }
    }

//    public List<ContatoExibicaoDto> buscarAniversariantes(LocalDate dataInicial, LocalDate dataFinal){
//        List<Contato> listaContato = contatoRepository.findByDataNascimentoBetween(dataInicial, dataFinal);
//        List<ContatoExibicaoDto> contatoExibicaoDtos = new ArrayList<>();
//        for(Contato contato : listaContato){
//            contatoExibicaoDtos.add(new ContatoExibicaoDto(contato));
//        }
//        return contatoExibicaoDtos;
//    }

    public List<ContatoExibicaoDto> buscarAniversariantes(LocalDate dataInicial, LocalDate dataFinal){
        return contatoRepository.findByDataNascimentoBetween(dataInicial, dataFinal)
        .stream()
        .map(ContatoExibicaoDto::new)
        .toList();
    }

}