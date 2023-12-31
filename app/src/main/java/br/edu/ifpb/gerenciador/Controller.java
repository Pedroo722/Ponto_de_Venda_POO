package br.edu.ifpb.gerenciador;

import java.util.List;

import br.edu.ifpb.exceptions.EstoqueVazioException;
import br.edu.ifpb.exceptions.IDInvalidoException;
import br.edu.ifpb.exceptions.InventarioInsuficienteException;
import br.edu.ifpb.exceptions.NumeroVendaInvalidoException;
import br.edu.ifpb.exceptions.ProdutoNaoEncontradoException;
import br.edu.ifpb.exceptions.VendasVazioException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import br.edu.ifpb.model.ListaDeVenda;
import br.edu.ifpb.model.Produto;
import br.edu.ifpb.model.Venda;
import br.edu.ifpb.model.Inventario;

import java.io.Serializable;


// Classe Principal

public class Controller implements Serializable {
  private static Controller instance;
  private Inventario inventario;
  private Venda itemVenda;
  private ListaDeVenda vendas;

  public Controller() {
    inventario = new Inventario();
    itemVenda = new Venda();
    vendas = new ListaDeVenda();

    inicializarProdutos(); 
    inicializarVendas(); 
  }

  public static Controller getInstance() {
    if (instance == null) {
      instance = new Controller();
    }
    return instance;
  }

  // Serialização e Deserialização

  public void inicializarProdutos() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("produtos.bin"))) {
        List<Produto> produtosSerializados = (List<Produto>) ois.readObject();
        inventario.setProdutos(produtosSerializados);
    } catch (FileNotFoundException e) {
        // Handle file not found exception
        e.printStackTrace();
    } catch (IOException | ClassNotFoundException e) {
        // Handle IO or class not found exception
        e.printStackTrace();
    }
  }

  public void inicializarVendas() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("vendas.bin"))) {
        List<Venda> vendasSerializadas = (List<Venda>) ois.readObject();
        vendas.setVendas(vendasSerializadas);
    } catch (FileNotFoundException e) {
        // Handle file not found exception
        e.printStackTrace();
    } catch (IOException | ClassNotFoundException e) {
        // Handle IO or class not found exception
        e.printStackTrace();
    }
  }

  public void salvarProdutos() {
    try (FileOutputStream fileOut = new FileOutputStream("produtos.bin");
         ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
        List<Produto> produtos = inventario.listarProdutos();
        out.writeObject(produtos);
        System.out.printf("Produtos salvos em produtos.bin%n");
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  public void salvarVendas() {
    try (FileOutputStream fileOut = new FileOutputStream("vendas.bin");
         ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
        List<Venda> vendasList = vendas.listarVendas();
        out.writeObject(vendasList);
        System.out.printf("Vendas salvas em vendas.bin%n");
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  // Caso 1. Cadastrar produto no Estoque
  public void cadastrarProduto(Integer identificadorProduto, String nomeProduto, double precoProduto, int quantidadeProduto) {
    for (Produto produto : inventario.listarProdutos()) {
      if (identificadorProduto.equals(produto.getIdentificador())) {
        throw new IDInvalidoException();
      }
    }

    Produto novoProduto = new Produto(identificadorProduto, nomeProduto, precoProduto, quantidadeProduto);
    inventario.adicionarProduto(novoProduto);
  }

  // Caso 2. Editar um produto no Estoque
  public void editarProduto(Integer produtoEditando, String novoNomeProduto, Double novoPrecoProduto, int novaQuantidadeProduto) {
    if (inventario.listarProdutos().isEmpty()) {
      throw new EstoqueVazioException();
    }

    boolean produtoEncontrado = false;

    for (Produto produto : inventario.listarProdutos()) {
      if (produtoEditando.equals(produto.getIdentificador())) {

        produto.setNome(novoNomeProduto);
        produto.setPreco(novoPrecoProduto);
        produto.setQuantidade(novaQuantidadeProduto);

        produtoEncontrado = true;
        break;
      }
    }

    if (!produtoEncontrado) {
      throw new ProdutoNaoEncontradoException();
    }
  }

  // Caso 3. Listar os produtos do Estoque
  public List<Produto> listarProdutos() {
    List<Produto> produtosNoEstoque = inventario.listarProdutos();

    if (produtosNoEstoque.isEmpty()) {
      throw new EstoqueVazioException();
    }

    return produtosNoEstoque;
  }

  // Caso 4. Remover um produto especifico do Estoque
  public void removerProduto(Integer idProdutoRemover) {
    List<Produto> produtos = inventario.listarProdutos();
    boolean produtoEncontrado = false;

    for (Produto produto : produtos) {
      if (idProdutoRemover.equals(produto.getIdentificador())) {
        inventario.removerProduto(produto);
        produtoEncontrado = true;
        break;
      }
    }

    if (!produtoEncontrado) {
      throw new ProdutoNaoEncontradoException();
    }
  }

  // Caso 5. Cadastrar uma venda
  public void adicionarItemVenda(List<Integer> identificadoresProdutos, List<Integer> quantidadesVendidas) {
    Venda novaVenda = new Venda();

    for (int i = 0; i < identificadoresProdutos.size(); i++) {
        int identificadorProduto = identificadoresProdutos.get(i);
        int quantidadeVendida = quantidadesVendidas.get(i);

        Produto produto = buscarProdutoPorIdentificador(identificadorProduto);

        if (produto != null) {
            if (quantidadeVendida <= produto.getQuantidade()) {
                novaVenda.adicionarProduto(produto, quantidadeVendida);
                produto.setQuantidade(produto.getQuantidade() - quantidadeVendida);
            } else {
                throw new InventarioInsuficienteException();
            }
        } else {
            throw new ProdutoNaoEncontradoException();
        }
    }

    vendas.novaVenda(novaVenda);
  }

  public Produto buscarProdutoPorIdentificador(int identificadorProduto) {
    for (Produto produto : inventario.listarProdutos()) {
      if (identificadorProduto == produto.getIdentificador()) {
        return produto;
      }
    }
    return null;
  }

  // Caso 6. Listar as vendas atuais
  public List<Venda> relatorioVendas() {
    List<Venda> vendas = this.vendas.listarVendas();

    if (vendas.isEmpty()) {
      throw new VendasVazioException();
    } else {
      return vendas;
    }
  }

  // Caso 7. Apagar uma venda
  public void apagarVenda(int numeroVenda) {
    if (numeroVenda <= 0 || numeroVenda > vendas.listarVendas().size()) {
      throw new NumeroVendaInvalidoException();
    }

    vendas.listarVendas().remove(numeroVenda - 1);
  }

  // Função para retornar nome do produto
  public String obterNome(int idProduto) {
    for (Produto produto : inventario.listarProdutos()) {
        if (idProduto == produto.getIdentificador()) {
            return produto.getNome();
        }
    }
    return "Não encontrado";
  }
}