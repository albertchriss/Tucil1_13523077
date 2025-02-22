package puzzle;

public class Solver {
    private int[][][] map;
    private Block[] blocks;
    private int width, length, height = 1, counter, luas = 0;
    private boolean validMap = true;
    private String errorMsg, mode;
    private boolean[] placed;
    private java.time.Duration duration;
    public boolean isSolved;
    public String solveMsg;

    public Solver(int length, int width, String[] input, Block[] blocks){
        this.length = length;
        this.width = width;
        if (this.length != input.length){
            this.validMap = false;
            this.errorMsg = "Invalid map!";
        }
        for (int i = 0; i < this.length; i++){
            if (input[i].length() != width){
                this.validMap = false;
                this.errorMsg = "Invalid map!";
                break;
            }
        }
        this.map = new int[this.length][this.width][this.height];
        if (validMap){
            for(int i = 0; i < this.length; i++){
                String line = input[i];
                for (int j = 0; j < this.width; j++){
                    if (line.charAt(j) == '.'){
                        this.map[i][j][0] = -1;
                    }
                    else if (line.charAt(j) == 'X'){
                        this.map[i][j][0] = 0;
                        this.luas++;
                    }
                    else{
                        this.validMap = false;
                        this.errorMsg = "Invalid map! Map must only contain \'.\' or \'X\' character!";
                    }

                }
            }
        }
        this.blocks = blocks;
        this.mode = "CUSTOM";

        this.placed = new boolean[blocks.length];
        for (int i = 0; i < placed.length; i++){
            placed[i] = false;
        }
    }

