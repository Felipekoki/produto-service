package br.com.pocs.produtoservice.pontodeentrada.rest;

import br.com.pocs.produtoservice.aplicacao.ProdutoService;
import br.com.pocs.produtoservice.dominio.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<Produto>> getAllProdutos() {
        return ResponseEntity.ok(produtoService.obterTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.obterPorId(id));
    }

    @PostMapping
    public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.cadastrar(produto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produto) {
        produto.setId(id);
        return ResponseEntity.ok(produtoService.atualizar(produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
