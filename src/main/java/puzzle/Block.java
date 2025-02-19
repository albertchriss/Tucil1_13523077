package puzzle;

public class Block {
    public int[][] block;
    private int width, length, color, size;

    // Constructor
    public Block(String[] input) {
        this.length = input.length;
        this.width = input[0].length();
        for (int i = 1; i < length; i++){
            this.width = Math.max(this.width, input[i].length());
        }
        this.size = Math.max(this.length, this.width);
        this.block = new int[size][size];
        this.color = (int) (input[0].charAt(0));

        for (int i = 0; i < this.size; i++) {
            if (i >= this.length){
                for (int j = 0; j < this.size; j++)
                    this.block[i][j] = 0;
                continue;
            }
            String line = input[i];
            for (int j = 0; j < this.size; j++) {
                if (j >= line.length()) {
                    this.block[i][j] = 0;
                    continue;
                }

                char c = line.charAt(j);
                if (c == ' ') {
                    block[i][j] = 0;
                } else {
                    block[i][j] = this.color;
                }
            }
        }

        pushLeft();
        pushUp();
    }
    
    // getter function
    public int getWidth() {
        return this.width;
    }
    public int getLength(){
        return this.length;
    }
    public int getColor(){
        return this.color;
    }
    public int getLuas(){
        int luas = 0;
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){
                if (this.block[i][j] != 0){
                    luas++;
                }
            }
        }
        return luas;
    }

    public void mirror(){
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size / 2; j++){
                int temp = this.block[i][j];
                this.block[i][j] = this.block[i][this.size - j - 1];
                this.block[i][this.size - j - 1] = temp;
            }
        }
        pushLeft();
        pushUp();
    }

    public void rotate() {
        int[][] temp = new int[this.size][this.size];
        for(int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){
                temp[i][j] = this.block[this.size - j - 1][i];
            }
        }
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){
                this.block[i][j] = temp[i][j];
            }
        }
        pushLeft();
        pushUp();
        int x = this.length;
        this.length = this.width;
        this.width = x;

    }

    public void print() {
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){
                System.out.print((char) this.block[i][j] + " ");
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
                if (block[i][0] != 0) {
                    empty = false;
                    break;
                }
            }
            if (empty){
                for (int i = 0; i < this.size; i++) {
                    for (int j = 0; j < this.size - 1; j++) {
                        block[i][j] = block[i][j + 1];
                    }
                    block[i][this.size - 1] = 0;
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
                if (block[0][j] != 0) {
                    empty = false;
                    break;
                }
            }
            if (empty){
                for (int i = 0; i < this.size - 1; i++) {
                    for (int j = 0; j < this.size; j++) {
                        block[i][j] = block[i + 1][j];
                    }
                }
                for (int j = 0; j < this.size; j++) {
                    block[this.size - 1][j] = 0;
                }
            }
        } while (empty);
    }

}
