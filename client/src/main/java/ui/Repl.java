package ui;

import java.util.Scanner;


public class Repl {
    public static String serverURL;
    public static State state;
    private final PreLogin preLogin = new PreLogin();
    private final PostLogin postLogin = new PostLogin();

    public Repl(String url) {
        Repl.serverURL= url;
        state = State.PRELOGIN;
    }
    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess. Sign in to start.");
        Scanner scanner = new Scanner(System.in);
        String resultString = "";

        while (!resultString.equals("quit")) {
            if (state == State.PRELOGIN){
                System.out.print(preLogin.help());
            }
            else if (state == State.POSTLOGIN){
                System.out.print(postLogin.help());
            }
            System.out.print("\n>>>");
            String line = scanner.nextLine();

            try {
                if (state == State.PRELOGIN) {
                    resultString = preLogin.eval(line);
                } else if (state == State.POSTLOGIN) {
                    resultString = postLogin.eval(line);
                }

            System.out.print(resultString);
            System.out.println();
            System.out.println();

            } catch (Throwable ex) {
                String errorMessage=ex.toString();
                System.out.print(errorMessage);
            }
        }
        System.out.println();
    }

}
