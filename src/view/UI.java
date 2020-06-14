package view;

import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Main UI (User Interface) class by using the {@code Scanner} from java.util to facilitate the interaction with the console.
 */
public class UI {
    /** {@code Scanner} created from {@code System.in} */
    static private Scanner scanner = new Scanner(System.in);

    /**
     * Waits for a new line from the scanner and then ignores it. Used as a user confirmation.
     */
    public static void waitConfirm() {
        scanner.nextLine();
    }

    /**
     * Interface used to represent a menu option.
     */
    public interface UIMenuOptionsInterface {
        /**
         * Populate a menu option.
         *
         * @param index The current option index
         * @return A {@code String} with the menu option description
         */
        String populate(int index);
    }

    /**
     * Creates and displays the main menu to the user and then waits for user input on a menu option.
     *
     * @param name The name of the main menu
     * @param numOptions The number of options given to the user
     * @param option A {@code UIMenuOptionsInterface} to a certain menu option
     * @return A {@code int} with the user input
     */
    public static int displayMenu(String name, int numOptions, UIMenuOptionsInterface option) {
        try {
            System.out.println("\n");
            printASCII("IT", name);
            for (int i = 0; i < numOptions; i++) {
                System.out.println(" " + (i + 1) + ". " + option.populate(i));
            }
            System.out.print(">");
            int input = scanner.nextInt();
            scanner.nextLine();
            if (input > 0) return input - 1;
        } catch (RuntimeException ex) { /**/ }
        return 0;
    }

    /**
     * Print a line to the console with a ASCII character at the start.
     * Used as a helper function to display information.
     *
     * @param ascii The ASCII character name
     * @param text The text to be shown
     */
    public static void printASCII(String ascii, String text) {
        System.out.println(ASCII.valueOf(ascii) + " " + text);
    }

    /**
     * Similar to {@link #printASCII(String, String)} but used for errors
     *
     * @see #printASCII(String, String)
     *
     * @param ascii The ASCII character name
     * @param error The error to be shown
     */
    public static void printASCIIError(String ascii, String error) {
        System.err.println(ASCII.valueOf(ascii) + " " + error);
    }

    /**
     * Print a prompt start.
     *
     * @param text The prompt to be shown
     */
    public static void printPromptStart(String text) {
        printASCII("BS", text);
        System.out.println("Pressiona Enter para sair.");
    }

    /**
     * Cleares the console by printing empty lines
     */
    public static void clearConsole() {
        for (int y = 0; y < 25; y++) // Console is 80 columns and 25 lines
            System.out.println("\n");
    }

    /**
     * Get input from the user
     *
     * @return The user input
     */
    private static String getInput() {
        System.out.print(" > ");
        String str = scanner.nextLine();
        return str.trim();
    }

    /**
     * Handles all logic related to questions to the user
     *
     * @param prompt Prompt to be shown
     * @param onInput {@code Predicate} used to test if the user input matches a certain condition (like input length or if it is a number)
     * @param onError {@code String} displayed if the {@code onInput} is not matched
     * @param optional {@code Boolean} indicating if the prompt is optional or not. In case the prompt is not optional {@code {@code optional} = false} it is repeated until the {@code onInput} {@code Predicate} condition is matched or the user aborts the prompt.
     * @return {@code String} containing the user input
     */
    public static String prompt(String prompt, Predicate<String> onInput, String onError, boolean optional) {
        printASCII("GP", prompt);
        while (true) {
            String str = getInput();
            if (str.isEmpty()) {
                if (!optional) printASCIIError("CROSS", "Comando abortado. Pressione Enter para retornar ao menu principal.");
                return null;
            }
            if (onInput.test(str)) return str;
            System.err.println(onError + " Tente outra vez.");
        }
    }

