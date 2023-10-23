import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Controller controller = new Controller();
    Scanner scanner = new Scanner(System.in);
    Scanner nomeScanner = new Scanner(System.in); // scanner para pegar a linha completa
    boolean processamento = true; // flag para o loop

    System.out.println("Bem-vindo ao Ponto de Venda do Jeremias!");
    System.out.println("O que deseja fazer?\n");

    while (processamento) {
      System.out.println("== Menu ==");
      System.out.println("Selecione uma opção: ");
      System.out.println("1 - Operações no inventário");
      System.out.println("2 - Operações de Vendas");
      System.out.println("3 - Sair\n");
      System.out.print("Opção: ");
      int opcao = scanner.nextInt();
      System.out.println();

      switch (opcao) {
       // Operações no Inventario    
        case 1:
          System.out.println("Operações no Inventario");
          System.out.println("1 - Cadastrar um produto no estoque");
          System.out.println("2 - Editar um produto no estoque");
          System.out.println("3 - Listar os produtos no estoque");
          System.out.println("4 - Remover um produto do estoque");
          System.out.println("5 - Voltar ao Menu");

          System.out.print("\nOpção: ");
          int opcaoInventario = scanner.nextInt();

          // Switch de Inventario
          switch (opcaoInventario) {
           // Adicionar ao Estoque 
            case 1:
              System.out.println();
              System.out.println("Informe o nome do produto:");
              String nomeProduto = nomeScanner.nextLine();
              System.out.println("Informe o preço do produto:");
              double precoProduto = scanner.nextDouble();
              System.out.println("Informe a quantidade do produto a ser adicionada no estoque:");
              int quantidadeProduto = scanner.nextInt();

              controller.cadastrarProduto(nomeProduto, precoProduto, quantidadeProduto);
              System.out.println("\nProduto adicionado ao estoque.\n");
              break;

           // Editar no Estoque
            case 2:
              System.out.println("\nInforme o nome do produto a ser editado:");
              controller.editarProduto(scanner);
              System.out.println("\nProduto editado com sucesso\n");
              break;

           // Listar os produtos no estoque
            case 3:
              System.out.println();
              controller.listarProdutos();
              break;

           // Remover do Estoque
            case 4:
              System.out.println("\nInforme o nome do produto a ser removido:");
              String nomeProdutoRemover = nomeScanner.nextLine();
              controller.removerProduto(nomeProdutoRemover);
              System.out.println("Produto removido com sucesso.\n");
              break;

           // Voltar ao menu principal
            case 5:
              break;

            default:
              System.out.println("Opção inválida. Tente novamente.");
          }
          break;

       // Operações de Vendas
        case 2:
          System.out.println("Operações de Vendas");
          System.out.println("1 - Cadastrar uma venda");
          System.out.println("2 - Listar as vendas atuais");
          System.out.println("3 - Apagar uma venda");
          System.out.println("4 - Voltar ao Menu");

          System.out.print("\nOpção: ");
          int opcaoVendas = scanner.nextInt();

          // Switch de Vendas
          switch (opcaoVendas) {
           // Cadastrar uma venda
            case 1:
              System.out.println("\nQuantos produtos a venda terá?");
              int quantidadeDaVenda = scanner.nextInt();
              controller.adicionarItemVenda(quantidadeDaVenda, scanner, nomeScanner);
              System.out.println("\nVenda cadastrada!\n");
              break;

           // Listar as vendas cadastradas
            case 2:
              System.out.println();
              controller.relatorioVendas();
              break;

           // Apagar uma venda
            case 3:
              System.out.print("\nInforme o número da venda a ser apagada: ");
              int numeroVendaApagar = scanner.nextInt();
              controller.apagarVenda(numeroVendaApagar);
              break;

           // Voltar ao menu principal
            case 4:
              break;

            default:
              System.out.println("Opção inválida. Tente novamente.");
          }
          break;

       // Encerrar o Processamento
        case 3:
          System.out.println("Sistema encerrado.");
          processamento = false;
          break;

        default:
          System.out.println("Opção inválida. Tente novamente.");
      }
    }
  }
}