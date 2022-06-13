/**
 * CLASSE Estoque QUE CONTROLA A TODOS OS ITENS CRIADOS
 * COM MAPAS E LISTAS, ALÉM DE PROPRIEDADES DE ADMNISTRADOR
 * CONTÉM COMPORTAMENTOS RESPONSÁVEIS PARA MANIPULAR OS ITENS
 */

package br.com.cantinaifal.estoque;


import java.sql.*;
import java.util.*;
import br.com.cantinaifal.database.connection.CantinaIfalConnector;
import br.com.cantinaifal.estoque.sql.*;


public class Estoque {

    private Connection connection;
    private EstoqueConsulta consulta;
    private List<Funcionario> funcionarios;

    public Estoque() throws SQLException {
        // construtor de Estoque
        
        this.connection = CantinaIfalConnector.connect();
        this.consulta = new EstoqueConsulta(this.connection);
        this.funcionarios = new ArrayList<>();
        
        this.consulta.insertProduto(
            new Item(4564, "Agua", 2.50, 3.50, 10, 1)
        );
        this.consulta.insertProduto(
            new Item(5564, "Coxinha", 3.00, 5.00, 60, 1)
        );
        this.consulta.insertProduto(
            new Item(7975, "Amendoim", 10.00, 12.50, 100, 1)
        );
        this.consulta.insertProduto(
            new Item(4212, "Sanduiche", 5.00, 6.00, 5, 1)
        );
        this.consulta.insertProduto(
            new Item(1225, "Bolo de chocolate", 3.50, 4.50, 12, 1)
        );
    }

    // métodos para manipular admnistrador

    public void cadastrarFuncionario(String login, String senha) throws Exception {
        Funcionario novoFunc = new Funcionario(
            this.gerarChaveUnica(3),
            login, senha
        );
        this.adicionarFuncionario(novoFunc);
    }

    private void adicionarFuncionario(Funcionario func) throws Exception {
        this.funcionarios.add(func);
        this.consulta.insertFuncionario(func);
    }

    public void entrarComoFuncionario(String login, String senha) throws SQLException, IllegalArgumentException {
        String sql = "SELECT nome, senha FROM cantinaifal.funcionario " +
                     "WHERE nome = ? AND senha = ?";

        Map<Integer, Object> atributos = new HashMap<>();
        atributos.put(1, login);
        atributos.put(2, senha);

        ResultSet result = this.consulta.selectQuery(sql, atributos); 

        if(result != null && result.next()) {
            return;
        } else {
            throw new IllegalArgumentException("Login ou senha inválidos");
        }
    }

    public int getQuantosFuncionarios() {
        return this.funcionarios.size();
    }

    // public void setSenhaAdmin(Scanner inputUsuario) throws IllegalArgumentException {
    //     // RESPONSÁVEL POR SETAR A SENHA DE ADMINISTRADOR

    //     // caso senha já tenha sido definida (temAdmin = true)
    //     // precisará passar por verificação para que a senha possa ser redefinida
    //     if(this.temAdmin) {
    //         System.out.print("Digite a senha atual para prosseguir: ");
    //         String senhaAtual = inputUsuario.nextLine();

    //         if(!senhaAtual.equals(this.senhaAdmin)) {
    //             throw new IllegalArgumentException("As senhas não correspondem!");
    //         }
    //     }
    //     // digitando nova senha
    //     System.out.print("Digite a nova senha: ");
    //     String senhaNova = inputUsuario.nextLine();

    //     // caso a senha não tenha 4 dígitos de apenas números, lance uma exceção
    //     if(!senhaNova.matches("[0-9]{4}")) {
    //         throw new IllegalArgumentException(
    //             "Senha inválida!" +
    //             "\nDigite uma senha de 4 dígitos contendo apenas números."
    //         );
    //     }
    //     // definindo nova senha
    //     this.senhaAdmin = senhaNova;
    //     System.out.println("Senha cadastrada com sucesso!\n");
    //     // setando temAdmin para true, pois a senha foi definida
    //     this.temAdmin = true;
    // }

    // public void entrarComoAdmin(Scanner inputUsuario) throws IllegalArgumentException {
    //     // método para autenticar o acesso de administrador pedindo a senha
    //     // caso a senha seja autenticada, a propriedade this.acessoAdmin se torna true
    //     // para mostrar que o admin agora tem acesso
    //     // caso contrário, exceção será lançada

    //     System.out.print("Digite a senha do administrador: ");
    //     String senhaInput = inputUsuario.nextLine();

    //     if(senhaInput.equals(this.senhaAdmin)) {
    //         this.acessoAdmin = true;
    //         System.out.println("Admnistrador acessado com sucesso!");
    //     } else {
    //         throw new IllegalArgumentException("Não foi possível acessar o administrador!");
    //     }
    // }

    // public void deslogarComoAdmin() {
    //     // método para deslogar o administrador
    //     // é necessário apenas setar this.acessoAdmin para false

    //     this.acessoAdmin = false;
    //     System.out.println("O admnistrador foi deslogado");
    // }

    // métodos para manipular itens

    public void adicionarItem(
        int codigo, 
        String descricao, 
        double precoCompra, 
        double precoVenda
    ) throws IllegalArgumentException {
        Item item = new Item(codigo, descricao, precoCompra, precoVenda, 1, 1);
        this.consulta.insertProduto(item);
    }
    