    /**
     * Similar to {@link #prompt(String, Predicate, String, boolean)} but with default {@code optional} set to false.
     *
     * @see #prompt(String, Predicate, String, boolean)
     *
     * @param prompt Prompt to be shown
     * @param onInput {@code Predicate} used to test if the user input matches a certain condition (like input length or if it is a number)
     * @param onError {@code String} displayed if the {@code onInput} is not matched
     * @return {@code String} containing the user input
     */
    public static String prompt(String prompt, Predicate<String> onInput, String onError) {
        return prompt(prompt, onInput, onError, false);
    }

    /**
     * Similar to {@link #prompt(String, Predicate, String, boolean)} but used for confirmation prompts.
     * Checks if the user input is valid confirmation (s/N).
     *
     * @param prompt The prompt to confirm
     * @return {@code Boolean} representing the users confirmation. {@code True} if user confirmed.
     */
    public static boolean confirm(String prompt) {
        printASCII("GP", prompt + " (s/N):");
        while (true) {
            String str = getInput();
            str = str.trim().toLowerCase();
            if (str.isEmpty() || str.equals("s") || str.equals("n")) {
                return str.equals("s");
            }
            System.err.println("A confirmação deve ser Sim (s) ou Não (n). Tente outra vez.");
        }
    }

    /**
     * Similar to {@link #prompt(String, Predicate, String, boolean)} but used for multiple choice prompts.
     * A menu is shown to the user with options and he is then asked to pick one of them.
     *
     * @param prompt The prompt to be shown
     * @param choices A {@code String[]} array of choices
     * @return The choice {@code String} that the user picked
     */
    public static String promptMultipleChoice(String prompt, String[] choices) {
        printASCII("GP", prompt + " (Escolher uma das opções)");
        for (int i = 0; i <= choices.length - 1; i++) {
            String choice = choices[i];
            System.out.println(" " + (i + 1) + ". " + choice.substring(0, 1).toUpperCase() + choice.substring(1));
        }
        while (true) {
            String str = getInput();
            if (str.isEmpty()) {
                printASCIIError("CROSS", "Comando abortado. Pressione Enter para retornar ao menu principal.");
                return null;
            }
            int choice = Integer.parseInt(str);
            if (choice > 0 && choice <= choices.length) return choices[choice - 1];
            printASCIIError("CROSS", "Opção invalida. Tente outra vez ou pressione enter para sair.");
        }
    }

    /** Welcome text displayed to the user when the program is first launched. */
    public final static String WELCOME_TEXT = "\n" +
            " $$$$$$\\  $$$$$$\\                       $$\\  $$$$$$\\        $$\\  $$$$$$\\   $$$$$$\\  \n" +
            "$$  __$$\\ \\_$$  _|                    $$$$ |$$  __$$\\      $$  |$$  __$$\\ $$$ __$$\\ \n" +
            "$$ /  \\__|  $$ |                      \\_$$ |$$ /  $$ |    $$  / \\__/  $$ |$$$$\\ $$ |\n" +
            "\\$$$$$$\\    $$ |        $$$$$$\\         $$ |\\$$$$$$$ |   $$  /   $$$$$$  |$$\\$$\\$$ |\n" +
            " \\____$$\\   $$ |        \\______|        $$ | \\____$$ |  $$  /   $$  ____/ $$ \\$$$$ |\n" +
            "$$\\   $$ |  $$ |                        $$ |$$\\   $$ | $$  /    $$ |      $$ |\\$$$ |\n" +
            "\\$$$$$$  |$$$$$$\\                     $$$$$$\\$$$$$$  |$$  /     $$$$$$$$\\ \\$$$$$$  /\n" +
            " \\______/ \\______|                    \\______\\______/ \\__/      \\________| \\______/ \n" +
            "\n" +
            ": Trabalho final de Sistemas de Informação\n" +
            ": Verão (Ano letivo 19/20)\n" +
            ": Grupo 1 da Turma TL31N\n" +
            ": Aplicação de acesso e manipulação de uma base de dados SQL\n" +
            "\n";
}
