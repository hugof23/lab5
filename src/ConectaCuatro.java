import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ConectaCuatro {
    
    public static class HashST<K, V> {
        private static final int INIT_CAPACITY = 16;
        private Node[] buckets;
        private int size;

        private class Node {
            K key;
            V value;
            Node next;

            public Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }

        public HashST() {
            buckets = (Node[]) new HashST.Node[INIT_CAPACITY];
        }

        public void put(K key, V value) {
            if (key == null) throw new IllegalArgumentException("Key cannot be null");

            int index = hash(key);
            Node current = buckets[index];

            while (current != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }

            Node newNode = new Node(key, value);
            newNode.next = buckets[index];
            buckets[index] = newNode;
            size++;
        }

        public V get(K key) {
            if (key == null) throw new IllegalArgumentException("Key cannot be null");

            int index = hash(key);
            Node current = buckets[index];

            while (current != null) {
                if (current.key.equals(key)) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }

        public boolean contains(K key) {
            return get(key) != null;
        }

        private int hash(K key) {
            return (key.hashCode() & 0x7fffffff) % buckets.length;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }
    }

    public static class BST<K extends Comparable<K>, V> {
        private Node root;

        private class Node {
            K key;
            V value;
            Node left, right;

            public Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }

        public void put(K key, V value) {
            root = put(root, key, value);
        }

        private Node put(Node node, K key, V value) {
            if (node == null) return new Node(key, value);
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node.left = put(node.left, key, value);
            else if (cmp > 0) node.right = put(node.right, key, value);
            else node.value = value;
            return node;
        }

        public V get(K key) {
            Node node = get(root, key);
            return node == null ? null : node.value;
        }

        private Node get(Node node, K key) {
            if (node == null) return null;
            int cmp = key.compareTo(node.key);
            if (cmp < 0) return get(node.left, key);
            else if (cmp > 0) return get(node.right, key);
            else return node;
        }

        public void delete(K key, V value) {
            root = delete(root, key, value);
        }

        private Node delete(Node node, K key, V value) {
            if (node == null) return null;

            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node.left = delete(node.left, key, value);
            } else if (cmp > 0) {
                node.right = delete(node.right, key, value);
            } else {
                if (node.value.equals(value)) {
                    if (node.right == null) return node.left;
                    if (node.left == null) return node.right;

                    Node temp = node;
                    node = min(temp.right);
                    node.right = deleteMin(temp.right);
                    node.left = temp.left;
                }
            }
            return node;
        }

        private Node min(Node node) {
            if (node.left == null) return node;
            return min(node.left);
        }

        private Node deleteMin(Node node) {
            if (node.left == null) return node.right;
            node.left = deleteMin(node.left);
            return node;
        }

        public Iterable<K> keysInRange(K lo, K hi) {
            List<K> keys = new ArrayList<>();
            keysInRange(root, keys, lo, hi);
            return keys;
        }

        private void keysInRange(Node node, List<K> keys, K lo, K hi) {
            if (node == null) return;
            int cmpLo = lo.compareTo(node.key);
            int cmpHi = hi.compareTo(node.key);

            if (cmpLo < 0) keysInRange(node.left, keys, lo, hi);
            if (cmpLo <= 0 && cmpHi >= 0) keys.add(node.key);
            if (cmpHi > 0) keysInRange(node.right, keys, lo, hi);
        }

        public Iterable<V> getAll(K key) {
            List<V> values = new ArrayList<>();
            getAll(root, key, values);
            return values;
        }

        private void getAll(Node node, K key, List<V> values) {
            if (node == null) return;
            int cmp = key.compareTo(node.key);
            if (cmp < 0) getAll(node.left, key, values);
            else if (cmp > 0) getAll(node.right, key, values);
            else {
                values.add(node.value);
                getAll(node.left, key, values);
                getAll(node.right, key, values);
            }
        }

        public K successor(K key) {
            Node succ = successor(root, key, null);
            return succ != null ? succ.key : null;
        }

        private Node successor(Node node, K key, Node candidate) {
            if (node == null) return candidate;

            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                return successor(node.left, key, node);
            } else if (cmp > 0) {
                return successor(node.right, key, candidate);
            } else {
                if (node.right != null) {
                    return min(node.right);
                } else {
                    return candidate;
                }
            }
        }
    }

    public static class Player {
        String playerName;
        int wins;
        int draws;
        int losses;

        public Player(String playerName, int wins, int draws, int losses) {
            this.playerName = playerName;
            this.wins = wins;
            this.draws = draws;
            this.losses = losses;
        }

        public String getPlayerName() {
            return playerName;
        }
        public int getWins() {
            return wins;
        }
        public int getDraws() {
            return draws;
        }
        public int getLosses() {
            return losses;
        }
        public void  addWin(){
            wins++;
        }
        public void  addDraw(){
            draws++;
        }
        public void  addLoss(){
            losses++;
        }
        public double winRate(){
            int totalGames = wins + draws + losses;
            if(totalGames == 0){
                return 0;
            }
            return (double)wins / (double)totalGames;
        }
    }

    public static class ConnectFour {
        private char[][] grid;
        private static char currentSymbol;

        public ConnectFour(){
            grid = new char[7][6];
            for (int col = 0; col < 7; col++){
                for (int fila = 0; fila < 6; fila++){
                    grid[col][fila] = ' ';
                }
            }
            currentSymbol = 'X';
        }

        private boolean columnallena(int col){
            return grid[col][5] != ' ';
        }

        public static char getCurrentSymbol(){
            return currentSymbol;
        }

        public boolean makeMove(int col){
            if (col < 0 || col >= 7 || columnallena(col)){
                return false;
            }

            for (int fila = 0; fila < 6; fila++){
                if (grid[col][fila] == ' '){
                    grid[col][fila] = currentSymbol;
                    currentSymbol = (currentSymbol == 'X' ? 'O' : 'X');
                    return true;
                }
            }
            return false;
        }

        public enum Resultado{
            EN_PROGRESO, VICTORIA_X, VICTORIA_O, DRAW;
        }

        private boolean checkWin(char symbol){
            for (int col = 0; col < 7; col++){
                for (int fila = 0; fila < 6; fila++){
                    if (grid[col][fila] == symbol){
                        if(checkDireccion(col, fila, 1, 0, symbol) || checkDireccion(col, fila, 0, 1, symbol) ||
                        checkDireccion(col, fila, 1, 1, symbol) || checkDireccion(col, fila, 1, -1, symbol))
                            return true;
                    }
                }
            }
            return false;
        }

       private boolean checkDireccion(int col, int fila, int dx, int dy, char symbol){
            for (int i = 0; i < 4; i++){
                int newCol = col + i * dx;
                int newFila = fila + i * dy;
                if (newCol < 0 || newCol >= 7 || newFila < 0 || newFila >= 6 || grid[newCol][newFila] != symbol){
                    return false;
                }
            }
            return true;
       }

       private boolean tableroLleno(){
            for (int col = 0; col < 7; col++){
                if(!columnallena(col)){
                    return false;
                }
            }
            return true;
       }

        public Resultado isGameOver(){
            if (checkWin('X')){
                return Resultado.VICTORIA_X;
            }else if (checkWin('O')){
                return Resultado.VICTORIA_O;
            }
            if(tableroLleno()){
                return Resultado.DRAW;
            }
            return Resultado.EN_PROGRESO;
        }

        public void ImprimeTablero() {
            for (int fila = 5; fila >= 0; fila--) {
                for (int col = 0; col < 7; col++) {
                    System.out.print("|" + grid[col][fila]);
                }
                System.out.println("|");
            }
            System.out.println("---------------");
        }
    }

    public static class Game {
        private String Status;
        private String winnerPlayer;
        private String playerNameA;
        private String playerNameB;
        private ConnectFour connectFour;

        public Game(String playerNameA, String playerNameB) {
            this.playerNameA = playerNameA;
            this.playerNameB = playerNameB;
            this.connectFour = new ConnectFour();
            this.Status = "EN_PROGRESO";
            this.winnerPlayer = "";
        }

        public String getStatus() {
            return Status;
        }
        public String getWinnerPlayer() {
            return winnerPlayer;
        }
        private void gameResult(ConnectFour.Resultado resultado) {
            if(resultado == ConnectFour.Resultado.VICTORIA_X) {
                Status = "VICTORIA";
                winnerPlayer = playerNameA;
            }else if(resultado == ConnectFour.Resultado.VICTORIA_O) {
                Status = "VICTORIA";
                winnerPlayer = playerNameB;
            }else if(resultado == ConnectFour.Resultado.DRAW) {
                Status = "DRAW";
                winnerPlayer = "";
            }
        }
        public String play() {
            Scanner sc = new Scanner(System.in);
            while (Status.equals("EN_PROGRESO")) {
                connectFour.ImprimeTablero();
                char simbolo = ConnectFour.getCurrentSymbol();
                String currentPlayer = (simbolo == 'X') ? playerNameA : playerNameB;
                System.out.println("Turno de: " + currentPlayer + " (" + simbolo + "). Ingresa columna (0-6)");

                int col = sc.nextInt();
                if (!connectFour.makeMove(col)){
                    System.out.println("Columna invalida. Usa Otra!!!");
                    continue;
                }
                ConnectFour.Resultado resultado = connectFour.isGameOver();
                if (resultado != ConnectFour.Resultado.EN_PROGRESO) {
                    gameResult(resultado);
                    break;
                }
            }
            return winnerPlayer;
        }
    }

    public static class ScoreBoard {
        private BST<Integer, String> winTree;
        private HashST<String, Player> players;
        int playedGames;

        public ScoreBoard() {
            this.winTree = new BST<>();
            this.players = new HashST<>();
            this.playedGames = 0;
        }

        public void registerPlayer(String playerName) {
            if (!players.contains(playerName)){
                Player newPlayer = new Player(playerName, 0, 0, 0);
                players.put(playerName, newPlayer);
                winTree.put(0, playerName);
            }
        }

        public boolean checkPlayer(String playerName) {
            return players.contains(playerName);
        }

        public void addGameResult(String winnerPlayerName, String looserPlayerName, boolean draw){
            if (!checkPlayer(winnerPlayerName)){
                registerPlayer(winnerPlayerName);
            }
            if (!checkPlayer(looserPlayerName)){
                registerPlayer(looserPlayerName);
            }

            Player winner = players.get(winnerPlayerName);
            Player looser = players.get(looserPlayerName);

            if(draw){
                winner.addDraw();
                looser.addDraw();
            }else{
                int oldWins = winner.getWins();

                winner.addWin();
                looser.addLoss();

                winTree.delete(oldWins, winner.getPlayerName());
                winTree.put(winner.getWins(), winner.getPlayerName());
            }

            this.playedGames++;
        }

        public Player[] winRange(int lo, int hi){
            List<Player> resultPlayers = new ArrayList<>();
            Iterable<Integer> KeysInRange = winTree.keysInRange(lo, hi);

            for(Integer key : KeysInRange){
                Iterable<String> playerNames = winTree.getAll(key);
                for(String playerName : playerNames){
                    resultPlayers.add(players.get(playerName));
                }
            }

            return resultPlayers.toArray(new Player[0]);
        }

        public Player[] winSuccessor(int wins){
            Integer successorWins = winTree.successor(wins);

            if (successorWins == null){
                return new Player[0];
            }

            List<Player> resultPlayers = new ArrayList<>();
            Iterable<String> playerNames = winTree.getAll(successorWins);
            for(String Name : playerNames){
                resultPlayers.add(players.get(Name));
            }

            return resultPlayers.toArray(new Player[0]);
        }
    }

