package me.dio.CarrinhoApi.services.impl;

import lombok.RequiredArgsConstructor;
import me.dio.CarrinhoApi.enums.FormaPagamento;
import me.dio.CarrinhoApi.model.Item;
import me.dio.CarrinhoApi.model.Restaurante;
import me.dio.CarrinhoApi.model.Carrinho;
import me.dio.CarrinhoApi.repository.ItemRepository;
import me.dio.CarrinhoApi.repository.ProdutoRespository;
import me.dio.CarrinhoApi.repository.CarrinhoRepository;
import me.dio.CarrinhoApi.resources.dto.ItemDto;
import me.dio.CarrinhoApi.services.CarrinhoService;
import org.springframework.stereotype.Service;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CarrinhoServiceImpl implements CarrinhoService {
    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoRespository produtoRepository;
    private final ItemRepository itemRepository;

    @Override
    public Item incluirItemNoCarrinho(ItemDto itemDto) {
        Carrinho carrinho = verCarrinho(itemDto.getCarrinhoId());


        if(carrinho.isFechada()) {
            throw new RuntimeException("Esse Carrinho está fechado!");
        }

        Item itemParaSerInserido = Item.builder()
                .quantidade(itemDto.getQuantidade())
                .carrinho(carrinho)
                .produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(() -> {
                    throw new RuntimeException("Esse produto não existe!");
                }))
                .build();

        List<Item>itensDoCarrinho = carrinho.getItens();
        if(itensDoCarrinho.isEmpty()) {
            itensDoCarrinho.add(itemParaSerInserido);
        } else {
            Restaurante restauranteAtual = itensDoCarrinho.get(0).getProduto().getRestaurante();
            Restaurante restauranteDoItemParaAdicionar = itemParaSerInserido.getProduto().getRestaurante();

            if(restauranteAtual.equals(restauranteDoItemParaAdicionar)) {
                itensDoCarrinho.add(itemParaSerInserido);
            } else {
                throw new RuntimeException("Não é possível adicionar produtos de restaurantes diferentes.");
            }
        }

        List<Double> valorDosItens = new ArrayList<>();
        for (Item itemDoCarrinho: itensDoCarrinho) {
            double valorTotalItem =
                    itemDoCarrinho.getProduto().getValorUnitario() * itemDoCarrinho.getQuantidade();
            valorDosItens.add(valorTotalItem);
        }

        double valorTotalCarrinho = valorDosItens.stream()
                .mapToDouble(valorTotalDeCadaItem -> valorTotalDeCadaItem)
                .sum();

        carrinho.setValorTotal(valorTotalCarrinho);
        carrinhoRepository.save(carrinho);
        return itemParaSerInserido;
    }

    @Override
    public Carrinho verCarrinho(Long id) {
        return carrinhoRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Esse Carrinho não existe!");
                }
        );
    }

    @Override
    public Carrinho fecharCarrinho(Long id, int numeroformaPagamento) {
        Carrinho carrinho = verCarrinho(id);
        if (carrinho.getItens().isEmpty()) {
            throw new RuntimeException("Inclua ítens no carrinho!");
        }
    /*if (numeroformaPagamento == 0) {
      carrinho.setFormaPagamento(FormaPagamento.DINHEIRO);
    } else {
      carrinho.setFormaPagamento(FormaPagamento.CARTAO);
    }*/
        FormaPagamento formaPagamento =
                numeroformaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.CARTAO;
        carrinho.setFormaPagamento(formaPagamento);
        carrinho.setFechada(true);
        return carrinhoRepository.save(carrinho);
    }
}


