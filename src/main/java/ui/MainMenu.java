package ui;

import br.com.erikferreira.persistence.entity.BoardColumnEntity;
import br.com.erikferreira.persistence.entity.BoardColumnKindEnum;
import br.com.erikferreira.persistence.entity.BoardEntity;
import br.com.erikferreira.service.BoardQueryService;
import br.com.erikferreira.service.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static br.com.erikferreira.persistence.config.ConnectionConfig.getConnection;

public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);

    public void execute() throws SQLException {
        System.out.println("Bem vindo ao gerenciador de boards!");
        System.out.println("Escolha uma opção:");
        var option = -1;
        while(true){
            System.out.println("1 - Criar novo board");
            System.out.println("2 - Acessar board existente");
            System.out.println("3 - Deletar board existente");
            System.out.println("4 - Sair");
            option = scanner.nextInt();
            switch (option){
                case 1 -> {
                    createBoardMenu();
                }
                case 2 -> {
                    accessBoardMenu();
                }
                case 3 -> {
                    deleteBoardMenu();
                }
                case 4 -> {
                    System.out.println("Saindo...");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida!");
            }

        }
    }

    private void createBoardMenu() {
        var entity = new BoardEntity();
        System.out.println("Informe o nome do board:");
        entity.setName(scanner.next());

        System.out.println("Seu board tem mais de 3 colunas? se sim dgite quantas, se não digite 0");
        var hasMoreThan3Columns = scanner.nextInt();

        List<BoardColumnEntity> columns = new ArrayList<>();

        System.out.println("Informe o nome da coluna inicial:");
        var initialColumnName = scanner.next();
        var initialColumn = createColumn(initialColumnName, BoardColumnKindEnum.INITIAL, 0);
        columns.add(initialColumn);


        for (var i = 0; i < hasMoreThan3Columns; i++) {
            System.out.println("Informe o nome da coluna de tarefa pendente: ");
            var pendingColumnName = scanner.next();
            var pendingColumn = createColumn(pendingColumnName, BoardColumnKindEnum.PENDING, i + 1);
            columns.add(pendingColumn);
        }

        System.out.println("Informe o nome da coluna final: ");
        var finalColumnName = scanner.next();
        var finalColumn = createColumn(finalColumnName, BoardColumnKindEnum.FINAL, hasMoreThan3Columns + 1);
        columns.add(finalColumn);

        System.out.println("Informe o nome da coluna de cancelamento: ");
        var cancelColumnName = scanner.next();
        var cancelColumn = createColumn(cancelColumnName, BoardColumnKindEnum.CANCEL, hasMoreThan3Columns + 2);
        columns.add(cancelColumn);

        entity.setBoardColumns(columns);

        try (var connection = getConnection()) {
            var service = new BoardService(connection);
            var created = service.insert(entity);
            System.out.println("Board criado com sucesso! ID: " + created.getId());
        } catch (SQLException e) {
            System.out.println("Erro ao criar board: " + e.getMessage());
        }
    }


    private void accessBoardMenu() throws SQLException {
        System.out.println("Informe o ID do board que deseja acessar:");
        var id = scanner.nextLong();
        try (var connection = getConnection()) {
            var service = new BoardQueryService(connection);
            var optional = service.findById(id);
            optional.ifPresentOrElse(
                    b -> new BoardMenu(b).execute(),
                    () -> System.out.println("Nenhum board encontrado!")
            );
        }
    }
        private void deleteBoardMenu() throws SQLException {
        System.out.println("Informe o ID do board que deseja deletar:");
        var id = scanner.nextLong();
        try(var connection = getConnection()) {
            var service = new BoardService(connection);
            if(service.delete(id)){
                System.out.println("Board deletado com sucesso!");
            } else {
                System.out.println("Board não encontrado!");
            }
        }
    }

    private BoardColumnEntity createColumn(final String name, final BoardColumnKindEnum kind, final int orderIndex) {
        var column = new BoardColumnEntity();
        column.setName(name);
        column.setKind(kind);
        column.setOrderIndex(orderIndex);
        return column;
    }

}
