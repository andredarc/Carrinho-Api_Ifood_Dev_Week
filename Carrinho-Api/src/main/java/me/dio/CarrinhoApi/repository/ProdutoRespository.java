package me.dio.CarrinhoApi.repository;


import me.dio.CarrinhoApi.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRespository extends JpaRepository<Produto, Long> {
}