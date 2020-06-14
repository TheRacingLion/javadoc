package main;

import jdbc.Database;
import model.*;
import view.UI;
import view.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Interface used to represent a App class method
 */
interface DbWorker { void doWork();}

/**
 * Controls both the UI and database sides by allowing them to interact in order complete certain tasks
 */
public class App {
    /** Main app instance */
    private static App __instance;

    /**
     * Get or create a app instance
     * @return A {@link App}
     */
    private static App getInstance() {
        if (__instance == null) __instance = new App();
        return __instance;
    }

    /**
     * Main application entry point
     * @param args App arguments if given
     */
    public static void main(String[] args) {
        getInstance().Run();
    }

    /**
     * Enum with all the menu options to show the user
     */
    public enum MenuOption {
        /** Exit option. Terminates the application. */
        Exit("Exit"),
        /** Add colaborator option. {@link App#adicionarColaborador()} */
        adicionarColaborador("Adicionar colaborador"),
        /** Alter volunteers program option. {@link App#alterarProgramaVoluntario()} ()} */
        alterarProgramaVoluntario("Alterar programa de um voluntário"),
        /** Cancel a short term program option. {@link App#cancelarProgramaCurtaDuracao()} ()} */
        cancelarProgramaCurtaDuracao("Cancelar programa de curta duração"),
        /** Present all contacts option. {@link App#apresentarContactos()} ()} */
        apresentarContactos("Apresentar todos os contactos"),
        /** Presents all colaborators option. {@link App#apresentarColaboradores()} ()} */
        apresentarColaboradores("Apresentar todos os colaboradores"),
        /** Present latest volunteers option. {@link App#apresentarVoluntariosUltimosAnos()} ()} */
        apresentarVoluntariosUltimosAnos("Apresentar o nome completo dos voluntários com idade inferior a 30 anos e que tenham realizado voluntariado nos últimos 3 anos");

        /** Menu option description */
        public String description;

        /**
         * Creates a menu option with a description
         *
         * @param description The option description to present to the user
         */
        MenuOption(String description) { this.description = description; }
    }

    /**
     * {@link HashMap} with the menu option to class methods mapping.
     */
    private HashMap<MenuOption,DbWorker> __dbMethods;

    /**
     * Main app method. Maps menu options to the corresponding class methods and adds them to a {@link HashMap}
     */
    private App() {
        __dbMethods = new HashMap<>();
        __dbMethods.put(MenuOption.adicionarColaborador, App.this::adicionarColaborador);
        __dbMethods.put(MenuOption.alterarProgramaVoluntario, App.this::alterarProgramaVoluntario);
        __dbMethods.put(MenuOption.cancelarProgramaCurtaDuracao, App.this::cancelarProgramaCurtaDuracao);
        __dbMethods.put(MenuOption.apresentarContactos, App.this::apresentarContactos);
        __dbMethods.put(MenuOption.apresentarColaboradores, App.this::apresentarColaboradores);
        __dbMethods.put(MenuOption.apresentarVoluntariosUltimosAnos, App.this::apresentarVoluntariosUltimosAnos);
    }

    /**
     * Runs the application. Shows a {@link UI#WELCOME_TEXT} welcome text and tests the connection to the database.
     * It then starts a loop for the main menu where the menu is displayed to the user and then input is awaited.
     * If the input is valid the corresponding menu option method is called.
     * If the input is a {@link MenuOption#Exit} menu option for exit then the program terminates.
     */
    private void Run() {
        System.out.println(UI.WELCOME_TEXT);

        boolean success = testDatabase();
        if (!success) return;

        int userInput;
        MenuOption option = null;
        boolean initialRun = true;
        do {
            if (!initialRun) UI.clearConsole();
            else initialRun = false;
            userInput = UI.displayMenu(
                    "Menu Inicial",
                    MenuOption.values().length,
                    index -> MenuOption.values()[index].description
            );
            if (userInput >= 0) {
                option = MenuOption.values()[userInput];
                UI.clearConsole();
                try {
                    __dbMethods.get(option).doWork();
                    UI.waitConfirm();
                } catch (NullPointerException ex) {
                    // Nothing to do. The option was not a valid one. Read another.
                }
            }
        } while (option != MenuOption.Exit);
        UI.printASCII("IT", "Aplicação Encerrada. Tenha um bom dia.");
    }

