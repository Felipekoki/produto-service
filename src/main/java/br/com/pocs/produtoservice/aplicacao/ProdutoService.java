package br.com.pocs.produtoservice.aplicacao;

import br.com.pocs.produtoservice.dominio.Produto;
import br.com.pocs.produtoservice.infra.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> obterTodos(){
        return produtoRepository.findAll();
    }

    public Produto obterPorId(Long id){
        return produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public Produto cadastrar(Produto produto){
        return produtoRepository.save(produto);
    }
    
    public Produto atualizar(Produto produto) {
        return produtoRepository.findById(produto.getId()).map(produtoCadastrado -> {
            produtoCadastrado.setCodigo(produto.getNome());
            produtoCadastrado.setNome(produto.getNome());
            produtoCadastrado.setDescricao(produto.getDescricao());
            return produtoRepository.save(produtoCadastrado);
        }).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }
}