    public Solver(int length, int width, Block[] blocks){
        this.length = length;
        this.width = width;
        this.map = new int[length][width][this.height];
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++){
                map[i][j][0] = 0;
            }
        }
        this.blocks = blocks;
        this.luas = length * width;
        this.mode = "DEFAULT";
        this.placed = new boolean[blocks.length];
        for (int i = 0; i < placed.length; i++){
            placed[i] = false;
        }
    }

    public Solver(int length, int width, int height, Block[] blocks){
        this.length = length;
        this.width = width;
        if (length != width){
            this.validMap = false;
            this.errorMsg = "Invalid map for pyramid!";
        }
        this.height = height;
        this.map = new int[length][width][height];
        for (int k = 0; k < height; k++){
            for (int i = 0; i < length; i++){
                for (int j = 0; j < width; j++){
                    if (i >= k && j >= k){
                        map[i][j][k] = 0;
                        this.luas++;
                    }
                    else{
                        map[i][j][k] = -1;
                    }
                }
            }
        }
        this.blocks = blocks;
        this.mode = "PYRAMID";
        this.placed = new boolean[blocks.length];
        for (int i = 0; i < placed.length; i++){
            placed[i] = false;
        }
    }

    public int[][][] getMap(){
        return this.map;
    }

    public int getWidth(){
        return this.width;
    }

    public int getLength(){
        return this.length;
    }

    public int getHeight(){
        return this.height;
    }

    public int getCounter(){
        return this.counter;
    }

    public int getDuration(){
        return (int) duration.toMillis();
    }

    // normal
    private boolean place(Block block, int row, int col){
        boolean ok = true;
        for (int i = 0; i < block.getSize(); i++){
            for (int j = 0; j < block.getSize(); j++){
                int mapi = i + row, mapj = j + col;
                if (mapi >= this.length || mapj >= this.width){
                    if (block.block[i][j][0] > 0)
                        ok = false;
                    continue;
                }
                if (block.block[i][j][0] > 0 && (this.map[mapi][mapj][0] > 0 || this.map[mapi][mapj][0] == -1)){
                    ok = false;
                }
            }
        }
        if (!ok) return false;
        for (int i = 0; i < block.getSize(); i++){
            for (int j = 0; j < block.getSize(); j++){
                int mapi = i + row, mapj = j + col;
                if (mapi < this.length && mapj < this.width && block.block[i][j][0] > 0)
                    this.map[mapi][mapj][0] = block.block[i][j][0];
            }
        }
        return true;
    }

    // pyramid
    private boolean place(Block block, int row, int col, int height){
        boolean ok = true;
        for (int i = 0; i < block.getSize(); i++){
            for (int j = 0; j < block.getSize(); j++){
                for (int k = 0; k < block.getSize(); k++){
                    int mapi = i + row, mapj = j + col, mapk = k + height;
                    if (mapi >= this.length || mapj >= this.width || mapk >= this.height){
                        if (block.block[i][j][k] > 0)
                            ok = false;
                        continue;
                    }
                    if (block.block[i][j][k] > 0 && (this.map[mapi][mapj][mapk] > 0 || this.map[mapi][mapj][mapk] == -1)){
                        ok = false;
                    }
                }
            }
        }
        if (!ok) return false;
        for (int i = 0; i < block.getSize(); i++){
            for (int j = 0; j < block.getSize(); j++){
                for (int k = 0; k < block.getSize(); k++){
                    int mapi = i + row, mapj = j + col, mapk = k + height;
                    if (mapi < this.length && mapj < this.width && mapk < this.height && block.block[i][j][k] > 0)
                        this.map[mapi][mapj][mapk] = block.block[i][j][k];
                }
            }
        }
        return true;
    }

    // normal
    private void unplace(Block block, int row, int col){
        for (int i = 0; i < block.getSize(); i++){
            for (int j = 0; j < block.getSize(); j++){
                int mapi = i + row, mapj = j + col;
                if (mapi < this.length && mapj < this.width && block.block[i][j][0]> 0)
                    this.map[mapi][mapj][0] = 0;
            }
        }
    }

    // pyramid
    private void unplace(Block block, int row, int col, int height){
        for (int i = 0; i < block.getSize(); i++){
            for (int j = 0; j < block.getSize(); j++){
                for (int k = 0; k < block.getSize(); k++){
                    int mapi = i + row, mapj = j + col, mapk = k + height;
                    if (mapi < this.length && mapj < this.width && mapk < this.height && block.block[i][j][k] > 0)
                        this.map[mapi][mapj][mapk] = 0;
                }
            }
        }
    }

    private boolean isMapFull(){
        for (int i = 0; i < this.length; i++){
            for (int j = 0; j < this.width; j++){
                for (int k = 0; k < this.height; k++){
                    if (this.map[i][j][k] == 0) return false;
                }
            }
        }
        return true;
    }

    private boolean isPyramidSolved(){
        for (int k = 0; k < this.height; k++){
            for (int i = k; i < this.length; i++){
                for (int j = k; j < this.width; j++){
                    if (this.map[i][j][k] == 0) return false;
                }
            }
        }
        return true;
    }

    private boolean solveHelper(int blockidx, int i, int j, int rotate){
        this.counter++;
        if (rotate == 8) return solveHelper(blockidx, i, j+1, 0);
        if (j >= this.width) return solveHelper(blockidx, i+1, 0, rotate);
        if (i >= this.length) return false;
        if (blockidx >= blocks.length) return isMapFull();
        Block currBlock = new Block(blocks[blockidx]);
        // rotate block
        for (int temp = 0; temp < rotate%4; temp++) currBlock.rotate();
        if (rotate >= 4) currBlock.mirror();

        if (place(currBlock, i, j)){
            if (solveHelper(blockidx+1, 0, 0, 0) && isMapFull()) return true;
            
            else unplace(currBlock, i, j);
        }
        return solveHelper(blockidx, i, j, rotate+1);
    }

    private boolean solveHelper(int blockidx, int i, int j, int k, int rotate){
        this.counter++;
        if (rotate == 24) return solveHelper(blockidx, i, j, k+1, 0);
        if (k >= this.height) return solveHelper(blockidx, i, j+1, 0, rotate);
        if (j >= this.width) return solveHelper(blockidx, i+1, 0, 0, rotate);
        if (i >= this.length) return false;
        if (blockidx >= blocks.length) return isPyramidSolved();
        Block currBlock = new Block(blocks[blockidx]);
        // rotate block
        int lift = rotate / 8, rotate2 = rotate % 8;
        for (int temp = 0; temp < rotate2%4; temp++) currBlock.rotate();
        if (rotate2 >= 4) currBlock.mirror();
        if (lift >= 1) currBlock.lift();
        if (lift == 2) currBlock.mirrorLift();



        if (place(currBlock, i, j, k)){
            if (solveHelper(blockidx+1, 0, 0, 0, 0) && isPyramidSolved()) return true;
            else unplace(currBlock, i, j, k);
        }
        return solveHelper(blockidx, i, j, k, rotate+1);
    }

    public void solve(){
        if (!validMap){
            // System.out.println(errorMsg);
            this.isSolved = false;
            this.solveMsg = errorMsg;
            return;
        }
        
        int totalluas = 0;
        for (int i = 0; i < blocks.length; i++){
            totalluas += blocks[i].getLuas();
        }
        if (totalluas != this.luas){
            this.isSolved = false;
            this.solveMsg = "Not Solvable! Total area of blocks must be equal to the area of the map!";
            return;
        }

        this.counter = 0;
        java.time.LocalTime time = java.time.LocalTime.now();
        if (this.mode.equals("PYRAMID")){
            if (solveHelper(0, 0, 0, 0, 0)){
                this.printPyramid();
                this.isSolved = true;
            }
            else{
                this.isSolved = false;
                this.solveMsg = "Not Solvable!";
            }
        }
        else {
            if (solveHelper(0, 0, 0, 0)){
                this.isSolved = true;
                this.print();
            }
            else{
                this.isSolved = false;
                this.solveMsg = "Not Solvable!";
            }
        }
        duration = java.time.Duration.between(time, java.time.LocalTime.now());
        System.out.println("\nBanyak kasus yang ditinjau: " + counter);
        System.out.println("\nWaktu pencarian: " + duration.toMillis() + " ms");
    }
    public void print(){
        for (int i = 0; i < this.length; i++){
            for (int j = 0; j < this.width; j++){
                if (map[i][j][0] > 0)
                    colorPrint((char) this.map[i][j][0]);
                else
                    System.out.print(" ");
            }
            System.out.println();
        }
    }

    public void printPyramid(){
        for (int k = this.height-1; k >= 0; k--){
            for (int i = k; i < this.length; i++){
                for (int j = k; j < this.width; j++){
                    if (map[i][j][k] > 0)
                        colorPrint((char) this.map[i][j][k]);
                    else
                        System.out.print(" ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public String getOutput(){
        StringBuilder sb = new StringBuilder();

        if (this.mode.equals("PYRAMID")){
            for (int k = this.height-1; k >= 0; k--){
                for (int i = k; i < this.length; i++){
                    for (int j = k; j < this.width; j++){
                        if (map[i][j][k] > 0)
                            sb.append(getColorChar((char) this.map[i][j][k]));
                        else
                            sb.append(" ");
                    }
                    sb.append("\n");
                }
                sb.append("\n");
            }
        }
        else{
            for (int i = 0; i < this.length; i++){
                for (int j = 0; j < this.width; j++){
                    if (map[i][j][0] > 0)
                        sb.append(getColorChar((char) this.map[i][j][0]));
                    else
                        sb.append(" ");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String getColorChar(char c){
        int colorCode = (c - 'A') + 1; 
        return "\u001B[38;5;" + colorCode + "m" + c + "\u001B[0m";
    }

    private void colorPrint(char c){
        int colorCode = (c - 'A') + 1; 
        System.out.print("\u001B[38;5;" + colorCode + "m" + c + "\u001B[0m");
    }

}
