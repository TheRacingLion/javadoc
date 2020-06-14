package jdbc;

import model.*;
import view.UI;

import java.sql.*;
import java.util.ArrayList;

/**
 * Handles all actions to the SQL Database such as creation, editing or removal of information.
 */
public class Database {
    /**
     * Test the connection to the database by checking if the {@code Connection} is valid.
     *
     * @return A {@code String[]} array with some info about the connection like URL and name. Null if not valid.
     */
    public static String[] testConnection() {
        Connection con = Driver.getConnection();
        if (con != null) {
            try {
                DatabaseMetaData dmd = con.getMetaData();
                String[] info = new String[]{dmd.getDatabaseProductName(), dmd.getURL().split(";")[0], con.getCatalog()};
                DBUtils.closeQuietly(con);
                return info;
            } catch (final SQLException e) { return null; }
        }
        return null;
    }

    /**
     * Get the list of programs
     *
     * @param ativos {@code boolean} indicating if to return only active programs
     * @param filter {@code String} filter saying if the program is short or long term
     * @return A {@code ArrayList} of {@code PROGRAMA}
     */
    public static ArrayList<PROGRAMA> listarProgramas(boolean ativos, String filter) {
        Connection con = Driver.getConnection();

        String SELECT_ALL_PROGRAMS;
        if (ativos) {
            SELECT_ALL_PROGRAMS = "select * from PROGRAMA where datainicio > CAST(GETDATE() AS DATE) " +
                                        (filter != null ? "AND atrdiscriminante = " + filter : "");
        } else {
            SELECT_ALL_PROGRAMS = "select * from PROGRAMA " + (filter != null ? "where atrdiscriminante = " + filter : "");
        }

        PreparedStatement listPrograms = null;
        ResultSet rs = null;
        ArrayList<PROGRAMA> programas = new ArrayList<>();
        try {
            listPrograms = con.prepareStatement(SELECT_ALL_PROGRAMS);
            rs = listPrograms.executeQuery();
            while (rs.next()) {
                programas.add(new PROGRAMA(rs));
            }
            return programas;
        } catch (SQLException e) {
            // Do nothing
        } finally {
            DBUtils.closeQuietly(con, listPrograms, rs);
        }
        return null;
    }

    /**
     * Get the intervention area for a certain code
     *
     * @param codigo The area code {@code String}
     * @return The intervention area {@code String}
     */
    public static String getAreaIntervencao(String codigo) {
        Connection con = Driver.getConnection();

        String SELECT_AREA_INTERVENCAO_WHERE_CODIGO = "select areasintervencao from AREAINTERVENCAO where codigo = ?";

        PreparedStatement selectAreaIntervencao = null;
        ResultSet rs = null;
        String areaintervencao = null;
        try {
            selectAreaIntervencao = con.prepareStatement(SELECT_AREA_INTERVENCAO_WHERE_CODIGO);
            selectAreaIntervencao.setString(1, codigo);
            rs = selectAreaIntervencao.executeQuery();

            if (rs.next()) areaintervencao = rs.getString(1);
            if (areaintervencao != null) return areaintervencao.trim();
            return null;
        } catch (SQLException e) {
            // Do nothing
        } finally {
            DBUtils.closeQuietly(con, selectAreaIntervencao, rs);
        }
        return null;
    }

