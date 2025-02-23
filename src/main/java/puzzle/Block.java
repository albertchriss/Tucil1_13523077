package puzzle;

public class Block {
    public int[][][] block;
    private int color, size, width, length;

    // Constructor
    public Block(String[] input) {
        int length = input.length, width = input[0].length();
        for (int i = 1; i < length; i++){
            width = Math.max(width, input[i].length());
        }
        this.length = length;
        this.width = width;
        this.size = Math.max(length, width)*2;
        this.block = new int[size][size][size];
        this.color = (int) (input[0].charAt(0));

        for (int i = 0; i < this.size; i++) {
            if (i >= length){
                for (int j = 0; j < this.size; j++)
                    this.block[i][j][0] = 0;
                continue;
            }
            String line = input[i];
            for (int j = 0; j < this.size; j++) {
                if (j >= line.length()) {
                    this.block[i][j][0] = 0;
                    continue;
                }

                char c = line.charAt(j);
                if (c == ' ') {
                    this.block[i][j][0] = 0;
                } else {
                    this.block[i][j][0] = this.color;
                }

                for (int k = 1; k < this.size; k++){
                    this.block[i][j][k] = 0;
                }
            }
        }

        pushLeft();
        pushUp();
    }

    public Block(Block input){
        this.size = input.size;
        this.color = input.color;
        this.block = new int[this.size][this.size][this.size];
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){
                for (int k = 0; k < this.size; k++){
                    this.block[i][j][k] = input.block[i][j][k];
                }
            }
        }
    }
    
    // getter function
    public int getSize(){
        return this.size;
    }
    public int getColor(){
        return this.color;
    }
    public int getLuas(){
        int luas = 0;
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){
                if (this.block[i][j][0] != 0){
                    luas++;
                }
            }
        }
        return luas;
    }

    public void mirror(){
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size / 2; j++){
                int temp = this.block[i][j][0];
                this.block[i][j][0] = this.block[i][this.size - j - 1][0];
                this.block[i][this.size - j - 1][0] = temp;
            }
        }
        pushLeft();
        pushUp();
    }

    public void rotate() {
        int[][][] temp = new int[this.size][this.size][this.size];
        for (int k = 0; k < this.size; k++){
            for(int i = 0; i < this.size; i++){
                for (int j = 0; j < this.size; j++){
                    temp[i][j][k] = this.block[this.size - j - 1][i][k];
                }
            }
        }
        for (int k = 0; k < this.size; k++){
            for (int i = 0; i < this.size; i++){
                for (int j = 0; j < this.size; j++){
                    this.block[i][j][k] = temp[i][j][k];
                }
            }
        }
        pushLeft();
        pushUp();
    }

    public void lift(){
        for (int i = 0; i < this.size/2; i++){
            for (int j = 0; j < this.size/2; j++){
                int temp = this.block[i][j][i+j];
                this.block[i][j][i+j] = this.block[i][j][0];
                this.block[i][j][0] = temp;
            }
        }
        pushBottom();
        pushLeft();
        pushUp();
    }

    public void mirrorLift(){
        for (int k = 0; k < this.size/2; k++){
            for (int i = 0; i < k+1; i++){
                for (int j = 0; j < (k+1)/2; j++){
                    int temp = this.block[i][j][k];
                    this.block[i][j][k] = this.block[i][k-j][k];
                    this.block[i][k-j][k] = temp;
                }
            }
        }
        for (int k = this.size/2+1; k < this.size; k++){
            for (int i = k - this.size/2; i < this.size/2; i++){
                for (int j = k - this.size/2; j < k/2; j++){
                    int temp = this.block[i][j][k-1];
                    this.block[i][j][k-1] = this.block[i][k - j - 1][k-1];
                    this.block[i][k - j - 1][k-1] = temp;
                }
            }
        }
    }

    public void print3D() {
        for (int k = this.size-1; k >= 0; k--){
            System.out.println("Layer " + k);
            for (int i = 0; i < this.size/2; i++){
                for (int j = 0; j < this.size/2; j++){
                    if (this.block[i][j][k] == 0) System.out.print("X");
                    else
                    System.out.print((char) this.block[i][j][k]);
                }
                System.out.println();
            }
        }
    }

    public void print(){
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){
                System.out.print((char) this.block[i][j][0] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Merapatkan konten block ke kiri
     */
    private void pushLeft() {
        boolean empty = true;
        do{
            for (int i = 0; i < this.size; i++) {
                for (int k = 0; k < this.size; k++){
                    if (block[i][0][k] != 0) {
                        empty = false;
                        break;
                    }
                }
            }
            if (empty){
                for (int k = 0; k < this.size; k++){
                    for (int i = 0; i < this.size; i++) {
                        for (int j = 0; j < this.size - 1; j++) {
                                block[i][j][k] = block[i][j + 1][k];
                        }
                        block[i][this.size - 1][k] = 0;
                    }
                }
            }
        } while (empty);
    }

    /**
     * Merapatkan konten block ke kiri
     */
    private void pushUp() {
        boolean empty = true;
        do{
            for (int j = 0; j < this.size; j++) {
                for (int k = 0; k < this.size; k++){
                    if (block[0][j][k] != 0) {
                        empty = false;
                        break;
                    }
                }
            }
            if (empty){
                for (int k = 0; k < this.size; k++){
                    for (int i = 0; i < this.size - 1; i++) {
                        for (int j = 0; j < this.size; j++) {
                            block[i][j][k] = block[i + 1][j][k];
                        }
                    }
                    for (int j = 0; j < this.size; j++) {
                        block[this.size - 1][j][k] = 0;
                    }
                }
            }
        } while (empty);
    }

    private void pushBottom(){
        boolean empty = true;
        do {
            for (int i = 0; i < this.size; i++){
                for (int j = 0; j < this.size; j++){
                    if (this.block[i][j][0] != 0) {
                        empty = false;
                        break;
                    }
                }
            }
            if (empty){
                for (int k = 0; k < this.size-1; k++){
                    for (int i = 0; i < this.size; i++){
                        for (int j = 0; j < this.size; j++){
                            this.block[i][j][k] = this.block[i][j][k+1];
                        }
                    }
                }
                for (int i = 0; i < this.size; i++){
                    for (int j = 0; j < this.size; j++){
                        this.block[i][j][this.size-1] = 0;
                    }
                }
            }
        } while (empty);

    }

    public String getBlockAsString(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.length; i++){
            int lastidx = -1;
            for (int j = this.width-1; j >= 0; j--){
                if (this.block[i][j][0] > 0){
                    lastidx = j;
                    break;
                }
            }
            for (int j = 0; j <= lastidx; j++){
                if (this.block[i][j][0] == 0){
                    sb.append(" ");
                } else {
                    sb.append((char) this.block[i][j][0]);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}