    public void adicionarItem(
        int codigo, 
        String descricao, 
        double precoCompra, 
        double precoVenda,
        int quantidade
    ) throws IllegalArgumentException {
        Item item = new Item(codigo, descricao, precoCompra, precoVenda, quantidade, 1);
        this.consulta.insertProduto(item);
    }

    public void adicionarItem(
        int codigo, 
        String descricao, 
        double precoCompra, 
        double precoVenda,
        int quantidade,
        int estoqueMinimo
    ) throws IllegalArgumentException {
        Item item = new Item(codigo, descricao, precoCompra, precoVenda, quantidade, estoqueMinimo);
        this.consulta.insertProduto(item);
    }

    public void adicionarItem(String nome, int quantidade) {

    }

    public ResultSet retornarEstoquePorNome() {
        // mostra os itens disponíveis no estoque por nome

        String sql = "SELECT codigo_produto, descricao, preco_venda, quantidade_comprada " +
                     "FROM cantinaifal.produto " +
                     "WHERE quantidade_comprada > 0 " +
                     "ORDER BY descricao";
        ResultSet result = this.consulta.selectQuery(sql);

        return result;
    }

    public ResultSet retornarEstoquePorQuantidade() {
        // mostra os itens disponíveis no estoque por quantidade

        String sql = "SELECT codigo_produto, descricao, preco_venda, quantidade_comprada " +
                     "FROM cantinaifal.produto " +
                     "WHERE quantidade_comprada > 0 " +
                     "ORDER BY quantidade_comprada DESC";
        ResultSet result = this.consulta.selectQuery(sql);

        return result;

    }
    
    public ResultSet retornarEstoqueEmBaixaQuantidade() {
        // método para retornar itens em quantidade abaixo de 50

        String sql = "SELECT codigo_produto, descricao, preco_venda, quantidade_comprada " +
                     "FROM cantinaifal.produto " +
                     "WHERE quantidade_comprada > 0 AND quantidade_comprada <= 50 " +
                     "ORDER BY quantidade_comprada DESC";
        ResultSet result = this.consulta.selectQuery(sql);

        return result;
    }

    public List<Integer> retornarListaCodigoProdutos() throws SQLException {
        ResultSet result = this.retornarEstoquePorNome();
        List<Integer> listaCodigos = new ArrayList<>();  
        
        while (result != null && result.next()) {
            listaCodigos.add(result.getInt(1));
        }
        return listaCodigos;
    }

    public int retornarQuantidadeProduto(int codigo) throws SQLException {
        ResultSet result = this.retornarEstoquePorQuantidade();

        int quantidade = 0;
        while (result != null && result.next()) {
            if(codigo == result.getInt(1)) {
                quantidade = result.getInt(4);
            }
        }
        return quantidade;
    }

    // public double retornarLucroResumo() throws NullPointerException {
    //     // método que passa por todos os itens baixados
    //     // os imprime, e calcula a soma de seus valores (lucro)
    //     // com base nos preços de venda dos produtos


    //     // caso nenhum item tenha sido baixado
    //     if(this.itensBaixados.size() == 0) {
    //         throw new NullPointerException("Não foi dado baixa em nenhum item no estoque...");
    //     }
        
    //     double totalLucro = 0;
    //     for(String item : itensBaixados.keySet()) {
    //         totalLucro += itensBaixados.get(item).get(0).getPrecoVenda() * itensBaixados.get(item).size();

    //         System.out.println(
    //             item + ": " +
    //             itensBaixados.get(item).size()
    //         );
    //     }

    //     return totalLucro;
    // }

    public void comprarItemNoEstoque(int codigoItemEscolhido, int quantidadeEscolhida) throws SQLException {
        // lógica que transfere os itens comprados de um mapa a outro
        
        String qrySelect =  "SELECT * FROM cantinaifal.produto " +
                            "WHERE codigo_produto = " + codigoItemEscolhido;
        ResultSet result = this.consulta.selectQuery(qrySelect);

        if(result != null && result.next()) {
            this.consulta.insertItemVendido(
                new ItemVendido(
                    this.gerarChaveUnica(5), 
                    result.getInt(1), 
                    result.getDouble(4), 
                    quantidadeEscolhida
                )
            );
        }
        int novaQuantidade = result.getInt(5) - quantidadeEscolhida;
        this.consulta.updateQuantidadeProduto(codigoItemEscolhido, novaQuantidade);

    }

    // public void baixarItens() {
    //     // baixa um item

    //     // primeiro for loop para transferir os itens de uma lista para outra
    //     for(String item : this.itensVendidos.keySet()) {
    //         ArrayList<Item> itensTransferidos = this.itensVendidos.get(item);
    //         this.itensBaixados.put(item, itensTransferidos);
    //     }
    //     // segundo for loop para remover os itens de itensVendidos
    //     // usando itensBaixados para iterar para evitar ConcurrentModificationException
    //     for(String item : this.itensBaixados.keySet()) {
    //         this.itensVendidos.remove(item);
    //     }
    // }

    private int gerarChaveUnica(int tamanho) {
        String id = "";
        Random rand = new Random();
        for (int i = 0; i < tamanho; i++) {
            id += rand.nextInt(10);
        }
        return Integer.parseInt(id);
    }
    
}