    /**
     * Tests the connection to the database. Also shows some connection information if it is valid.
     *
     * @return {@code Boolean} indicating if the database connection is valid
     */
    private boolean testDatabase() {
        String[] info = Database.testConnection();
        if (info != null) {
            UI.printASCII("INF", "Conexão de teste á base de dados bem-sucedida:");
            System.out.println("- Driver: " + info[0]);
            System.out.println("- Host: " + info[1]);
            System.out.println("- Nome: " + info[2]);
            return true;
        } else {
            System.err.println("Conexão de teste á base de dados falhou. Por favor verifique as suas credenciais.");
            return false;
        }
    }

    /**
     * Adds a colaborator.
     * Asks:
     *  - If the colaborator receives a paycheck or is a volunteer.
     *  - All the colaborator information
     *  - The paycheck colaborator or volunteer information
     *  - All the normal or emergency contacts
     *  - The insurance information
     *
     *  {@link Database#adicionarColaborador(COLABORADOR, SEGURO, VOLUNTARIO, ASSALARIADO, ArrayList, ArrayList)}
     */
    private void adicionarColaborador() {
        UI.printPromptStart("Registo de um novo colaborador:");

        String TIPO_COLABORADOR = UI.promptMultipleChoice(
                "Tipo de Colaborador:",
                new String[]{"Assalariado", "Voluntário"});
        if (TIPO_COLABORADOR == null) return;

        ArrayList<PROGRAMA> programas = null;
        if (TIPO_COLABORADOR.equals("Voluntário")) {
            programas = Database.listarProgramas(true, "PCD");
            if (programas == null || programas.isEmpty()) {
                UI.printASCII("CROSS", "De momento não há programas disponiveis.");
                return;
            }
        }

        /*
          :START COLABORADOR
         */
        System.out.println();
        UI.printASCII("IT", "Informação do Colaborador:");

        String nome = UI.prompt(
                "Nome:",
                (str) -> Validator.isLength(str, 15),
                "O nome do colaborador não deve passar os 15 characteres.");
        if (nome == null) return;

        String apelido = UI.prompt(
                "Apelido:",
                (str) -> Validator.isLength(str,30),
                "O apelido do colaborador não deve passar os 15 characteres.");
        if (apelido == null) return;

        String dtnascimento = UI.prompt(
                "Data de Nascimento (Formato yyyy-MM-dd):",
                (str) -> Validator.isDate(str, "1960-01-01"),
                "Data tem de estar no formato yyyy-MM-dd e maior do que 1960-01-01.");
        if (dtnascimento == null) return;

        String nident = UI.prompt(
                "Número de Identificação: ",
                (str) -> Validator.isLength(str,11),
                "O número de identificação do colaborador não deve passar os 11 characteres.");
        if (nident == null) return;

        String[] TIPOID = new String[]{"CC", "BI", "Passaporte"};
        String tipoidInput = UI.promptMultipleChoice("Tipo de Identificação:", TIPOID);
        if (tipoidInput == null) return;
        int tipoid = Arrays.asList(TIPOID).indexOf(tipoidInput) + 1;

        String nfiscal = UI.prompt(
                "Número Fiscal (opcional):",
                (str) -> Validator.isLength(str,9),
                "O número fiscal do colaborador não deve passar os 9 characteres.",
                true);

        String nacionalidade = UI.prompt(
                "Nacionalidade:",
                (str) -> Validator.isLength(str,20),
                "O nacionalidade do colaborador não deve passar os 20 characteres.");
        if (nacionalidade == null) return;

        String morada = UI.prompt(
                "Morada:",
                (str) -> Validator.isLength(str,250),
                "O morada do colaborador não deve passar os 250 characteres.");
        if (morada == null) return;

        COLABORADOR colaborador = new COLABORADOR(nome, apelido, dtnascimento, nident, tipoid, nfiscal, nacionalidade, morada);
        /*
          :END COLABORADOR
         */

        VOLUNTARIO voluntario = null;
        ASSALARIADO assalariado = null;
        if (TIPO_COLABORADOR.equals("Voluntário")) {
            System.out.println();
            UI.printASCII("IT", "Informação do Voluntário:");

            String ocupacaoactual = UI.promptMultipleChoice(
                    "Ocupação atual:",
                    new String[]{"estudante", "empregado", "desempregado"});
            if (ocupacaoactual == null) return;

            String idioma = UI.prompt(
                    "Idioma:",
                    (str) -> Validator.isLength(str,250),
                    "O idioma do colaborador não deve passar os 250 characteres.");
            if (idioma == null) return;

            String programaId = selecionarPrograma(programas);

            voluntario = new VOLUNTARIO(ocupacaoactual, idioma, programaId);
        } else if (TIPO_COLABORADOR.equals("Assalariado")) {
            System.out.println();
            UI.printASCII("IT", "Informação do Assalariado:");

            String cargo = UI.prompt(
                    "Cargo:",
                    (str) -> Validator.isLength(str,50),
                    "O cargo do assalariado não deve passar os 50 characteres.");
            if (cargo == null) return;

            String vencimento = UI.prompt(
                    "Vencimento:",
                    (str) -> Validator.isDecimal(str, 8, 2),
                    "O vencimento do colaborador deve ser um numero decimal de até 8 digitos e até duas casas decimais.");
            if (vencimento == null) return;

            assalariado = new ASSALARIADO(cargo, vencimento);
        }

        /*
          :START CONTACTOS
         */
        System.out.println();
        UI.printASCII("IT", "Informação dos Contactos:");

        ArrayList<CONTACTO> contactos = new ArrayList<>();

        boolean nextContact = true;
        int noordemContact = 1;
        while (nextContact) {
            System.out.println();
            UI.printASCII("IT", "Criar contacto de colaborador:");
            System.out.println(" - Contacto com prioridade mais elevada primeiro.");

            String contacto = UI.prompt(
                    "Contacto:",
                    (str) -> Validator.isEmailOrPhone(str),
                    "O contacto deve ser um email ou número de telefone válido.");
            if (contacto == null) return;

            String descricao = UI.prompt(
                    "Descrição:",
                    (str) -> Validator.isLength(str,50),
                    "O descricao do contacto não deve passar os 50 characteres.");
            if (descricao == null) return;

            contactos.add(new CONTACTO(noordemContact, contacto, descricao));

            nextContact = UI.confirm("Adicionar outro contacto de colaborador?");
            if (nextContact) noordemContact++;
        }

        ArrayList<CONTACTOEMERGENCIA> contactos_emergencia = new ArrayList<>();
        if (TIPO_COLABORADOR.equals("Voluntário")) {
            boolean nextContactEmergencia = true;
            int noordemContactoEmergencia = noordemContact + 1;
            while (nextContactEmergencia) {
                System.out.println();
                UI.printASCII("IT", "Criar contacto de emergência do voluntário:");

                String contacto_emergencia = UI.prompt(
                        "Contacto:",
                        (str) -> Validator.isEmailOrPhone(str),
                        "O contacto deve ser um email ou número de telefone válido.");
                if (contacto_emergencia == null) return;

                String descricao_emergencia = UI.prompt(
                        "Descrição:",
                        (str) -> Validator.isLength(str,50),
                        "O descricao do contacto de emergência não deve passar os 50 characteres.");
                if (descricao_emergencia == null) return;

                String contacto_emergencia_nome = UI.prompt(
                        "Nome:",
                        (str) -> Validator.isLength(str,50),
                        "O nome do contacto de emergência não deve passar os 50 characteres.");
                if (contacto_emergencia_nome == null) return;

                String contacto_emergencia_grauparentesco = UI.prompt(
                        "Grau de Parentesco:",
                        (str) -> Validator.isLength(str, 15),
                        "O Grau de parentensco do contacto de emergência não deve passar os 15 characteres.");
                if (contacto_emergencia_grauparentesco == null) return;

                String contacto_emergencia_contacto = UI.prompt(
                        "Contacto de Emergência:",
                        (str) -> Validator.isEmailOrPhone(str),
                        "O contacto deve ser um email ou número de telefone válido.");
                if (contacto_emergencia_contacto == null) return;

                contactos.add(new CONTACTO(noordemContactoEmergencia, contacto_emergencia, descricao_emergencia));
                contactos_emergencia.add(new CONTACTOEMERGENCIA(contacto_emergencia_nome, contacto_emergencia_grauparentesco, contacto_emergencia_contacto, noordemContactoEmergencia));

                nextContactEmergencia = UI.confirm("Adicionar outro contacto de emergência do voluntário?");
                if (nextContactEmergencia) noordemContactoEmergencia++;
            }
        }
        /*
          :END CONTACTOS
         */

        /*
          :START SEGURO
         */
        System.out.println();
        UI.printASCII("IT", "Informação do Seguro:");

        String data = UI.prompt(
                "Data do Seguro (Formato yyyy-MM-dd):",
                (str) -> Validator.isDate(str),
                "A data tem de estar no formato yyyy-MM-dd.");
        if (data == null) return;

        String descricao = UI.prompt(
                "Descrição do Seguro:",
                (str) -> Validator.isLength(str, 150),
                "A descricao do seguro não deve passar os 150 characteres.");
        if (descricao == null) return;

        String premio = UI.prompt(
                "Prémio:",
                (str) -> Validator.isDecimal(str, 8, 2),
                "O prémio do seguro deve ser um numero decimal de até 8 digitos e até duas casas decimais.");
        if (premio == null) return;

        String prazo = UI.promptMultipleChoice(
                "Prazo:",
                new String[]{"permanente", "temporario"});
        if (prazo == null) return;

        String duracao = UI.prompt(
                "Duração:",
                (str) -> Validator.isInteger(str, true),
                "A duracao do seguro deve ser um número positivo.");
        if (duracao == null) return;

        SEGURO seguro = new SEGURO(data, descricao, premio, prazo, duracao);

        /*
          :END SEGURO
         */

        boolean success = Database.adicionarColaborador(
                colaborador,
                seguro,
                voluntario,
                assalariado,
                contactos,
                contactos_emergencia
        );
        if (success) {
            UI.printASCII("BS", "Registo do Colaborador realizado com exito!");
        } else {
            UI.printASCII("CROSS", "Não foi possivel realizar o registo do Colaborador!");
        }
    }

