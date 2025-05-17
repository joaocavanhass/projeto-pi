import java.util.Scanner;

class JogoQuadrados {
    private boolean[][] horizontalLines;
    private boolean[][] verticalLines;
    private int player1Score;
    private int player2Score;
    private boolean player1Turn;
    private int boardSize;

    public JogoQuadrados(int size) {
        this.boardSize = size;
        this.horizontalLines = new boolean[size][size - 1];
        this.verticalLines = new boolean[size - 1][size];
        this.player1Score = 0;
        this.player2Score = 0;
        this.player1Turn = true;
    }

    public boolean fazerJogada(int linha1, int coluna1, int linha2, int coluna2) {
        if (!saoAdjacentes(linha1, coluna1, linha2, coluna2) || !estaDentroDoTabuleiro(linha1, coluna1) || !estaDentroDoTabuleiro(linha2, coluna2) || linhaJaDesenhada(linha1, coluna1, linha2, coluna2)) {
            System.out.println("Jogada inválida. Tente novamente.");
            return false; // Jogada inválida
        }

        boolean quadradoFechado = false;
        int quadradosFechadosNestaJogada = 0;

        if (linha1 == linha2) { // Linha horizontal
            int coluna = Math.min(coluna1, coluna2);
            if (!horizontalLines[linha1][coluna]) {
                horizontalLines[linha1][coluna] = true;
                quadradosFechadosNestaJogada += verificarQuadradosFechadosHorizontal(linha1, coluna);
            }
        } else { // Linha vertical
            int linha = Math.min(linha1, linha2);
            if (!verticalLines[linha][coluna1]) {
                verticalLines[linha][coluna1] = true;
                quadradosFechadosNestaJogada += verificarQuadradosFechadosVertical(linha, coluna1);
            }
        }

        if (player1Turn) {
            player1Score += quadradosFechadosNestaJogada;
        } else {
            player2Score += quadradosFechadosNestaJogada;
        }

        return quadradosFechadosNestaJogada > 0;
    }

    private boolean saoAdjacentes(int r1, int c1, int r2, int c2) {
        return (r1 == r2 && Math.abs(c1 - c2) == 1) || (c1 == c2 && Math.abs(r1 - r2) == 1);
    }

    private boolean estaDentroDoTabuleiro(int r, int c) {
        return r >= 0 && r < boardSize && c >= 0 && c < boardSize;
    }

    private boolean linhaJaDesenhada(int r1, int c1, int r2, int c2) {
        if (r1 == r2) { // Horizontal
            int coluna = Math.min(c1, c2);
            return horizontalLines[r1][coluna];
        } else { // Vertical
            int linha = Math.min(r1, r2);
            return verticalLines[linha][c1];
        }
    }

    private int verificarQuadradosFechadosHorizontal(int linha, int coluna) {
        int quadradosFechados = 0;
        // Verificar quadrado acima
        if (linha > 0 && verticalLines[linha - 1][coluna] && verticalLines[linha - 1][coluna + 1] && horizontalLines[linha - 1][coluna]) {
            quadradosFechados++;
        }
        // Verificar quadrado abaixo
        if (linha < boardSize - 1 && verticalLines[linha][coluna] && verticalLines[linha][coluna + 1] && horizontalLines[linha + 1][coluna]) {
            quadradosFechados++;
        }
        return quadradosFechados;
    }

    private int verificarQuadradosFechadosVertical(int linha, int coluna) {
        int quadradosFechados = 0;
        // Verificar quadrado à esquerda
        if (coluna > 0 && horizontalLines[linha][coluna - 1] && horizontalLines[linha + 1][coluna - 1] && verticalLines[linha][coluna - 1]) {
            quadradosFechados++;
        }
        // Verificar quadrado à direita
        if (coluna < boardSize - 1 && horizontalLines[linha][coluna] && horizontalLines[linha + 1][coluna] && verticalLines[linha][coluna + 1]) {
            quadradosFechados++;
        }
        return quadradosFechados;
    }

    public boolean jogoTerminado() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize - 1; j++) {
                if (!horizontalLines[i][j]) return false;
            }
        }
        for (int i = 0; i < boardSize - 1; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!verticalLines[i][j]) return false;
            }
        }
        return true;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public boolean isPlayer1Turn() {
        return player1Turn;
    }

    public void alternarTurno() {
        player1Turn = !player1Turn;
    }

    public static void exibirTabuleiro(JogoQuadrados jogo) {
        int size = jogo.boardSize;
        boolean[][] horizontal = jogo.horizontalLines;
        boolean[][] vertical = jogo.verticalLines;

        for (int i = 0; i < size; i++) {
            // Imprimir linha de pontos
            for (int j = 0; j < size; j++) {
                System.out.print(".");
                if (j < size - 1) {
                    System.out.print(horizontal[i][j] ? " -- " : "    ");
                }
            }
            System.out.println();
            // Imprimir linhas verticais
            if (i < size - 1) {
                for (int j = 0; j < size; j++) {
                    System.out.print(vertical[i][j] ? "|   " : "    ");
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o tamanho do tabuleiro (número de pontos por lado): ");
        int size = scanner.nextInt();
        JogoQuadrados jogo = new JogoQuadrados(size);

        while (!jogo.jogoTerminado()) {
            System.out.println("\nTabuleiro:");
            exibirTabuleiro(jogo);

            int currentPlayer = jogo.isPlayer1Turn() ? 1 : 2;
            System.out.println("Vez do Jogador " + currentPlayer);
            System.out.print("Digite as coordenadas do primeiro ponto (linha coluna) da linha que deseja desenhar (ex: 0 0): ");
            int r1 = scanner.nextInt();
            int c1 = scanner.nextInt();
            System.out.print("Digite as coordenadas do segundo ponto (linha coluna) da linha que deseja desenhar (ex: 0 1): ");
            int r2 = scanner.nextInt();
            int c2 = scanner.nextInt();

            // Ajustar as coordenadas para serem baseadas em 0
            r1--;
            c1--;
            r2--;
            c2--;

            if (jogo.fazerJogada(r1, c1, r2, c2)) {
                System.out.println("Quadrado(s) fechado(s)! Jogador " + currentPlayer + " joga novamente.");
            } else {
                jogo.alternarTurno();
            }

            System.out.println("Pontuação: Jogador 1 = " + jogo.getPlayer1Score() + ", Jogador 2 = " + jogo.getPlayer2Score());
        }

        System.out.println("\nJogo Terminado!");
        if (jogo.getPlayer1Score() > jogo.getPlayer2Score()) {
            System.out.println("Jogador 1 venceu!");
        } else if (jogo.getPlayer2Score() > jogo.getPlayer1Score()) {
            System.out.println("Jogador 2 venceu!");
        } else {
            System.out.println("Empate!");
        }

        scanner.close();
    }
}
