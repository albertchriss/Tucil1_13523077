package puzzle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReader {

    public int row, col, numBlocks;
    public String[] map;
    public Block[] blocks;
    public boolean validInput = true;
    public String mode, errorMsg;

    public void readInput(Path filePath){
        try {
            List<String> lines = Files.readAllLines(filePath);
            String[] firstLine = lines.get(0).split(" ");
            if (firstLine.length != 3){
                this.validInput = false;
                this.errorMsg = "Invalid input! First line must contain 3 integers only, N, M, and P!";
                return;
            }
            for (int i = 0; i < firstLine.length; i++){
                if (!isNumeric(firstLine[i])){
                    this.validInput = false;
                    this.errorMsg = "Invalid input! First line must contain 3 integers, N, M, and P!";
                    return;
                }
            }
            this.row = Integer.parseInt(firstLine[0]);
            this.col = Integer.parseInt(firstLine[1]);
            this.numBlocks = Integer.parseInt(firstLine[2]);

            this.mode = lines.get(1);
            if (!this.mode.equals("DEFAULT") && !this.mode.equals("CUSTOM") && !this.mode.equals("PYRAMID")){
                this.validInput = false;
                this.errorMsg = "Invalid mode! Mode must be either DEFAULT, CUSTOM, or PYRAMID";
                return;
            }

            int startBlockIdx = 2;
            if (this.mode.equals("CUSTOM")){
                List<String> mapLines = lines.subList(2, 2+this.row);
                startBlockIdx += this.row;
                if (!parseMap(mapLines, mode)){
                    return;
                }
            }

            List<String> blockLines = lines.subList(startBlockIdx, lines.size());
            if (!parseBlocks(blockLines)){
                this.validInput = false;
                this.errorMsg = "Invalid blocks input!";
                return;
            }
        } 
        catch (IOException e){
            this.validInput = false;
            this.errorMsg = ("Error reading file: " + e.getMessage());
        }
    }

    private boolean parseMap(List<String> lines, String mode){
        if (this.row != lines.size()){
            this.validInput = false;
            this.errorMsg = "Invalid map! Number of rows must be equal to N!";
            return false;
        }
        for (int i = 0; i < lines.size(); i++){
            if (this.col != lines.get(i).length()){
                this.validInput = false;
                this.errorMsg = "Invalid map! Number of columns must be equal to M!";
                return false;
            }
        }

        if (mode.equals("PYRAMID")) {
            if (this.row != this.col){
                this.validInput = false;
                this.errorMsg = "Invalid map for pyramid! N must be equal to M!";
                return false;
            }
            this.map = new String[this.row];
        }
        else{
            this.map = new String[this.row];
        }

        for(int i = 0; i < this.row; i++){
            String line = lines.get(i);
            for (int j = 0; j < this.col; j++){
                if(line.charAt(i) != 'X' && line.charAt(i) != '.'){
                    this.validInput = false;
                    this.errorMsg = "Invalid map! Map must only contain \'.\' or \'X\' character!";
                }
            }
            this.map[i] = line;
        }

        return true;
    }

    private boolean parseBlocks(List<String> lines){
        this.blocks = new Block[this.numBlocks];
        List<String> temp = new java.util.ArrayList<>();
        int j = 0;
        for (int i = 0; i < lines.size(); i++){
            String blockLine = lines.get(i);
            if (blockLine.length() == 0) continue;
            if (!isUppercase(blockLine)) return false;
            
            if (temp.size() == 0 || getChar(temp.get(0)) == getChar(blockLine)){
                temp.add(blockLine);
            }
            else{
                if (j >= this.numBlocks) return false;

                String[] block = new String[temp.size()];
                for (int k = 0; k < temp.size(); k++){
                    block[k] = temp.get(k);
                }
                this.blocks[j] = new Block(block);
                j++;
                temp.clear();
                temp.add(blockLine);
            }
        }
        if (temp.size() > 0){
            if (j >= this.numBlocks) return false;
            String[] block = new String[temp.size()];
            for (int k = 0; k < temp.size(); k++){
                block[k] = temp.get(k);
            }
            this.blocks[j] = new Block(block);
            temp.clear();
            j++;
        }

        if (j != this.numBlocks) return false;
        return true;
    }

    private char getChar(String s){
        for (int i = 0; i < s.length(); i++){
            if (Character.isLetter(s.charAt(i))) return s.charAt(i);
        }
        return ' ';
    }

    private boolean isUppercase(String s){
        for (int i = 0; i < s.length(); i++){
            if (!Character.isUpperCase(s.charAt(i)) && s.charAt(i) != ' ') return false;
        }
        return true;
    }

    private boolean isNumeric(String str) {
        if (str == null) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