    /**
     * Asks the user to select a program. Displays a multiple choice menu with all the programs and waits for input.
     *
     * @param programas A {@code ArrayList} of {@link PROGRAMA} models
     * @return {@code String} with the program ID selected
     */
    private String selecionarPrograma(ArrayList<PROGRAMA> programas) {
        String programa;
        String[] programDisplayStrings = new String[programas.size()];
        for (int i = 0; i < programas.size(); i++) programDisplayStrings[i] = programas.get(i).toString();
        programa = UI.promptMultipleChoice("Selecione um programa:", programDisplayStrings);
        if (programa == null) return null;
        return programas.get(Arrays.asList(programDisplayStrings).indexOf(programa)).identificador;
    }

    /**
     * Alter volunteer program. Asks for the volunteer identification number and the new program.
     *
     * {@link Database#alterarProgramaVoluntario(String, String)}
     */
    private void alterarProgramaVoluntario() {
        UI.printPromptStart("Alterar o programa de um voluntário:");

        ArrayList<PROGRAMA> programas = Database.listarProgramas(true, "PCD");
        if (programas == null || programas.isEmpty()) {
            UI.printASCII("CROSS", "De momento não há programas disponiveis.");
            return;
        }

        String nident = UI.prompt(
                "Número de Identificação do Voluntário: ",
                (str) -> Validator.isLength(str,11),
                "O número de identificação do voluntário não deve passar os 11 characteres.");
        if (nident == null) return;

        String programaId = selecionarPrograma(programas);

        boolean success = Database.alterarProgramaVoluntario(nident, programaId);
        if (success) {
            UI.printASCII("BS", "O voluntario foi adicionado com sucesso ao programa!");
        } else {
            UI.printASCII("CROSS", "Não foi possivel realizar o registo do voluntario no novo programa!");
        }
    }

