package puzzle;

public class Solver {
    private int[][][] map;
    private Block[] blocks;
    private int width, length, height = 1, counter, luas = 0;
    private boolean validMap = true;
    private String errorMsg;

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
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++){
                for (int k = 0; k < height; k++){
                    map[i][j][k] = 0;
                }
            }
        }
        this.blocks = blocks;
        this.luas = length * width * height;
    }

    private boolean place(Block block, int row, int col){
        boolean ok = true;
        for (int i = 0; i < block.getLength(); i++){
            for (int j = 0; j < block.getWidth(); j++){
                int mapi = i + row, mapj = j + col;
                if (mapi >= this.length || mapj >= this.width){
                    if (block.block[i][j] > 0)
                        ok = false;
                    continue;
                }
                if (block.block[i][j] > 0 && (this.map[mapi][mapj][0] > 0 || this.map[mapi][mapj][0] == -1)){
                    ok = false;
                }
            }
        }
        if (!ok) return false;
        for (int i = 0; i < block.getLength(); i++){
            for (int j = 0; j < block.getWidth(); j++){
                int mapi = i + row, mapj = j + col;
                if (mapi < this.length && mapj < this.width && block.block[i][j] > 0)
                    this.map[mapi][mapj][0] = block.block[i][j];
            }
        }
        return true;
    }

    private void unplace(Block block, int row, int col){
        for (int i = 0; i < block.getLength(); i++){
            for (int j = 0; j < block.getWidth(); j++){
                int mapi = i + row, mapj = j + col;
                if (mapi < this.length && mapj < this.width && block.block[i][j] > 0)
                    this.map[mapi][mapj][0] = 0;
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
    private boolean solveHelper(int blockidx, int i, int j, int rotate){
        this.counter++;
        if (rotate == 8){
            return solveHelper(blockidx, i, j+1, 0);
        }
        if (j >= this.width){
            return solveHelper(blockidx, i+1, 0, rotate);
        }
        if (i >= this.length){
            return solveHelper(blockidx + 1, 0, 0, rotate);
        }
        if (blockidx >= blocks.length){
            if (isMapFull()) return true;
            else return false;
            // return true;
        }
        Block currBlock = blocks[blockidx];
        // rotate block
        for (int temp = 0; temp < rotate%4; temp++)
            currBlock.rotate();
        if (rotate >= 4)
            currBlock.mirror();

        if (place(currBlock, i, j)){
            if (solveHelper(blockidx+1, 0, 0, 1) && isMapFull()){
                return true;
            }
            else{
                unplace(currBlock, i, j);
            }
        }
        return solveHelper(blockidx, i, j, rotate+1);
    }
    public void solve(){
        if (!validMap){
            System.out.println(errorMsg);
            return;
        }
        
        int totalluas = 0;
        for (int i = 0; i < blocks.length; i++){
            totalluas += blocks[i].getLuas();
        }
        if (totalluas != this.luas){
            System.out.println("Not Solvable! Total area of blocks must be equal to the area of the map!");
            return;
        }

        this.counter = 0;
        java.time.LocalTime time = java.time.LocalTime.now();
        if (solveHelper(0, 0, 0, 0)){
            this.print();
        }
        else{
            System.out.println("Not Solvable");
        }
        java.time.Duration duration = java.time.Duration.between(time, java.time.LocalTime.now());
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

    private void colorPrint(char c){
        int colorCode = (c - 'A') + 1; 
        System.out.print("\u001B[38;5;" + colorCode + "m" + c + "\u001B[0m");
    }

}
