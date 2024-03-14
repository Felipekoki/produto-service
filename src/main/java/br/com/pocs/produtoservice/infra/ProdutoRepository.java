package br.com.pocs.produtoservice.infra;

import br.com.pocs.produtoservice.dominio.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