    /**
     * Cancels a future program. Asks for the program to cancel.
     *
     * {@link Database#cancelarProgramaCurtaDuracao(String)}
     */
    private void cancelarProgramaCurtaDuracao() {
        UI.printPromptStart("Cancelar programa de curta duração:");

        ArrayList<PROGRAMA> programas = Database.listarProgramas(true, "PCD");
        if (programas == null || programas.isEmpty()) {
            UI.printASCIIError("CROSS", "De momento não há programas disponiveis.");
            return;
        }
        String programaId = selecionarPrograma(programas);

        boolean success = Database.cancelarProgramaCurtaDuracao(programaId);
        if (success) {
            UI.printASCII("BS", "O programa foi cancelado com sucesso!");
        } else {
            UI.printASCII("CROSS", "Não foi possivel cancelar o programa!");
        }
    }

    /**
     * Shows all contacts.
     * Asks type of contact (normal or emergency), time filter (last 6 months or 1 year) and contact filter to show (email, phone or both).
     *
     * {@link Database#apresentarContactos(String, String, String)}
     */
    private void apresentarContactos() {
        UI.printPromptStart("Apresentar todos os contactos:");

        String tipo = UI.promptMultipleChoice(
                "Tipo do Contacto:",
                new String[]{"Normal", "Emergência"});
        if (tipo == null) return;

        String tempo = UI.promptMultipleChoice(
                "Registados:",
                new String[]{"Nos últimos 6 meses", "No último ano"});
        if (tempo == null) return;

        String filter = UI.promptMultipleChoice(
                "Apresentar:",
                new String[]{"Emails", "Telefones", "Ambos"});
        if (filter == null) return;

        Database.apresentarContactos(tipo, tempo, filter);
    }