    /**
     * Add a colaborator to the Database
     *
     * @param colaborador {@code COLABORATOR} object
     * @param seguro {@code SEGURO} object
     * @param voluntario {@code VOLUNTARIO} object
     * @param assalariado {@code ASSALARIADO} object
     * @param contactos {@code ArrayList} of {@code CONTACTO}
     * @param contactos_emergencia {@code ArrayList} of {@code CONTACTOEMERGENCIA}
     * @return {@code boolean} indicating if it was successful
     */
    public static boolean adicionarColaborador(
            COLABORADOR colaborador,
            SEGURO seguro,
            VOLUNTARIO voluntario,
            ASSALARIADO assalariado,
            ArrayList<CONTACTO> contactos,
            ArrayList<CONTACTOEMERGENCIA> contactos_emergencia
    ) {
        Connection con = Driver.getConnection();

        String ADICIONAR_COLABORADOR = "insert into COLABORADOR (nome, apelido, dtnascimento, nident, tipoid, nfiscal, nacionalidade, morada) values (?,?,?,?,?,?,?,?)";
        String ADICIONAR_SEGURO = "insert into SEGURO (data, pessoa, descricao, premio, prazo, duracao) values (?,?,?,?,?,?)";
        String ADICIONAR_VOLUNTARIO = "insert into VOLUNTARIO (nocolaborador, ocupacaoatual, idioma, idprograma) values (?,?,?,?)";
        String ADICIONAR_ASSALARIADO = "insert into ASSALARIADO (nocolaborador, cargo, vencimento) values (?,?,?)";
        String ADICIONAR_CONTACTO = "insert into CONTACTO (idtitular, noordem, contacto, descricao) values (?,?,?,?)";
        String ADICIONAR_CONTACTO_EMERGENCIA = "insert into CONTACTOEMERGENCIA (nome, grauparentesco, contacto, idtitular, noordem) values (?,?,?,?,?)";

        PreparedStatement adicionarColaborador = null;
        PreparedStatement adicionarSeguro = null;
        PreparedStatement adicionarVoluntario = null;
        PreparedStatement adicionarAssalariadoo = null;
        PreparedStatement adicionarContacto = null;
        PreparedStatement adicionarContactoEmergencia = null;
        try {
            con.setAutoCommit(false);

            adicionarColaborador = con.prepareStatement(ADICIONAR_COLABORADOR, Statement.RETURN_GENERATED_KEYS);
            colaborador.fillInPreparedStatement(adicionarColaborador, 1);

            int nocolaborador;
            int affectedRows = adicionarColaborador.executeUpdate();
            if (affectedRows == 0) throw new SQLException(); // Rollback if no rows were affected
            try (ResultSet generatedKeys = adicionarColaborador.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    nocolaborador = generatedKeys.getInt(1);
                } else throw new SQLException();
            }

            adicionarSeguro = con.prepareStatement(ADICIONAR_SEGURO);
            seguro.setNoColaborador(nocolaborador);
            seguro.fillInPreparedStatement(adicionarSeguro, 1);
            adicionarSeguro.executeUpdate();

            if (assalariado == null) {
                adicionarVoluntario = con.prepareStatement(ADICIONAR_VOLUNTARIO);
                voluntario.setNoColaborador(nocolaborador);
                voluntario.fillInPreparedStatement(adicionarVoluntario, 1);
                adicionarVoluntario.executeUpdate();
            } else {
                adicionarAssalariadoo = con.prepareStatement(ADICIONAR_ASSALARIADO);
                assalariado.setNoColaborador(nocolaborador);
                assalariado.fillInPreparedStatement(adicionarAssalariadoo, 1);
                adicionarAssalariadoo.executeUpdate();
            }

            adicionarContacto = con.prepareStatement(ADICIONAR_CONTACTO);
            for (CONTACTO contacto : contactos) {
                contacto.setNoColaborador(nocolaborador);
                contacto.fillInPreparedStatement(adicionarContacto, 1);
                adicionarContacto.addBatch();
                adicionarContacto.clearParameters();
            }
            adicionarContacto.executeBatch();

            adicionarContactoEmergencia = con.prepareStatement(ADICIONAR_CONTACTO_EMERGENCIA);
            for (CONTACTOEMERGENCIA contacto_emergencia : contactos_emergencia) {
                contacto_emergencia.setNoColaborador(nocolaborador);
                contacto_emergencia.fillInPreparedStatement(adicionarContactoEmergencia, 1);
                adicionarContactoEmergencia.addBatch();
                adicionarContactoEmergencia.clearParameters();
            }
            adicionarContactoEmergencia.executeBatch();

            con.commit();
            return true;
        } catch (SQLException e) {
            return DBUtils.handleExecuteUpdateException(con);
        } finally {
            DBUtils.closeQuietly(adicionarColaborador);
            DBUtils.closeQuietly(adicionarSeguro);
            DBUtils.closeQuietly(adicionarVoluntario);
            DBUtils.closeQuietly(adicionarAssalariadoo);
            DBUtils.closeQuietly(adicionarContacto);
            DBUtils.closeQuietly(adicionarContactoEmergencia);
            DBUtils.closeQuietly(con);
        }
    }

    /**
     * Alters a volunteer program
     *
     * @param nident Identification number of the volunteer as a {@code String}
     * @param programaId New program ID {@code String}
     * @return {@code boolean} indicating if it was successful
     */
    public static boolean alterarProgramaVoluntario(String nident, String programaId) {
        Connection con = Driver.getConnection();

        String ALTERAR_PROGRAMA_VOLUNTARIO =
                "UPDATE VOLUNTARIO SET idprograma = ? WHERE nocolaborador IN (" +
                        "SELECT nocolaborador FROM COLABORADOR WHERE nident = ?" +
                        ")";

        PreparedStatement alterarProgramaVoluntario = null;
        try {
            con.setAutoCommit(false);
            alterarProgramaVoluntario = con.prepareStatement(ALTERAR_PROGRAMA_VOLUNTARIO);

            alterarProgramaVoluntario.setString(1, programaId);
            alterarProgramaVoluntario.setString(2, nident);

            alterarProgramaVoluntario.executeUpdate();
            con.commit();
            return true;
        } catch (SQLException e) {
            return DBUtils.handleExecuteUpdateException(con);
        } finally {
            DBUtils.closeQuietly(con, alterarProgramaVoluntario, null);
        }
    }

    /**
     * Cancels a future program
     *
     * @param programaId Program ID {@code String} to cancel
     * @return {@code boolean} indicating if it was successful
     */
    public static boolean cancelarProgramaCurtaDuracao(String programaId) {
        Connection con = Driver.getConnection();

        String DELETE_VOLUNTARIOS = "delete from COLABORADOR where nocolaborador IN (SELECT nocolaborador from VOLUNTARIO where idprograma = ?)";
        String DELETE_PROGRAMA = "delete from PROGRAMA where identificador = ?";

        PreparedStatement deleteVoluntarios = null;
        PreparedStatement deletePrograma = null;
        try {
            con.setAutoCommit(false);
            deleteVoluntarios = con.prepareStatement(DELETE_VOLUNTARIOS);
            deleteVoluntarios.setString(1, programaId);
            deleteVoluntarios.executeUpdate();

            deletePrograma = con.prepareStatement(DELETE_PROGRAMA);
            deletePrograma.setString(1, programaId);
            deletePrograma.executeUpdate();

            con.commit();
            return true;
        } catch (SQLException e) {
            return DBUtils.handleExecuteUpdateException(con);
        } finally {
            DBUtils.closeQuietly(deleteVoluntarios);
            DBUtils.closeQuietly(deletePrograma);
            DBUtils.closeQuietly(con);
        }
    }

    /**
     * Pad right function. Fills in the right side of a string with spaces.
     *
     * @param s The {@code String} to pad
     * @param n A {@code int} with the max length of the string with the pads
     * @return The padded {@code String}
     */
    private static String padRight(String s, int n) { return String.format("%-" + (n > 25 ? 27 : n + 2) + "s", s); }

    /**
     * Formats and prints the results of a {@code ResultSet} to the console.
     *
     * @param rs The {@code ResultSet} to print.
     */
    private static void printResults(ResultSet rs) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            boolean isFirst = true;
            while (rs.next()) {
                if (isFirst) {
                    System.out.print(view.ASCII.BV + " ");
                    int totalPad = 0;
                    for (int i = 1; i <= columnsNumber; i++) {
                        int displaySize = rsmd.getColumnDisplaySize(i);
                        totalPad += (displaySize > 25 ? 27 : displaySize + 2);
                        System.out.print(padRight(rsmd.getColumnLabel(i), displaySize));
                    }
                    System.out.println("\n" + view.ASCII.BVR + String.format("%0" + (totalPad + 1) + "d", 0).replace("0", view.ASCII.BH.toString()));
                    isFirst = false;
                }
                System.out.print(view.ASCII.BV + " ");
                for (int i = 1; i <= columnsNumber; i++) {
                    System.out.print(padRight(rs.getString(i), rsmd.getColumnDisplaySize(i)));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            // Do nothing
        }
    }

    /**
     * Get and print all contacts from the CONTACTO and CONTACTOEMERGENCIA tables.
     *
     * @param tipo {@code String} with the type of contact (normal or emergency)
     * @param tempo {@code String} time filter (last 6 months or 1 year)
     * @param filter {@code String} contact filter to show (email, phone or both)
     */
    public static void apresentarContactos(String tipo, String tempo, String filter) {
        Connection con = Driver.getConnection();

        String SELECT_TIPO = tipo.equals("Emergência") ? "" : "NOT";
        String SELECT_TEMPO = tempo.equals("Nos últimos 6 meses") ? "-6" : "-12";
        String SELECT_FILTER = "(descricao = 'email' OR descricao = 'telefone')";
        switch (filter) {
            case "Emails": SELECT_FILTER = "descricao = 'email'"; break;
            case "Telefones": SELECT_FILTER = "descricao = 'telefone'"; break;
        }

        String SELECT_CONTACTOS =
                "select * from CONTACTO C " +
                "where " + SELECT_TIPO + " EXISTS (" +
                      "select idtitular, noordem from CONTACTOEMERGENCIA CE " +
                      "where C.idtitular = CE.idtitular AND C.noordem = CE.noordem" +
                ") AND EXISTS (" +
                      "select idtitular from SEGURO S " +
                      "where C.idtitular = S.pessoa AND S.data >= dateadd(month, " + SELECT_TEMPO + ", getdate())" +
                ") AND " + SELECT_FILTER;

        PreparedStatement contactos = null;
        ResultSet rs = null;
        try {
            contactos = con.prepareStatement(SELECT_CONTACTOS);
            rs = contactos.executeQuery();
            if (!rs.isBeforeFirst() ) {
                UI.printASCII("IT", "Não há contactos que satisfaçam as condições.");
                return;
            }
            printResults(rs);
        } catch (SQLException e) {
            // Do nothing
        } finally {
            DBUtils.closeQuietly(con, contactos, rs);
        }
    }

    /**
     * Get and print all colaborators from the COLABORADOR, ASSALARIADO and VOLUNTARIO tables.
     *
     * @param tipoColaborador {@code String} Type of colaborator to show. Either "Assalariado" or "Voluntário".
     */
    public static void apresentarColaboradores(String tipoColaborador) {
        Connection con = Driver.getConnection();

        String TABLE_NAME = tipoColaborador.equals("Assalariado") ? "ASSALARIADO" : "VOLUNTARIO";
        String SELECT_COLABORADORES =
                "select nocolaborador as 'Nr. Colab', nome as 'Nome', apelido as 'Apelido', dtnascimento as 'Data Nascimento' " +
                "from COLABORADOR where nocolaborador IN (" +
                    "select nocolaborador from " + TABLE_NAME +
                ")";

        PreparedStatement colaboradores = null;
        ResultSet rs = null;
        try {
            colaboradores = con.prepareStatement(SELECT_COLABORADORES);
            rs = colaboradores.executeQuery();
            if (!rs.isBeforeFirst() ) {
                UI.printASCII("IT", "Não existem colaboradores para apresentar.");
                return;
            }
            printResults(rs);
        } catch (SQLException e) {
            // Do nothing
        } finally {
            DBUtils.closeQuietly(con, colaboradores, rs);
        }
    }

    /**
     * Get and print all volunteers under 30 and the have been in a program in the last 3 years
     */
    public static void apresentarVoluntariosUltimosAnos()  {
        Connection con = Driver.getConnection();

        String SELECT_VOLUNTARIOS_ULTIMOS_ANOS =
                "SELECT nome as 'Nome', apelido as 'Apelido' " +
                "FROM COLABORADOR " +
                "WHERE nocolaborador IN (" +
                    "SELECT nocolaborador FROM VOLUNTARIO " +
                    "WHERE idprograma IN (" +
                        "SELECT identificador FROM PROGRAMA " +
                        "WHERE (datafinal < GETDATE() AND YEAR(datafinal) > YEAR(GETDATE()) - 3))" +
                ") AND CAST(YEAR(GETDATE()) - YEAR(dtnascimento) as int) <= 30";

        PreparedStatement voluntariosUltimosAnos = null;
        ResultSet rs = null;
        try {
            voluntariosUltimosAnos = con.prepareStatement(SELECT_VOLUNTARIOS_ULTIMOS_ANOS);
            rs = voluntariosUltimosAnos.executeQuery();
            if (!rs.isBeforeFirst() ) {
                UI.printASCII("IT", "Não há voluntários que satisfaçam as condições.");
                return;
            }
            printResults(rs);
        } catch (SQLException e) {
            // Do nothing
        } finally {
            DBUtils.closeQuietly(con, voluntariosUltimosAnos, rs);
        }
    }
}