public static void main(String[] args) {
    ScoreBoard scoreBoard = new ScoreBoard();
    Scanner scanner = new Scanner(System.in);
    
    System.out.println("Bienvenido a Conecta 4");
    
    boolean jugarOtraPartida = true;
    
    while (jugarOtraPartida) {
        System.out.print("Ingrese nombre del Jugador 1 (X): ");
        String player1 = scanner.nextLine();
        System.out.print("Ingrese nombre del Jugador 2 (O): ");
        String player2 = scanner.nextLine();
        
        Game game = new Game(player1, player2);
        String winner = game.play();
        
        if (winner.isEmpty()) {
            System.out.println("El juego terminó en empate!");
            scoreBoard.addGameResult(player1, player2, true);
        } else {
            System.out.println("El ganador es: " + winner + "!");
            scoreBoard.addGameResult(winner, winner.equals(player1) ? player2 : player1, false);
        }
        
        System.out.println("\nEstadísticas:");
        Player[] topPlayers = scoreBoard.winRange(0, Integer.MAX_VALUE);
        for (Player p : topPlayers) {
            System.out.printf("%s: %d victorias, %d empates, %d derrotas (%.2f%% porcentaje de victorias)%n",
                p.getPlayerName(), p.getWins(), p.getDraws(), p.getLosses(), p.winRate() * 100);
        }
        
        System.out.print("\n¿Quieren jugar otra partida? (s/n): ");
        String respuesta = scanner.nextLine().toLowerCase();
        jugarOtraPartida = respuesta.equals("s") || respuesta.equals("si");
    }
    
    System.out.println("¡Gracias por jugar!");
    scanner.close();
}
}