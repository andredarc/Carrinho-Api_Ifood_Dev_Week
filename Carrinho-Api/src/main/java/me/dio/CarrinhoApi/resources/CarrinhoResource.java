package me.dio.CarrinhoApi.resources;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import me.dio.CarrinhoApi.model.Carrinho;
import me.dio.CarrinhoApi.model.Item;
import me.dio.CarrinhoApi.resources.dto.ItemDto;
import me.dio.CarrinhoApi.services.CarrinhoService;
import org.springframework.web.bind.annotation.*;

@Api(value="/ifood-dev-week/carrinho")
@RestController
@RequestMapping("/ifood-dev-week/carrinho")
@RequiredArgsConstructor
public class CarrinhoResource {
    private final CarrinhoService carrinhoService;

    @PostMapping
    public Item incluirItemNoCarrinho(@RequestBody ItemDto itemDto) {
        return carrinhoService.incluirItemNoCarrinho(itemDto);
    }

    @GetMapping("/{id}")
    public Carrinho verCarrinho(@PathVariable("id") Long id) {
        return carrinhoService.verCarrinho(id);
    }

    @PatchMapping("/fecharCarrinho/{sacolaId}")
    public Carrinho fecharCarrinho(@PathVariable("carrinhoId") Long carrinhoId,
                               @RequestParam("formaPagamento") int formaPagamento) {
        return carrinhoService.fecharCarrinho(carrinhoId, formaPagamento);
    }
}