    /**
     * Shows all colaborators. Either people with paycheck or volunteers. Asks for the colaborator type.
     *
     * {@link Database#apresentarColaboradores(String)}
     */
    private void apresentarColaboradores() {
        UI.printPromptStart("Apresentar todos os colaboradores:");

        String tipoColaborador = UI.promptMultipleChoice(
                "Tipo de Colaborador:",
                new String[]{"Assalariado", "Voluntário"});
        if (tipoColaborador == null) return;

        Database.apresentarColaboradores(tipoColaborador);
    }

    /**
     * Shows the volunteers with age under 30 and that have been in a program in the last 3 years
     *
     * {@link Database#apresentarVoluntariosUltimosAnos()}
     */
    private void apresentarVoluntariosUltimosAnos() {
        UI.printPromptStart("Apresentar o nome completo dos voluntários com idade inferior a 30 anos e que tenham realizado voluntariado nos últimos 3 anos: ");

        Database.apresentarVoluntariosUltimosAnos();
    }

    /*
        MENU:
        1. Adicionar Colaborador
              1. Voluntário
                    1)Pedir infos das tabelas colab e voluntario
                    2) pedir info para contacto
                    3)Adicionar outro contacto contacto de colaborador? (s/n)
                        Até a resposta ser não
                    4) pedir info p contacto de emergencia
                    5) Adcionar  outro contacto de emergencia? (s/n)
                         Até resposta ser não
                    6) Pedir info seguro
                    7) "O voluntario x foi adicionado com sucesso"
              2. Assalariado
                    1)Pedir infos das tabelas colab e assalariado
                    2)Pedir info contacto
                    3) Adicionar outro contacto? (s/n)
                        Até a resposta ser não
                    4) Pedir info seguro
                    5) "O colaborador assalariado x foi adicionado com sucesso"
        2. Alterar escolha do programa de um voluntário
              1) Pedir numero de identificação de id
              2) Mostrar lista de programas disponiveis, com data de inicio superior à data atual
              3) Alterar
              4) "O voluntario x foi adicionado com sucesso ao programa x"
        3. Cancelar programa de curta duração
              1) Mostrar lista de programas de curta duraçao cujo a data de inicio é superior à atual
              2) Eliminar, por ordem, da tabela FORMAÇÃO, SEGURO, CONTACTO DE EMERGENCIA, CONTACTO, VOLUNTARIO, COLABORADOR, todos os voluntarios associados.
              3) Eliminar, por ordem, da tabela IDIOMA, PCURTADURAÇAO, PROGRAMA o tuplo associado ao programa.
              4) mensagem de "Programa Eliminado com sucesso"
        4. Apresentar todos os contactos
             1. De colaborador
             2. De emergência
                       1. emails
                       2. telefones
                       3. emails e telefones
                           1. Registados nos ultimos 6 meses
                           2. Registados no ultimo ano
        5. Obter o número, o nome completo e a idade de todos os colaboradores
                    1. Voluntários
                    2. Assalariados
        6. Apresentar o nome completo dos voluntários com idade inferior a 30 anos e que tenham realizado voluntariado nos últimos 3 anos
     */
}
