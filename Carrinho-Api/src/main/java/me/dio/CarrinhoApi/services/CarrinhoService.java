package me.dio.CarrinhoApi.services;


import me.dio.CarrinhoApi.model.Carrinho;
import me.dio.CarrinhoApi.model.Item;
import me.dio.CarrinhoApi.resources.dto.ItemDto;


public interface CarrinhoService {
    Item incluirItemNoCarrinho(ItemDto itemDto);
    Carrinho verCarrinho(Long id);
    Carrinho fecharCarrinho(Long id, int formaPagamento);
}