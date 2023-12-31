package br.edu.ifpb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Venda implements Serializable {
  private List<Produto> produtos;

  public Venda() {
    produtos = new ArrayList<>();
  }

  public void adicionarProduto(Produto produto, int quantidadeVendida) {
    produtos.add(new Produto(produto.getIdentificador(), produto.getNome(), produto.getPreco(), produto.getQuantidade(), quantidadeVendida));
  }

  public List<Produto> getProdutos() {
    return produtos;
  }
}